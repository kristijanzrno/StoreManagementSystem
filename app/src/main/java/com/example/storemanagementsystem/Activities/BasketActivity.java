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

public class BasketActivity extends AppCompatActivity{


    @BindView(R.id.basketRecyclerView)
    RecyclerView recyclerView;
    ProgressDialog pd;

    PurchaseInvoice invoice = new PurchaseInvoice();

    WServiceClient client;
    BasketRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.bind(this);
        setupViews();
        client = new WServiceClient("http://192.168.0.12:8080/StoreManagementSystem/webresources/StoreManagement");
        invoice.setUserID(2);
        invoice.setInvoiceDate(new SimpleDateFormat("yyyy-dd-MM").format(new Date()));
        invoice.setTotalPrice(12);
        invoice.setInvoiceDescription("invoice description");
    }



    private void setupViews(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new BasketRecyclerViewAdapter(invoice.getItems());
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
        Toast.makeText(this, "sending request", Toast.LENGTH_SHORT).show();
        client.createCustomerPurchaseInvoice(invoice);
    }


    // Results from the scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null && result.getContents() != null){
                System.out.println(result.getContents());
                new JsonTask().execute("http:/192.168.0.12:8080/StoreManagementSystem/webresources/StoreManagement/getItem/" + result.getContents());
            }
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(BasketActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            Gson gson = new GsonBuilder().setFieldNamingStrategy(f -> f.getName().toLowerCase()).create();
            StockItem item = gson.fromJson(result, StockItem.class);

            CustomerPurchaseItem customerPurchaseItem = new CustomerPurchaseItem();
            customerPurchaseItem.setItemID(""+item.getItemID());
            customerPurchaseItem.setItemName(item.getName());
            invoice.getItems().add(customerPurchaseItem);

            adapter.notifyDataSetChanged();


        }
    }


}

