package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.model.Course;
import com.example.e_commerce.model.Order;
import com.google.android.gms.samples.CheckoutActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderPage extends AppCompatActivity {

    public static int summa;


    public void PayPush(View view){
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        TextView summa_purchase = findViewById(R.id.summa_purchase);
        summa = 0;
        ListView order_list = findViewById(R.id.order_list);

        List<String> coursesTitle = new ArrayList<>();
        for (Course c : MainActivity.fullCourseList) {
            if (Order.itemsId.contains(c.getId())){
                summa += Integer.parseInt(c.getPrice());
                coursesTitle.add(c.getTitle());}
        }
        summa_purchase.setText(String.valueOf(summa));

        order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coursesTitle));


    }
}

