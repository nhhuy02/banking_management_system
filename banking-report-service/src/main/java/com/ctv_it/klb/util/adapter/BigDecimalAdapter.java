package com.ctv_it.klb.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalAdapter extends TypeAdapter<BigDecimal> {

  @Override
  public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) throws IOException {
    jsonWriter.value(bigDecimal != null ? bigDecimal.toPlainString() : null);
  }

  @Override
  public BigDecimal read(JsonReader jsonReader) throws IOException {
    String value = jsonReader.nextString();
    return value != null ? new BigDecimal(value) : null;
  }
}
