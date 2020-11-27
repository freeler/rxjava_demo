package com.freeler.demo.annotation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class MyFieldTest extends AppCompatActivity {

    @MyField(des = "data数据", len = 6)
    private String data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testMyField();
    }

    public void testMyField() {
        Class<MyFieldTest> c = MyFieldTest.class;
        for (Field f : c.getDeclaredFields()) {
            if (f.isAnnotationPresent(MyField.class)) {
                MyField annotation = f.getAnnotation(MyField.class);
                System.out.println("des = " + annotation.des() + "，len = " + annotation.len());
            }
        }

    }

}
