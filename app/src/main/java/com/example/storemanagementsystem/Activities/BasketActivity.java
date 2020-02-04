package com.example.storemanagementsystem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.storemanagementsystem.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapters.BasketRecyclerViewAdapter;
import Data.CustomerPurchaseItem;
import Data.PurchaseInvoice;
import Data.PurchaseItem;
import Data.StockItem;
import Data.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasketActivity extends AppCompatActivity implements WServiceClient.SendGetRequest.ClientFetchDelegate, WServiceClient.SendPostRequest.TransactionDelegate, MessageDialogs.DialogResponses, BasketRecyclerViewAdapter.BasketItemActions {


    @BindView(R.id.basketRecyclerView)
    RecyclerView recyclerView;

    ProgressDialog pd;

    WServiceClient client;
    BasketRecyclerViewAdapter adapter;
    Gson gson;

    PurchaseInvoice invoice = new PurchaseInvoice();
    private int userID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.bind(this);
        setupViews();
        userID = getIntent().getIntExtra("userID", -1);
        gson = new GsonBuilder().setFieldNamingStrategy(f -> f.getName().toLowerCase()).create();
        client = new WServiceClient("http://192.168.0.12:8080/StoreManagementSystem/webresources/StoreManagement", gson);
        populateInvoice();
    }

    private void populateInvoice(){
        invoice.setUserID(userID);
        invoice.setInvoiceDate(new SimpleDateFormat("yyyy-dd-MM").format(new Date()));
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


    @OnClick(R.id.scanItem)
    public void scanItem(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @OnClick(R.id.confirmPurchase)
    public void confirmPurchase(View view){
        if(!invoice.getItems().isEmpty()) {
            Toast.makeText(this, "Purchasing items...", Toast.LENGTH_SHORT).show();
            invoice.setInvoiceDescription("User: " + userID + " | NoOfItems: " + invoice.getItems().size());
            client.createCustomerPurchaseInvoice(invoice, this);
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
        if(json.isEmpty() || json==null) return;
        switch(method){
            case "getItem":
                StockItem item = gson.fromJson(json, StockItem.class);
                if(item.isRentable())
                    invoice.setHasRentedItems(true);
                CustomerPurchaseItem customerPurchaseItem = new CustomerPurchaseItem();
                customerPurchaseItem.setItemID(""+item.getItemID());
                customerPurchaseItem.setItemName(item.getName());
                invoice.getItems().add(customerPurchaseItem);
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void transactionFinished(boolean success) {
        if(success)
            MessageDialogs.transactionFinishedDialog(this, this);
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
        }
    }


    @Override
    public void increaseQuantity(int position) {
        invoice.getItems().get(position).increaseQuantity();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void decreaseQuantity(int position) {
        if(invoice.getItems().get(position).getQuantity() == 1){
            basketRemoveItem(position);
        }else {
            invoice.getItems().get(position).increaseQuantity();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void basketRemoveItem(int position) {
        MessageDialogs.removeItemDialog(this, this, position);
    }
}

