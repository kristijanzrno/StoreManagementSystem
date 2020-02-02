package com.example.storemanagementsystem.Activities;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

import Data.PurchaseInvoice;



public class WServiceClient {

    String baseURL;
    public WServiceClient(String baseURL){
        this.baseURL = baseURL;
    }

    public void createCustomerPurchaseInvoice(PurchaseInvoice invoice){
        String json = new Gson().toJson(invoice);
        String url = baseURL + "/createPurchaseInvoice";
        new SendPostRequest().execute(url,json);


    }

    private class SendPostRequest extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) ((new URL(strings[0]).openConnection()));
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.connect();
                System.out.println("connected");

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(strings[1]);
                writer.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line);
                bufferedReader.close();
                return stringBuilder.toString();
            }catch(Exception e){e.printStackTrace();}
            return "";
        }
    }


}
