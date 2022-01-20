package com.google.android.gms.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.R;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class PaymentTransparentActivity extends AppCompatActivity {

  // Произвольно выбранное постоянное целое число, которое мы определяем для отслеживания запроса на получение платежных данных.
  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Отключаем пользовательский интерфейс уведомлений, если действие было открыто из уведомления
    if (Notifications.ACTION_PAY_GOOGLE_PAY.equals(getIntent().getAction())) {
      sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
    
    showPaymentsSheet();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    
    switch (requestCode) {

      case LOAD_PAYMENT_DATA_REQUEST_CODE:
        switch (resultCode) {

          case Activity.RESULT_OK:
            PaymentData paymentData = PaymentData.getFromIntent(data);
            handlePaymentSuccess(paymentData);
            break;

          case Activity.RESULT_CANCELED:
            // Пользователь просто отменил платеж, не выбрав способ оплаты.
            break;

          case AutoResolveHelper.RESULT_ERROR:
            break;
        }

        // закрытие activity
        finishAndRemoveTask();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void showPaymentsSheet() {

    // Получаем цену на основе выбора пользователя
    long priceCents = getIntent().getLongExtra(Notifications.OPTION_PRICE_EXTRA, 2500L);

    // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
    Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
    if (!paymentDataRequestJson.isPresent()) {
      return;
    }

    PaymentDataRequest request =
        PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

    if (request != null) {
      final PaymentsClient paymentsClient = PaymentsUtil.createPaymentsClient(this);
      AutoResolveHelper.resolveTask(
          paymentsClient.loadPaymentData(request),
          this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }
  }

//    PaymentData объект ответа содержит платежную информацию, а также дополнительные
//    запрашиваемая информация, такая как адрес выставления счетов и доставки.
//   Данные об оплате Объект ответ, возвращаемый Google после оплаты, одобренной плательщиком.

  private void handlePaymentSuccess(PaymentData paymentData) {

    // Токен будет равен нулю, если запрос платежных данных не был создан с использованием PafromJson(String).
    final String paymentInfo = paymentData.toJson();
    if (paymentInfo == null) {
      return;
    }

    // Удалить уведомление об оплате
    Notifications.remove(this);

    try {
      JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");

      final JSONObject info = paymentMethodData.getJSONObject("info");
      final String billingName = info.getJSONObject("billingAddress").getString("name");
      Toast.makeText(
          this, getString(R.string.payments_show_name, billingName),
          Toast.LENGTH_LONG).show();

    } catch (JSONException e) {
      throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
    }
  }
}