package com.example.sebas.hotdogadmin;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class InventoryActivity extends AppCompatActivity{

    private TextView mHotDogCounter;
    private TextView mBbqSauceCounter;
    private TextView mKetchupCounter;
    private TextView mMayonnaiseCounter;
    private TextView mCurryCounter;
    private TextView mOnionCounter;
    private TextView mCheeseCounter;

    private int mHotDogNo;
    private int mBbqSauceNo;
    private int mKetchupNo;
    private int mMayonnaiseNo;
    private int mCurryNo;
    private int mOnionNo;
    private int mCheeseNo;

    private TextView mHotDogStock;
    private TextView mBbqSauceStock;
    private TextView mKetchupStock;
    private TextView mMayonnaiseStock;
    private TextView mCurryStock;
    private TextView mOnionStock;
    private TextView mCheeseStock;

    private EditText mHotDogAdd;
    private EditText mBbqSauceAdd;
    private EditText mKetchupAdd;
    private EditText mMayonnaiseAdd;
    private EditText mCurryAdd;
    private EditText mOnionAdd;
    private EditText mCheeseAdd;



    private Button mAdd;
    private MobileServiceClient mClient;
    private MobileServiceTable<HotDogItem> mHotDogTable;
    private MobileServiceTable<InventoryItem> mInventoryTable;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://hotdog2.azurewebsites.net",
                    this).withFilter(new InventoryActivity.ProgressFilter());

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            // Get the Mobile Service Table instance to use

            mHotDogTable = mClient.getTable(HotDogItem.class);
            mInventoryTable = mClient.getTable(InventoryItem.class);


        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }

        mHotDogCounter = (TextView) findViewById(R.id.current_hotdog_no);
        mBbqSauceCounter = (TextView) findViewById(R.id.current_bbq_no);
        mKetchupCounter = (TextView) findViewById(R.id.current_ketchup_no);
        mMayonnaiseCounter = (TextView) findViewById(R.id.current_mayonnaise_no);
        mCurryCounter = (TextView) findViewById(R.id.current_curry_no);
        mOnionCounter = (TextView) findViewById(R.id.current_onion_no);
        mCheeseCounter = (TextView) findViewById(R.id.current_cheese_no);

        mHotDogStock = (TextView) findViewById(R.id.current_hotdog_stock);
        mBbqSauceStock = (TextView) findViewById(R.id.current_bbq_stock);
        mKetchupStock = (TextView) findViewById(R.id.current_ketchup_stock);
        mMayonnaiseStock = (TextView) findViewById(R.id.current_mayonnaise_stock);
        mCurryStock = (TextView) findViewById(R.id.current_curry_stock);
        mOnionStock = (TextView) findViewById(R.id.current_onion_stock);
        mCheeseStock = (TextView) findViewById(R.id.current_cheese_stock);

        mHotDogAdd = (EditText) findViewById(R.id.hotdog_add);
        mBbqSauceAdd = (EditText) findViewById(R.id.bbq_sauce_add);
        mKetchupAdd = (EditText) findViewById(R.id.ketchup_add);
        mMayonnaiseAdd = (EditText) findViewById(R.id.mayonnaise_add);
        mCurryAdd = (EditText) findViewById(R.id.curry_add);
        mOnionAdd = (EditText) findViewById(R.id.onion_add);
        mCheeseAdd = (EditText) findViewById(R.id.cheese_add);

        mAdd = (Button) findViewById(R.id.add);

        refreshItemsFromTable();
        refreshStocksFromTable();

        mAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                updateStock();

            }
        });
    }

    private void updateStock(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {

                    int mHotDogPrice = Integer.parseInt(mHotDogAdd.getText().toString());
                    int mBbqSaucePrice = Integer.parseInt(mBbqSauceAdd.getText().toString());
                    int mKetchupPrice = Integer.parseInt(mKetchupAdd.getText().toString());
                    int mMayonnaisePrice = Integer.parseInt(mMayonnaiseAdd.getText().toString());
                    int mCurryPrice = Integer.parseInt(mCurryAdd.getText().toString());
                    int mOnionPrice = Integer.parseInt(mOnionAdd.getText().toString());
                    int mCheesePrice = Integer.parseInt(mCheeseAdd.getText().toString());

                    InventoryItem result = mInventoryTable
                            .lookUp("797f796d-5f19-45d1-852c-4142462f9420")

                            .get();
                    int mHotdogS = result.getHotdogStock();
                    int mBbqS = result.getBbqStock();
                    int mKetchupS = result.getKetchupStock();
                    int mMayonnaiseS = result.getMayonnaiseStock();
                    int mCurryS = result.getCurryStock();
                    int mOnionS = result.getOnionStock();
                    int mCheeseS = result.getCheeseStock();

                    InventoryItem InvItem = new InventoryItem();
                    InvItem.setHotdogStock(mHotDogPrice + mHotdogS);
                    InvItem.setBbqSauceStock(mBbqSaucePrice + mBbqS);
                    InvItem.setKetchupStock(mKetchupPrice + mKetchupS);
                    InvItem.setMayonnaiseStock(mMayonnaisePrice + mMayonnaiseS);
                    InvItem.setCurryStock(mCurryPrice + mCurryS);
                    InvItem.setOnionStock(mOnionPrice + mOnionS);
                    InvItem.setCheeseStock(mCheesePrice + mCheeseS);
                    InvItem.setId("797f796d-5f19-45d1-852c-4142462f9420");

                    mInventoryTable.update(InvItem, new TableOperationCallback<InventoryItem>() {
                        public static final String TAG = "TAG";

                        @Override
                        public void onCompleted(InventoryItem entity, Exception exception, ServiceFilterResponse response) {

                            if (exception == null){
                                Log.i(TAG, "Azure insert succeeded ID: " + entity);
                            } else {
                                Log.i(TAG, "Azure insert failed again " + exception);
                            }
                        }


                    });

                    refreshStocksFromTable();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(InventoryActivity.this,  "Vorräte wurden aktualisiert",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);
    }




    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {

                    List<HotDogItem> resultHd = mHotDogTable
                            .execute()
                            .get();
                    mHotDogNo = resultHd.size();
                    System.out.println(resultHd.size());


                    List<HotDogItem> resultBs = mHotDogTable
                            .where()
                            .field("bbqsauce").eq(true)
                            .execute()
                            .get();
                    mBbqSauceNo = resultBs.size();

                    List<HotDogItem> resultK = mHotDogTable
                            .where()
                            .field("ketchup").eq(true)
                            .execute()
                            .get();
                    mKetchupNo = resultK.size();

                    List<HotDogItem> resultM = mHotDogTable
                            .where()
                            .field("mayonnaise").eq(true)
                            .execute()
                            .get();
                    mMayonnaiseNo = resultM.size();

                    List<HotDogItem> resultC = mHotDogTable
                            .where()
                            .field("curry").eq(true)
                            .execute()
                            .get();
                    mCurryNo = resultC.size();

                    List<HotDogItem> resultO = mHotDogTable
                            .where()
                            .field("onion").eq(true)
                            .execute()
                            .get();
                    mOnionNo = resultO.size();

                    List<HotDogItem> resultCh = mHotDogTable
                            .where()
                            .field("cheese").eq(true)
                            .execute()
                            .get();
                    mCheeseNo = resultCh.size();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mHotDogCounter.setText(String.valueOf(mHotDogNo));
                            mHotDogCounter.setText(String.valueOf(mHotDogNo));
                            mBbqSauceCounter.setText(String.valueOf(mBbqSauceNo));
                            mKetchupCounter.setText(String.valueOf(mKetchupNo));
                            mMayonnaiseCounter.setText(String.valueOf(mMayonnaiseNo));
                            mCurryCounter.setText(String.valueOf(mCurryNo));
                            mOnionCounter.setText(String.valueOf(mOnionNo));
                            mCheeseCounter.setText(String.valueOf(mCheeseNo));
                        }
                    });
                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);
    }

    private void refreshStocksFromTable() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    List<HotDogItem> resultHd = mHotDogTable
                            .execute()
                            .get();
                    mHotDogNo = resultHd.size();
                    System.out.println(resultHd.size());


                    List<HotDogItem> resultBs = mHotDogTable
                            .where()
                            .field("bbqsauce").eq(true)
                            .execute()
                            .get();
                    mBbqSauceNo = resultBs.size();

                    List<HotDogItem> resultK = mHotDogTable
                            .where()
                            .field("ketchup").eq(true)
                            .execute()
                            .get();
                    mKetchupNo = resultK.size();

                    List<HotDogItem> resultM = mHotDogTable
                            .where()
                            .field("mayonnaise").eq(true)
                            .execute()
                            .get();
                    mMayonnaiseNo = resultM.size();

                    List<HotDogItem> resultC = mHotDogTable
                            .where()
                            .field("curry").eq(true)
                            .execute()
                            .get();
                    mCurryNo = resultC.size();

                    List<HotDogItem> resultO = mHotDogTable
                            .where()
                            .field("onion").eq(true)
                            .execute()
                            .get();
                    mOnionNo = resultO.size();

                    List<HotDogItem> resultCh = mHotDogTable
                            .where()
                            .field("cheese").eq(true)
                            .execute()
                            .get();
                    mCheeseNo = resultCh.size();

                    InventoryItem result = mInventoryTable
                            .lookUp("797f796d-5f19-45d1-852c-4142462f9420")

                            .get();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mHotDogStock.setText(String.valueOf(result.getHotdogStock() - mHotDogNo));
                            mBbqSauceStock.setText(String.valueOf(result.getBbqStock() - mBbqSauceNo));
                            mKetchupStock.setText(String.valueOf(result.getKetchupStock()- mKetchupNo));
                            mMayonnaiseStock.setText(String.valueOf(result.getMayonnaiseStock() - mMayonnaiseNo));
                            mCurryStock.setText(String.valueOf(result.getCurryStock() - mCurryNo));
                            mOnionStock.setText(String.valueOf(result.getOnionStock() - mOnionNo));
                            mCheeseStock.setText(String.valueOf(result.getCheeseStock() + mCheeseNo));
                        }
                    });

                    int mHotdog = result.getHotdogStock() - mHotDogNo;
                    int mBbqSauce = result.getBbqStock() - mBbqSauceNo;
                    int mKetchup = result.getKetchupStock()- mKetchupNo;
                    int mMayonnaise = result.getMayonnaiseStock() - mMayonnaiseNo;
                    int mCurry = result.getCurryStock() - mCurryNo;
                    int mOnion = result.getOnionStock() - mOnionNo;
                    int mCheese = result.getCheeseStock() + mCheeseNo;

                    if (mHotdog<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mBbqSauce<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mKetchup<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mMayonnaise<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mCurry<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mOnion<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mCheese<100 ){
                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("text/plain");

                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lieferbedarf");
                        mailIntent.putExtra(Intent.EXTRA_TEXT   , "Bitte liefern die unten aufgeführten Zutaten nach, bis diese sich wieder auf mindestens 100 Portionen belaufen" + "\n" +"\n" +
                                "Wurst und Brot: " + mHotdog+ "\n" +"\n" +
                                "BBQ-Sauce: " + mBbqSauce + "\n" +"\n" +
                                "Ketchup: " + mKetchup + "\n" + "\n" +
                                "Mayonnaise: " + mMayonnaise + "\n" + "\n" +
                                "Curry: " + mCurry + "\n" + "\n" +
                                "Zwiebeln: " + mOnion + "\n" + "\n" +
                                "Käse: " + mCheese
                        );

                        try {
                            startActivity(Intent.createChooser(mailIntent, "Vorräte kritisch... bitte benachrichtige den Lieferanten"));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InventoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }

        };

        runAsyncTask(task);
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }


}
