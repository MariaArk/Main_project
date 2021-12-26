package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.example.e_commerce.model.Course;
import com.example.e_commerce.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderPage extends Activity implements BillingProcessor.IBillingHandler {

    private BillingProcessor bp = null;
    private boolean readyToPurchase = false;

    Bundle savedInstanceState;
    Context context;

    private static final String PRODUCT_ID = "1";
    private static final String LICENSE_KEY = null;
    private static final String MERCHANT_ID=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        ListView order_list = findViewById(R.id.order_list);

        List<String> coursesTitle = new ArrayList<>();
        for(Course c: MainActivity.fullCourseList){
            if(Order.itemsId.contains(c.getId())) coursesTitle.add(c.getTitle());
        }

        order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coursesTitle));

        bp = new BillingProcessor(this, LICENSE_KEY, this );
        bp.initialize();


       /* if(!BillingProcessor.isIabServiceAvailable(this)) {
            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }/*/
        ImageButton button = (ImageButton) findViewById(R.id.imageButtonPay);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.purchase(OrderPage.this, "android.test.purchased");
            }
        });


    }


   /* public void payPush (View view){
        Intent intent = new Intent(this,OrderPage.class);
        startActivity(intent);
    }*/


    private void updateTextViews() {
        TextView text = findViewById(R.id.textView3);
        text.setText(String.format("%s is%s purchased", PRODUCT_ID, bp.isPurchased(PRODUCT_ID) ? "" : " not"));
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo purchaseInfo) {
        showToast("onProductPurchased: " + productId);
        updateTextViews();
    }


    @Override
    public  void onBillingError(int errorCode, @Nullable Throwable error) {
        showToast("onBillingError: " + errorCode);
    }
    @Override
    public void onBillingInitialized() {
                /*showToast("onBillingInitialized");
                readyToPurchase = true;
                updateTextViews();/*/
    }
    @Override
    public void onPurchaseHistoryRestored() {
                /*showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();*/
    }

}