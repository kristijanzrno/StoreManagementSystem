package com.example.storemanagementsystem.Activities;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Data.PurchaseInvoice;
import Data.User;


public class WServiceClient {

    private String baseURL;
    private Gson gson;

    private static final String CONFIRMATION_JSON = "{\"result\":1}";

    public WServiceClient(String baseURL, Gson gson){
        this.baseURL = baseURL;
        this.gson = gson;
    }

    public void createCustomerPurchaseInvoice(PurchaseInvoice invoice, SendPostRequest.TransactionDelegate delegate){
        String json = gson.toJson(invoice);
        String url = baseURL + "/createPurchaseInvoice";
        SendPostRequest request = new SendPostRequest();
        request.transactionDelegate = delegate;
        request.execute(url,json);
    }

    public void getItem(String itemID, SendGetRequest.ClientFetchDelegate delegate){
        String url = baseURL + "/getItem/"+itemID;
        SendGetRequest request = new SendGetRequest();
        request.delegate = delegate;
        request.method = "getItem";
        request.execute(url);
    }

    public void login(User user, SendGetRequest.ClientFetchDelegate delegate){
        String url = baseURL + "/login?username="+user.getUsername()+"&password="+user.getPassword();
        SendGetRequest request = new SendGetRequest();
        request.delegate = delegate;
        request.method = "login";
        request.execute(url);
    }

    public static class SendPostRequest extends AsyncTask<String, String, String>{

        public TransactionDelegate transactionDelegate;

        public interface TransactionDelegate{
            void transactionFinished(boolean success);
        }
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals(CONFIRMATION_JSON))
                transactionDelegate.transactionFinished(true);
            else
                transactionDelegate.transactionFinished(false);
        }
    }

    public static class SendGetRequest extends AsyncTask<String, String, String>{

        public ClientFetchDelegate delegate;
        public String method;

        public interface ClientFetchDelegate{
            void onObjectFetched(String method, String json);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            delegate.onObjectFetched(method, s);
        }
    }


}
