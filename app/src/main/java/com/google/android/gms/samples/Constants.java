package com.google.android.gms.samples;

import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Constants {
// Инициализируем все необходимое для работы
  public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

  public static final List<String> SUPPORTED_NETWORKS = Arrays.asList(
      "MASTERCARD",
      "VISA");

  public static final List<String> SUPPORTED_METHODS = Arrays.asList(
      "PAN_ONLY",
      "CRYPTOGRAM_3DS");

  public static final String COUNTRY_CODE = "RU";

  public static final String CURRENCY_CODE = "RUS";

  public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";

  public static final HashMap<String, String> PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS =
      new HashMap<String, String>() {{
        put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME);
        put("gatewayMerchantId", "exampleGatewayMerchantId");
// нам могут потребоваться дополнительные параметры.
      }};

  public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

  public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS =
      new HashMap<String, String>() {{
        put("protocolVersion", "ECv2");
        put("publicKey", DIRECT_TOKENIZATION_PUBLIC_KEY);
      }};
}
