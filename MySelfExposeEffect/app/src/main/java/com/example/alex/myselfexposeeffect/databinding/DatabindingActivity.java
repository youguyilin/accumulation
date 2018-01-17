package com.example.alex.myselfexposeeffect.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.alex.myselfexposeeffect.R;

public class DatabindingActivity extends AppCompatActivity {
    private com.example.alex.myselfexposeeffect.databinding.ActivityDatabindingBinding mComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databinding);
        mComponent =  DataBindingUtil.setContentView(this,R.layout.activity_databinding);
        mComponent.textView.setText("Databinding 基本使用");


    }

}
