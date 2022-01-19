package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ContactPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);
    }
    public void openMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openContact(View view){
        Intent intent = new Intent(this, ContactPage.class);
        startActivity(intent);
    }
    public void openAbout_us(View view){
        Intent intent = new Intent(this, About_usPage.class);
        startActivity(intent);
    }
}