package com.example.sebas.hotdogadmin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

public class PriceActivity extends Activity {

    private EditText mHotDogEditText;
    private EditText mBbqSauceEditText;
    private EditText mKetchupEditText;
    private EditText mMayonnaiseEditText;
    private EditText mCurryEditText;
    private EditText mOnionEditText;
    private EditText mCheeseEditText;

    private TextView mHotDogPrice;
    private TextView mBbqSaucePrice;
    private TextView mKetchupPrice;
    private TextView mMayonnaisePrice;
    private TextView mCurryPrice;
    private TextView mOnionPrice;
    private TextView mCheesePrice;

    private Button mUpdate;
    private Button mUpdatePricesButton;
    private boolean mPricesHaveChanged = false;
    private MobileServiceClient mClient;
    private MobileServiceTable<PriceItem> mPriceTable;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://hotdog2.azurewebsites.net",
                    this).withFilter(new ProgressFilter());

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

            mPriceTable = mClient.getTable(PriceItem.class);


        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }


        mHotDogEditText = (EditText) findViewById(R.id.hotdog_price);
        mBbqSauceEditText = (EditText) findViewById(R.id.bbq_sauce_price);
        mKetchupEditText = (EditText) findViewById(R.id.ketchup_price);
        mMayonnaiseEditText = (EditText) findViewById(R.id.mayonnaise_price);
        mCurryEditText = (EditText) findViewById(R.id.curry_price);
        mOnionEditText = (EditText) findViewById(R.id.onion_price);
        mCheeseEditText = (EditText) findViewById(R.id.cheese_price);
        mUpdatePricesButton = (Button) findViewById(R.id.order);

        mHotDogPrice = (TextView) findViewById(R.id.current_hotdog_price);
        mBbqSaucePrice = (TextView) findViewById(R.id.current_bbq_price);
        mKetchupPrice = (TextView) findViewById(R.id.current_ketchup_price);
        mMayonnaisePrice = (TextView) findViewById(R.id.current_mayonnaise_price);
        mCurryPrice = (TextView) findViewById(R.id.current_curry_price);
        mOnionPrice = (TextView) findViewById(R.id.current_onion_price);
        mCheesePrice = (TextView) findViewById(R.id.current_cheese_price);

        refreshItemsFromTable();

        mUpdatePricesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                double mHotDogPrice = Double.parseDouble(mHotDogEditText.getText().toString());
                double mBbqSaucePrice = Double.parseDouble(mBbqSauceEditText.getText().toString());
                double mKetchupPrice = Double.parseDouble(mKetchupEditText.getText().toString());
                double mMayonnaisePrice = Double.parseDouble(mMayonnaiseEditText.getText().toString());
                double mCurryPrice = Double.parseDouble(mCurryEditText.getText().toString());
                double mOnionPrice = Double.parseDouble(mOnionEditText.getText().toString());
                double mCheesePrice = Double.parseDouble(mCheeseEditText.getText().toString());

                PriceItem priceItem = new PriceItem();
                priceItem.setHotdog(mHotDogPrice);
                priceItem.setBbqSauce(mBbqSaucePrice);
                priceItem.setKetchup(mKetchupPrice);
                priceItem.setMayonnaise(mMayonnaisePrice);
                priceItem.setCurry(mCurryPrice);
                priceItem.setOnion(mOnionPrice);
                priceItem.setCheese(mCheesePrice);
                priceItem.setId("7b9845b1-c0c2-4a97-878b-d7178af5ea6d");

                mPriceTable.update(priceItem, new TableOperationCallback<PriceItem>() {
                    public static final String TAG = "TAG";

                    @Override
                    public void onCompleted(PriceItem entity, Exception exception, ServiceFilterResponse response) {
                        if (exception == null){
                            Log.i(TAG, "Azure insert succeeded ID: " + entity);
                        } else {
                            Log.i(TAG, "Azure insert failed again " + exception);
                        }
                    }
                });

                refreshItemsFromTable();


                Toast.makeText(PriceActivity.this,  "Preise wurden aktualisiert",
                        Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {

                    PriceItem result = mPriceTable
                            .lookUp("7b9845b1-c0c2-4a97-878b-d7178af5ea6d")

                            .get();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mHotDogPrice.setHint(String.format("%.2f",result.getHotDog()) + " €");
                            mBbqSaucePrice.setHint(String.format("%.2f",result.getBbqSauce())+ " €");
                            mKetchupPrice.setHint(String.format("%.2f",result.getKetchup())+ " €");
                            mMayonnaisePrice.setHint(String.format("%.2f",result.getMayonnaise())+ " €");
                            mCurryPrice.setHint(String.format("%.2f",result.getCurry())+ " €");
                            mOnionPrice.setHint(String.format("%.2f",result.getOnion())+ " €");
                            mCheesePrice.setHint(String.format("%.2f",result.getCheese())+ " €");
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


