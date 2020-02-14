package com.example.storemanagementsystem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storemanagementsystem.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Adapters.BasketRecyclerViewAdapter;
import Data.CustomerPurchaseItem;
import Data.PurchaseInvoice;
import Data.StockItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasketActivity extends AppCompatActivity implements WServiceClient.SendGetRequest.ClientFetchDelegate, WServiceClient.SendPostRequest.TransactionDelegate, MessageDialogs.DialogResponses, BasketRecyclerViewAdapter.BasketItemActions {


    @BindView(R.id.basketRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.headerDescription)
    TextView headerDescription;

    WServiceClient client;
    BasketRecyclerViewAdapter adapter;
    Gson gson;

    PurchaseInvoice invoice = new PurchaseInvoice();
    private int userID = 1;
    private ArrayList<Float> localPrices = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        setupViews();

        userID = getIntent().getIntExtra("userID", -1);
        gson = new GsonBuilder().setFieldNamingStrategy(f -> f.getName().toLowerCase()).create();
        client = new WServiceClient("http://10.0.2.2:8080/StoreManagementSystemAPI/webresources/StoreManagement", gson);
        populateInvoice();
    }

    private void populateInvoice(){
        localPrices.clear();
        updateHeader();
        invoice.setUserID(userID);
        invoice.setInvoiceDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        invoice.setHasRentedItems(false);
        invoice.setInvoiceDescription("");
    }


    private void setupViews(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new BasketRecyclerViewAdapter(invoice.getItems(), this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateHeader(){
        float totalPrice = 0f;
        for(int i = 0; i<localPrices.size(); i++){
            totalPrice += invoice.getItems().get(i).getQuantity() * localPrices.get(i);
        }
        headerDescription.setText("Total Items: " + invoice.getItems().size() + " ("+String.format("%.2f", totalPrice)+"£)");
    }


    @OnClick(R.id.scanItem)
    public void scanItem(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @OnClick(R.id.addManually)
    public void addManually(View v){
        MessageDialogs.addManually(this, this);
    }

    @OnClick(R.id.confirmPurchase)
    public void confirmPurchase(View view){
        if(!invoice.getItems().isEmpty()) {
            MessageDialogs.confirmPurchase(this, this);
        }else{
            Toast.makeText(this, "The basket is empty...", Toast.LENGTH_SHORT).show();
        }
    }

    // Results from the scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null && result.getContents() != null){
                System.out.println(result.getContents());
                client.getItem(result.getContents(), this);
            }
        }
    }

    @Override
    public void onObjectFetched(String method, String json) {
        if(json.isEmpty() || json==null){
            Toast.makeText(this, "Could not find the item...", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(method){
            case "getItem":
                StockItem item;
                try {
                    item = gson.fromJson(json, StockItem.class);
                }catch (Exception e){
                    Toast.makeText(this, "Could not find the item...", Toast.LENGTH_SHORT).show();
                    return;
                }
                CustomerPurchaseItem customerPurchaseItem = new CustomerPurchaseItem();
                customerPurchaseItem.setItemID(""+item.getItemID());
                customerPurchaseItem.setItemName(item.getName());
                if(item.isRentable()) {
                    invoice.setHasRentedItems(true);
                    customerPurchaseItem.setDateRented(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                }
                if(!invoice.addItem(customerPurchaseItem))
                    localPrices.add(item.getCost() + item.getCost()*(item.getVAT()/100));
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
                updateHeader();
                break;
        }

    }

    @Override
    public void transactionFinished(boolean success) {
        if(success)
            MessageDialogs.transactionFinishedDialog(this, this);
        else
            Toast.makeText(this, "Could not complete purchase...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishTransaction(boolean answer) {
        if(answer)
            finish();
        else{
            populateInvoice();
            invoice.getItems().clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removeItem(boolean answer, int position) {
        if(answer){
            invoice.getItems().remove(position);
            adapter.notifyDataSetChanged();
            localPrices.remove(position);
        }
    }

    @Override
    public void addManually(String itemID) {
        client.getItem(itemID, this);
    }

    @Override
    public void confirmTransaction(boolean answer) {
        if(answer) {
            Toast.makeText(this, "Purchasing items...", Toast.LENGTH_SHORT).show();
            invoice.setInvoiceDescription("User: " + userID + " | NoOfItems: " + invoice.getItems().size());
            client.createCustomerPurchaseInvoice(invoice, this);
        }
    }


    @Override
    public void increaseQuantity(int position) {
        invoice.getItems().get(position).increaseQuantity();
        adapter.notifyDataSetChanged();
        updateHeader();
    }

    @Override
    public void decreaseQuantity(int position) {
        if(invoice.getItems().get(position).getQuantity() == 1){
            basketRemoveItem(position);
        }else {
            invoice.getItems().get(position).decreaseQuantity();
            adapter.notifyDataSetChanged();
        }
        updateHeader();
    }

    @Override
    public void basketRemoveItem(int position) {
        MessageDialogs.removeItemDialog(this, this, position);
    }
}

