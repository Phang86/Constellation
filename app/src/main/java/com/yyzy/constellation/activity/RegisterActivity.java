package com.yyzy.constellation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yyzy.constellation.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        loadData();
    }

    private void initView() {
        tv = findViewById(R.id.btnRegister_tv_login);
        tv.setOnClickListener(this);
    }

    private void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister_tv_login:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
}