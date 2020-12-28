package br.com.mludovico.currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    TextView updatedLabel;
    EditText usdField;
    EditText eurField;
    EditText gbpField;
    EditText brlField;
    EditText btcField;
    private HashMap<String, String> rates;
    private String updated;
    private RequestQueue requestQueue;
    private boolean enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rates = new HashMap();

        updatedLabel = findViewById(R.id.updatedLabel);
        usdField = findViewById(R.id.usdTextInput);
        eurField = findViewById(R.id.eurTextInput);
        gbpField = findViewById(R.id.gbpTextInput);
        brlField = findViewById(R.id.brlTextInput);
        btcField = findViewById(R.id.btcTextInput);

        usdField.addTextChangedListener(new Watcher(usdField));
        eurField.addTextChangedListener(new Watcher(eurField));
        gbpField.addTextChangedListener(new Watcher(gbpField));
        brlField.addTextChangedListener(new Watcher(brlField));
        btcField.addTextChangedListener(new Watcher(btcField));

        enabled = true;

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        loadRates(null);
    }

    public void loadRates(View view) {

        String url = "https://api.exchangerate.host/latest?base=USD&symbols=USD,EUR,GBP,BRL,BTC";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject results = response.getJSONObject("rates");
                    rates.put("USD", results.getString("USD"));
                    rates.put("EUR", results.getString("EUR"));
                    rates.put("GBP", results.getString("GBP"));
                    rates.put("BRL", results.getString("BRL"));
                    rates.put("BTC", results.getString("BTC"));
                } catch (JSONException e) {
                    Log.e("HTTP Request", "Json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon list error", error);
            }
        });

        requestQueue.add(request);
        SimpleDateFormat brTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        brTimeFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        updated = brTimeFormat.format(new Date());
        updateUpdatedLabel();
    }

    private void updateUpdatedLabel() {
        updatedLabel.setText(updated);
    }

    private void updateTextFields(HashMap<String, Double> data) {
        if(!usdField.hasFocus())
            usdField.setText(String.format("0.2f", data.get("USD").toString()));
        if(!eurField.hasFocus())
            eurField.setText(data.get("EUR").toString());
        if(!gbpField.hasFocus())
            gbpField.setText(data.get("GBP").toString());
        if(!brlField.hasFocus())
            brlField.setText(data.get("BRL").toString());
        if(!btcField.hasFocus())
            btcField.setText(data.get("BTC").toString());
    }

    void getExchange(View v){
        EditText field = (EditText)v;
        String key = v.getTag().toString();
        String value = field.getText().toString().length() > 0 ? field.getText().toString() : "0";
        HashMap<String, Double> data = new HashMap<>();
        data.put("USD", Double.parseDouble(value) / Double.parseDouble(rates.get(key)));
        for(String rate : rates.keySet()) {
            data.put(rate, data.get("USD") * Double.parseDouble(rates.get(rate)));
        }
        updateTextFields(data);
    }

    private class Watcher implements TextWatcher {

        private EditText field;

        public Watcher(EditText nField) {
            this.field = nField;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("Listener", "Texedit before changed");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("Listener", "Texedit on changed");
            if(field.hasFocus()){
                getExchange(field);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("Listener", "Texedit after changed");
            enabled = true;
        }
    }
}