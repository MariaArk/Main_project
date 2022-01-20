package com.google.android.gms.samples;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
// Содержит вспомогательные статические методы для работы с API платежей.
public class PaymentsUtil {

  public static final BigDecimal CENTS_IN_A_UNIT = new BigDecimal(100d);
// Создаем базовый объект запроса API Google Pay со свойствами, используемыми во всех запросах.
  private static JSONObject getBaseRequest() throws JSONException {
    return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
  }
// Создает экземпляр клиента платежей {@link PaymentsClient} для использования в операции {@link Activity} с использованием
//* окружающая среда и тема задаются в константах  {@link Constants}.
  public static PaymentsClient createPaymentsClient(Activity activity) {
    Wallet.WalletOptions walletOptions =
        new Wallet.WalletOptions.Builder().setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
    return Wallet.getPaymentsClient(activity, walletOptions);
  }
// В ответе API Google Play будет возвращен зашифрованный способ оплаты, который может взиматься
//с поддерживаемого шлюза после авторизации плательщика.
  private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
    return new JSONObject() {{
      put("type", "PAYMENT_GATEWAY");
      put("parameters", new JSONObject() {{
        put("gateway", "example");
        put("gatewayMerchantId", "exampleGatewayMerchantId");
      }});
    }};
  }

  private static JSONObject getDirectTokenizationSpecification()
      throws JSONException, RuntimeException {
    if (Constants.DIRECT_TOKENIZATION_PARAMETERS.isEmpty()
        || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY.isEmpty()
        || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY == null
        || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY == "REPLACE_ME") {
      throw new RuntimeException(
          "Please edit the Constants.java file to add protocol version & public key.");
    }
    JSONObject tokenizationSpecification = new JSONObject();

    tokenizationSpecification.put("type", "DIRECT");
    JSONObject parameters = new JSONObject(Constants.DIRECT_TOKENIZATION_PARAMETERS);
    tokenizationSpecification.put("parameters", parameters);

    return tokenizationSpecification;
  }
// Card network, поддерживаемые вашим приложением и вашим шлюзом.
  private static JSONArray getAllowedCardNetworks() {
    return new JSONArray(Constants.SUPPORTED_NETWORKS);
  }

  private static JSONArray getAllowedCardAuthMethods() {
    return new JSONArray(Constants.SUPPORTED_METHODS);
  }

// Предоставленные свойства применимы как к запросу "Готов заплатить", так и к
// PaymentDataRequest.

//@return объект Метода оплаты КАРТОЙ, описывающий принятые карты.
  private static JSONObject getBaseCardPaymentMethod() throws JSONException {
    JSONObject cardPaymentMethod = new JSONObject();
    cardPaymentMethod.put("type", "CARD");

    JSONObject parameters = new JSONObject();
    parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
    parameters.put("allowedCardNetworks", getAllowedCardNetworks());

    JSONObject billingAddressParameters = new JSONObject();
    billingAddressParameters.put("format", "FULL");

    parameters.put("billingAddressParameters", billingAddressParameters);

    cardPaymentMethod.put("parameters", parameters);

    return cardPaymentMethod;
  }
// Способ оплаты КАРТОЙ с описанием принимаемых карт и дополнительных полей.
  private static JSONObject getCardPaymentMethod() throws JSONException {
    JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
    cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

    return cardPaymentMethod;
  }
// API version and payment methods supported by the app.
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static Optional<JSONObject> getIsReadyToPayRequest() {
    try {
      JSONObject isReadyToPayRequest = getBaseRequest();
      isReadyToPayRequest.put(
          "allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));

      return Optional.of(isReadyToPayRequest);

    } catch (JSONException e) {
      return Optional.empty();
    }
  }
//  Предоставляем API Google Pay сумму платежа, валюту и статус суммы.
  private static JSONObject getTransactionInfo(String price) throws JSONException {
    JSONObject transactionInfo = new JSONObject();
    transactionInfo.put("totalPrice", price);
    transactionInfo.put("totalPriceStatus", "FINAL");
    transactionInfo.put("countryCode", Constants.COUNTRY_CODE);
    transactionInfo.put("currencyCode", Constants.CURRENCY_CODE);
    transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

    return transactionInfo;
  }
// Информация о продавце, запрашивающем платежную информацию
  private static JSONObject getMerchantInfo() throws JSONException {
    return new JSONObject().put("merchantName", "Example Merchant");
  }
// Объект, описывающий информацию, запрошенную в платежной ведомости Google Pay
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static Optional<JSONObject> getPaymentDataRequest(long priceCents) {

    final String price = PaymentsUtil.centsToString(priceCents);

    try {
      JSONObject paymentDataRequest = PaymentsUtil.getBaseRequest();
      paymentDataRequest.put(
          "allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod()));
      paymentDataRequest.put("transactionInfo", PaymentsUtil.getTransactionInfo(price));
      paymentDataRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo());

      return Optional.of(paymentDataRequest);

    } catch (JSONException e) {
      return Optional.empty();
    }
  }
// Преобразует центы в строковый формат.
  public static String centsToString(long cents) {
    return new BigDecimal(cents)
        .divide(CENTS_IN_A_UNIT, RoundingMode.HALF_EVEN)
        .setScale(2, RoundingMode.HALF_EVEN)
        .toString();
  }
}
