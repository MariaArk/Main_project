package com.google.android.gms.samples;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Json {

  @RequiresApi(api = Build.VERSION_CODES.N)
  public static JSONArray readFromFile(Context context, String fileName) {
    try {
      final InputStream inputStream = context.getAssets().open(fileName);
      return readFromInputStream(inputStream);

    } catch (Exception e) {
      return new JSONArray();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public static JSONArray readFromResources(Context context, int resource) {
    try {
      final InputStream inputStream = context.getResources().openRawResource(resource);
      return readFromInputStream(inputStream);

    } catch (Exception e) {
      return new JSONArray();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private static JSONArray readFromInputStream(InputStream inputStream) throws JSONException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    final String inputString = reader.lines().collect(Collectors.joining());
    return new JSONArray(inputString);
  }
}