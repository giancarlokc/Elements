package com.elements.elements;

import com.elements.elements.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }



    public void elementClick(View v) {
        try {
            LinearLayout l = (LinearLayout) v;
            String symbol = ((TextView)((LinearLayout)l.getChildAt(0)).getChildAt(0)).getText().toString();
            openDetail(symbol);
        } catch(Exception e) {
            Log.i("DETAIL","Element click error");
        }
    }
    private void openDetail(String elementSymbol) {
        Intent intent = new Intent(this, ElementDetail.class);
        Bundle b = new Bundle();
        b.putString("element_symbol", elementSymbol);
        intent.putExtras(b);
        startActivity(intent);
    }
}
