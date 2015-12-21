package com.elements.elements.element;

import android.content.Context;
import android.os.AsyncTask;

import com.elements.elements.util.CallBackHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Giancarlo on 20/12/2015.
 * Element
 */
public class Element {
    private String name;
    private String symbol;
    private int color;

    public String getName() {
        return this.name;
    }
    public String getSymbol() {
        return this.symbol;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void loadWikipediaContent() {

    }

    /* API Request: Yahoo (official) rate               --- */
    public static class FindYahooRate                   extends AsyncTask<Void, Void, String> {

        private final Context mContext;
        private final String mFromCurrency;
        private final String mToCurrency;
        private final Object mInstance;
        private final CallBackHandler mHandler;
        private final List<String> mRates;

        public FindYahooRate(Context a, String fromCurrency, String toCurrency, List<String> rates, Object instance, CallBackHandler handler) {
            mContext = a;
            mFromCurrency = fromCurrency;
            mToCurrency = toCurrency;
            mInstance = instance;
            mHandler = handler;
            mRates = rates;
        }

        @Override
        protected String doInBackground(Void... params) {
            return sendExternalGet(
                    "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22" +
                            mFromCurrency +
                            mToCurrency +
                            "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
            );
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String response) {
            try {
            } catch(Exception e) {
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    // Send get to the API
    private static String sendExternalGet(String http) {
        try {
            // Setup
            String response = new String();
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);
            DefaultHttpClient client = new DefaultHttpClient(httpParams);
            HttpGet httpGet = new HttpGet(http);

            // Send
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            // Parse response
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

            return response;
        } catch (Exception e) {
            return "";
        }
    }
}
