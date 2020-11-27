package com.freeler.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freeler.demo.annotation.MyFieldTest;
import com.freeler.demo.rxjava.BoolActivity;
import com.freeler.demo.rxjava.CreateActivity;
import com.freeler.demo.rxjava.FilterActivity;
import com.freeler.demo.rxjava.MapActivity;

/**
 * @author: xuzeyang
 * @Date: 2020/5/6
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.createDemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });
        findViewById(R.id.mapDemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
        findViewById(R.id.filterDemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FilterActivity.class));
            }
        });
        findViewById(R.id.boolDemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BoolActivity.class));
            }
        });

        findViewById(R.id.annotationSimple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyFieldTest.class));
            }
        });
    }
}
