package com.example.sebas.hotdogadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mInventoryButton;
    private Button mOrderButton;
    private Button mPricesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mInventoryButton = (Button) findViewById(R.id.inventory);
        mOrderButton = (Button) findViewById(R.id.order);
        mPricesButton = (Button) findViewById(R.id.pricing);

         mInventoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);
        });

        mOrderButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        mPricesButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PriceActivity.class);
            startActivity(intent);
        });

    }
}
