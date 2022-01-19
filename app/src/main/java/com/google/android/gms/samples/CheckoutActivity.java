package com.google.android.gms.samples;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.OrderPage;
import com.example.e_commerce.R;
import com.example.e_commerce.databinding.ActivityCheckoutBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Optional;


public class CheckoutActivity extends AppCompatActivity {

// Произвольно выбранное постоянное целое число, которое вы определяете для отслеживания запроса на получение платежных данных.
  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

  private static final long SHIPPING_COST_CENTS = 100 * PaymentsUtil.CENTS_IN_A_UNIT.longValue();

// Клиент для взаимодействия с API Google Play.
  private PaymentsClient paymentsClient;

  private ActivityCheckoutBinding layoutBinding;
  private View googlePayButton;

  private JSONArray garmentList;
  private JSONObject selectedGarment;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_page);

    initializeUi();

    // Создавайте каналы уведомлений в соответствии с рекомендациями
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Notifications.createNotificationChannelIfNotCreated(this);
    }

    // Настройте макет информации для нашего элемента в пользовательском интерфейсе.
    try {
      selectedGarment = Buy_Garment();
      displayGarment(selectedGarment);
    } catch (JSONException e) {
      throw new RuntimeException("The list of garments cannot be loaded");
    }

// Инициализируйте клиент API Google Pay для среды, подходящей для тестирования.
    // Рекомендуется создать объект клиента платежей внутри метода onCreate.    paymentsClient = PaymentsUtil.createPaymentsClient(this);
    possiblyShowGooglePayButton();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menus, menu);
    return true;
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.send_notification:
        Notifications.triggerPaymentNotification(this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @SuppressLint("MissingSuperCall")
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      // значение, переданное в AutoResolveHelper
      case LOAD_PAYMENT_DATA_REQUEST_CODE:
        switch (resultCode) {

          case Activity.RESULT_OK:
            PaymentData paymentData = PaymentData.getFromIntent(data);
            handlePaymentSuccess(paymentData);
            break;

          case Activity.RESULT_CANCELED:
            // Пользователь отменил попытку оплаты
            break;

          case AutoResolveHelper.RESULT_ERROR:
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            handleError(status.getStatusCode());
            break;
        }

        // Повторно включите кнопку оплаты в Google Play.
        googlePayButton.setClickable(true);
    }
  }

  private void initializeUi() {

    // Используйте привязку представления для доступа к элементам пользовательского интерфейса
    layoutBinding = ActivityCheckoutBinding.inflate(getLayoutInflater());
    setContentView(layoutBinding.getRoot());

    // Отключите пользовательский интерфейс уведомлений, если действие было открыто из уведомления
    if (Notifications.ACTION_PAY_GOOGLE_PAY.equals(getIntent().getAction())) {
      sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    // Кнопка Google Pay представляет собой файл макета – используйте корневой вид
    googlePayButton = layoutBinding.googlePayButton.getRoot();
    googlePayButton.setOnClickListener(
        new View.OnClickListener() {
          @RequiresApi(api = Build.VERSION_CODES.N)
          @Override
          public void onClick(View view) {
            requestPayment(view);
          }
        });
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void displayGarment(JSONObject garment) throws JSONException {
    garment.put("price", OrderPage.summa);
    layoutBinding.detailPrice.setText(
            String.format(Locale.getDefault(), "$%.2f", garment.getDouble("price")));

    final String escapedHtmlText = Html.fromHtml(
            garment.getString("description"), Html.FROM_HTML_MODE_COMPACT).toString();
    layoutBinding.detailDescription.setText(Html.fromHtml(
            escapedHtmlText, Html.FROM_HTML_MODE_COMPACT));

    final String imageUri = String.format("@drawable/%s", garment.getString("image"));
    final int imageResource = getResources().getIdentifier(imageUri, null, getPackageName());
    layoutBinding.detailImage.setImageResource(imageResource);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void possiblyShowGooglePayButton() {

    final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
    if (!isReadyToPayJson.isPresent()) {
      return;
    }

    // Вызов Готов заплатить является асинхронным и возвращает задачу. Нам нужно предоставить
    // OnCompleteListener запускается, когда известен результат вызова.
    IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
    Task<Boolean> task = paymentsClient.isReadyToPay(request);
    task.addOnCompleteListener(this,
        new OnCompleteListener<Boolean>() {
          @Override
          public void onComplete(@NonNull Task<Boolean> task) {
            if (task.isSuccessful()) {
              setGooglePayAvailable(task.getResult());
            } else {
              Log.w("isReadyToPay failed", task.getException());
            }
          }
        });
  }

  private void setGooglePayAvailable(boolean available) {
    if (available) {
      googlePayButton.setVisibility(View.VISIBLE);
    } else {
      Toast.makeText(this, R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
    }
  }

  private void handlePaymentSuccess(PaymentData paymentData) {

    // Токен будет равен нулю, если запрос платежных данных не был создан с использованием fromJson(String).
    final String paymentInfo = paymentData.toJson();
    if (paymentInfo == null) {
      return;
    }

    try {
      JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
// Если для шлюза установлено значение "пример", платежная информация не возвращается - вместo этого токен
// будет состоять только из "Examplepaymentmethod токена".

      final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
      final String tokenizationType = tokenizationData.getString("type");
      final String token = tokenizationData.getString("token");

      if ("PAYMENT_GATEWAY".equals(tokenizationType) && "examplePaymentMethodToken".equals(token)) {
        new AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage(getString(R.string.gateway_replace_name_example))
            .setPositiveButton("OK", null)
            .create()
            .show();
      }

      final JSONObject info = paymentMethodData.getJSONObject("info");
      final String billingName = info.getJSONObject("billingAddress").getString("name");
      Toast.makeText(
          this, getString(R.string.payments_show_name, billingName),
          Toast.LENGTH_LONG).show();

// Строка маркера регистрации.

      Log.d("Google Pay token: ", token);

    } catch (JSONException e) {
      throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
    }
  }

  private void handleError(int statusCode) {
    Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void requestPayment(View view) {

    // Отключает кнопку, чтобы предотвратить многократные нажатия.
    googlePayButton.setClickable(false);

    // Цена, предоставляемая API, должна включать налоги и доставку.
    // Эта цена не отображается пользователю.
    try {
      double garmentPrice = selectedGarment.getDouble("price");
      long garmentPriceCents = Math.round(garmentPrice * PaymentsUtil.CENTS_IN_A_UNIT.longValue());
      long priceCents = garmentPriceCents + SHIPPING_COST_CENTS;

      Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
      if (!paymentDataRequestJson.isPresent()) {
        return;
      }

      PaymentDataRequest request =
          PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

// Поскольку данные о загрузке платежа могут отображать пользовательский интерфейс с просьбой пользователя выбрать способ оплаты, мы используем
// Помощник по авторазрешению, чтобы дождаться взаимодействия пользователя с ним. После завершения
// onActivityResult будет вызван с результатом.
      if (request != null) {
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(request),
            this, LOAD_PAYMENT_DATA_REQUEST_CODE);
      }

    } catch (JSONException e) {
      throw new RuntimeException("The price cannot be deserialized from the JSON object.");
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private JSONObject Buy_Garment() {

    // Only load the list of items if it has not been loaded before
    if (garmentList == null) {
      garmentList = Json.readFromResources(this, R.raw.course);
    }

    // Take a random element from the list
    int Index = garmentList.length() -1;
    try {
      return garmentList.getJSONObject(Index);
    } catch (JSONException e) {
      throw new RuntimeException("The index specified is out of bounds.");
    }
  }
}
