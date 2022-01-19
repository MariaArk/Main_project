package com.google.android.gms.samples.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.e_commerce.MainActivity;
import com.example.e_commerce.OrderPage;
import com.example.e_commerce.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Authentication extends AppCompatActivity {

    SignInButton btSignInButton;
    GoogleSignInClient mGoogleSignInClient;
    TextView tv_google_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        btSignInButton = findViewById(R.id.bt_google_sign);
        tv_google_name = findViewById(R.id.tv_google_name);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent =mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handlerSignInResult(task);
        }
    }

    public void handlerSignInResult(Task<GoogleSignInAccount> task) {
        try {

            GoogleSignInAccount account = task.getResult(ApiException.class);
            google_sign_in_ok(account);
        } catch (Exception e){

            google_sign_in_ok(null);
        }
    }

    public void google_sign_in_ok(GoogleSignInAccount account){

        if (account == null) {
            tv_google_name.setText("Вход не выполнен");
        }
        else {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}