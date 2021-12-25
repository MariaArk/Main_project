package com.example.e_commerce;

import static com.google.android.gms.samples.wallet.util.PayPurchases.getBaseRequest;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.google.android.gms.samples.wallet.Constants;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

//import com.google.android.gms.samples.wallet.util.Notifications;
//import com.google.android.gms.samples.wallet.util.PaymentsUtil;
//import com.google.android.gms.samples.wallet.R;

public class PayPurchases extends OrderPage {

    private PaymentsClient paymentsClient;
    public static final BigDecimal CENTS_IN_A_UNIT = new BigDecimal(100d);

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


    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));

            return Optional.of(isReadyToPayRequest);

        } catch (JSONException e) {
            return Optional.empty();
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

    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", Constants.COUNTRY_CODE);
        transactionInfo.put("currencyCode", Constants.CURRENCY_CODE);
        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

        return transactionInfo;
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject().put("merchantName", "Example Merchant");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<JSONObject> getPaymentDataRequest(long priceCents) {

        final String price = PayPurchases.centsToString(priceCents);

        try {
            JSONObject paymentDataRequest = PayPurchases.baseConfigurationJson();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(PayPurchases.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", PayPurchases.getTransactionInfo(price));
            paymentDataRequest.put("merchantInfo", PayPurchases.getMerchantInfo());

      /* An optional shipping address requirement is a top-level property of the PaymentDataRequest
      JSON object. */
            paymentDataRequest.put("shippingAddressRequired", true);

            JSONObject shippingAddressParameters = new JSONObject();
            shippingAddressParameters.put("phoneNumberRequired", false);

            JSONArray allowedCountryCodes = new JSONArray(Constants.SHIPPING_SUPPORTED_COUNTRIES);

            shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);
            paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);
            return Optional.of(paymentDataRequest);

        } catch (JSONException e) {
            return Optional.empty();
        }
    }

        public static String centsToString ( long cents){
            return new BigDecimal(cents)
                    .divide(CENTS_IN_A_UNIT, RoundingMode.HALF_EVEN)
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .toString();
        }
}
