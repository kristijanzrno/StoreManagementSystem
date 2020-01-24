package com.example.storemanagementsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.storemanagementsystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.usernameTB)
    EditText usernameTB;
    @BindView(R.id.passwordTB)
    EditText passwordTB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    public void login(View v){
        // If authentication == true
        Intent i = new Intent(LoginActivity.this, BasketActivity.class);
        startActivity(i);
    }
}
