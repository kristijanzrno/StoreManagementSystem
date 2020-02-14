package com.example.storemanagementsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.storemanagementsystem.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.NoSuchAlgorithmException;

import Data.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements WServiceClient.SendGetRequest.ClientFetchDelegate {

    @BindView(R.id.usernameTB)
    EditText usernameTB;
    @BindView(R.id.passwordTB)
    EditText passwordTB;

    WServiceClient client;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        gson = new GsonBuilder().setFieldNamingStrategy(f -> f.getName().toLowerCase()).create();
        client = new WServiceClient("http://10.0.2.2:8080/StoreManagementSystemAPI/webresources/StoreManagement", gson);

    }

    @OnClick(R.id.loginButton)
    public void login(View v) {
        User user = new User();
        user.setUsername(usernameTB.getText().toString());
        try {
            user.setEncryptedPassword(passwordTB.getText().toString());
            System.out.println(user.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        client.login(user, this);
    }

    @Override
    public void onObjectFetched(String method, String json) {
        switch (method) {
            case "login":
                if (json != null) {
                    User user = gson.fromJson(json, User.class);
                    if (user != null) {
                        Intent i = new Intent(this, BasketActivity.class);
                        i.putExtra("userID", user.getUserID());
                        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                        passwordTB.setText("");
                        startActivity(i);
                        return;
                    }
                }
                Toast.makeText(this, "Could not log in...", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
