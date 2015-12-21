package com.elements.elements;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elements.elements.element.Element;
import com.elements.elements.util.CallBackHandler;
import com.elements.elements.util.ElementDatabase;
import com.elements.elements.util.WikiAPI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class ElementDetail extends ActionBarActivity {

    LinearLayout progressSection;
    LinearLayout errorSection;
    TextView errorMessage;
    TextView content;
    FindElementInfo apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_detail);

        this.progressSection = (LinearLayout) findViewById(R.id.progress_section);
        this.content = (TextView) findViewById(R.id.content);
        this.errorSection = (LinearLayout) findViewById(R.id.error_section);
        this.errorMessage = (TextView) findViewById(R.id.error_message);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            String element_symbol = b.getString("element_symbol");

            TextView elementSymbol = (TextView) findViewById(R.id.element_symbol);
            elementSymbol.setText(element_symbol);
            List<Element> elements = ElementDatabase.getAll(this);
            for(Element e : elements) {
                if(e.getSymbol().equals(element_symbol)) {
                    TextView t = (TextView) findViewById(R.id.element_name);
                    t.setText(e.getName());
                    apiCall = new FindElementInfo(e.getName());
                    apiCall.execute();
                    return;
                }
            }

            showError(true, getString(R.string.element_info_unavailable));

        } else {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_element_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgress(boolean show) {
        if(progressSection != null) {
            if (show) {
                progressSection.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
            } else {
                progressSection.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showError(boolean show, String error) {
        if(progressSection != null) {
            if (show) {
                showProgress(false);
                errorSection.setVisibility(View.VISIBLE);
                errorMessage.setText(error);
            } else {
                errorSection.setVisibility(View.GONE);
            }
        }
    }

    /* API Request: Yahoo (official) rate               --- */
    private class FindElementInfo extends AsyncTask<Void, Void, String> {
        private final String mElementName;

        public FindElementInfo(String elementName) {
            mElementName = elementName;
        }

        @Override
        protected String doInBackground(Void... params) {
            return sendExternalGet("https://en.wikipedia.org/w/api.php?action=query&titles=" + mElementName + "&prop=revisions&rvprop=content&rvsection=0&format=json");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject o = new JSONObject(response);
                String pageId = (String) o.getJSONObject("query").getJSONObject("pages").keys().next();
                ElementDetail.this.content.setText(Html.fromHtml(WikiAPI.parseOverview(o.getJSONObject("query").getJSONObject("pages").getJSONObject(pageId).getJSONArray("revisions").getJSONObject(0).getString("*"))));
                showProgress(false);
            } catch(Exception e) {
                showError(true, getString(R.string.element_info_unavailable));
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
