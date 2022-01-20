package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class About_usPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_page);
    }
    // В данных  методах мы осуществляем переходы на страницы, которые расположены на боковой панеле
    public void openMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openContact(View view){
        Intent intent = new Intent(this, ContactPage.class);
        startActivity(intent);
    }

}