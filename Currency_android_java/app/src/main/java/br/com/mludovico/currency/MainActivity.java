package br.com.mludovico.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<String, String> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView updatedLabel = findViewById(R.id.updatedLabel);
        EditText usdField = findViewById(R.id.usdTextInput);
        EditText eurField = findViewById(R.id.eurTextInput);
        EditText gbpField = findViewById(R.id.gbpTextInput);
        EditText brlField = findViewById(R.id.brlTextInput);
        EditText btcField = findViewById(R.id.btcTextInput);


    }

    void getFromUsd(){

    }

    void getFromEur(){

    }

    void getFromGbp(){

    }

    void getFromBrl(){

    }

    void getFromBtc(){

    }

}