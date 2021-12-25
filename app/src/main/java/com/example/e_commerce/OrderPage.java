package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.model.Course;
import com.example.e_commerce.model.Order;
import com.google.android.gms.samples.wallet.activity.CheckoutActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderPage extends AppCompatActivity {



    public void PayPush(View view){
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        ListView order_list = findViewById(R.id.order_list);

        List<String> coursesTitle = new ArrayList<>();
        for (Course c : MainActivity.fullCourseList) {
            if (Order.itemsId.contains(c.getId())) coursesTitle.add(c.getTitle());
        }

        order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coursesTitle));


    }
}

