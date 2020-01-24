package com.example.storemanagementsystem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.storemanagementsystem.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import Adapters.BasketRecyclerViewAdapter;
import Data.PurchaseItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasketActivity extends AppCompatActivity{


    @BindView(R.id.basketRecyclerView)
    RecyclerView recyclerView;

    private ArrayList<PurchaseItem> items = new ArrayList<>();
    BasketRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.bind(this);
        addTestData();
        setupViews();
    }


    private void addTestData(){
        for(int i = 0; i<3; i++){
            PurchaseItem item = new PurchaseItem();
            item.setId("id");
            item.setItemName("Test item " + i);
            item.setQuantity(i);
            items.add(item);
        }
    }

    private void setupViews(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new BasketRecyclerViewAdapter(items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.scanItem)
    public void scanItem(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Please scan an item QR Code...");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @OnClick(R.id.confirmPurchase)
    public void confirmPurchase(View view){
        Toast.makeText(this, "Confirm purchase TODO...", Toast.LENGTH_SHORT).show();
    }


    // Results from the scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null && result.getContents() != null){
                PurchaseItem item = new PurchaseItem();
                item.setId("id");
                item.setItemName("Scanned item n");
                item.setQuantity(1);
                items.add(item);
                Toast.makeText(this, "Successfully scanned item", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }
    }


}

