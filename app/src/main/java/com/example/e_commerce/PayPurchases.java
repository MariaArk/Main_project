package com.example.e_commerce;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

//import com.google.android.gms.samples.wallet.util.Notifications;
//import com.google.android.gms.samples.wallet.util.PaymentsUtil;
//import com.google.android.gms.samples.wallet.R;
import androidx.annotation.NonNull;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PayPurchases extends OrderPage {
    private PaymentsClient paymentsClient;

    public PayPurchases() throws JSONException {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();

        paymentsClient = Wallet.getPaymentsClient(this,walletOptions);

    }

    IsReadyToPayRequest readyToPayRequest = IsReadyToPayRequest.fromJson(baseConfigurationJson().toString());

    private static JSONObject baseConfigurationJson() throws JSONException {
        return new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0)
                .put("allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));

    }

    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {{
                put("gateway", "sberbank");
                put("gatewayMerchantId", "Dandelion");
            }});
        }};
    }


/*    Task <Boolean> task = paymentsClient.isReadyToPay(readyToPayRequest);
    task.addOnComplete(this, new OnCompleteListener<Boolean>(){
       public void onComplete(@NonNull Task<Boolean> completeTask){
           if (completeTask.isSuccesseful()){
                showGooglePayButton(completeTask.getResult());
           } else {

           }
        }
    });*/

    private void  showGooglePayButton(boolean userIsReadyToPay){
        if(userIsReadyToPay){

        } else {

        }

    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        final String[] networks = new String[] {"VISA", "MASTERCARD", "МИР"};
        final String[] authMethods = new String[] {"RAY_ONLY","CRYPTOGRAM_3DS"};

        JSONObject card = new JSONObject();
        card.put("type", "CARD");
        card.put("tokenizationSpecification", getGatewayTokenizationSpecification());
        card.put("parameters", new JSONObject()
            .put("allowedAuthMethods", new JSONObject(String.valueOf(authMethods)))
            .put("allowedCardMethods", new JSONObject(String.valueOf(networks))));
        return card;
    }

/*    final JSONObject paymentRequestJson = baseConfigurationJson();
    paymentRequestJson.put("transactionInfo", new JSONObject()
        .put("totalPrice", "132.45")
        .put("totalPricestatus","FINAL")
        .put("currencyCode","USD"));

    paymentRequestJson.put("transactionInfo", new JSONObject()
        .put("merchantId", "01234567890123456789")
        .put("merchantName","Example merchant"));

    final PaymentDataRequest request = PaymentDataRequest.fromJson(paymentRequestJson.toString());
    AutoResolveHelper.resolveTask(paymentClient.loadPaymentData(request),this, LOAD_PAYMENT_DATA_REQUEST_CODE);
*/
}
