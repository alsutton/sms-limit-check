package com.funkyandroid.smslimitcheck;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.provider.Settings;

public class MainActivity extends Activity {
    public static final String SMS_OUTGOING_CHECK_INTERVAL_MS = "sms_outgoing_check_interval_ms";
    public static final String SMS_OUTGOING_CHECK_MAX_COUNT = "sms_outgoing_check_max_count";

    /** Default checking period for SMS sent without user permission. */
    private static final int DEFAULT_SMS_CHECK_PERIOD = 1800000;    // 30 minutes

    /** Default number of SMS sent in checking period without user permission. */
    private static final int DEFAULT_SMS_MAX_COUNT = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	((TextView)findViewById(R.id.limitText)).setText(R.string.too_early);
        	return;
        }
        
        
        ContentResolver resolver = getContentResolver();

        int maxAllowed = Settings.Secure.getInt(resolver, SMS_OUTGOING_CHECK_MAX_COUNT, -1);
        int checkPeriod = Settings.Secure.getInt(resolver, SMS_OUTGOING_CHECK_INTERVAL_MS, -1);

        TextView guessView = (TextView) findViewById(R.id.guess);
        if(maxAllowed == -1 && checkPeriod == -1) {
        	guessView.setVisibility(View.VISIBLE);
        	guessView.setText(R.string.guess_time_and_rate);
        	maxAllowed = DEFAULT_SMS_MAX_COUNT;
        	checkPeriod = DEFAULT_SMS_CHECK_PERIOD;
        } else if (maxAllowed == -1) {
        	guessView.setVisibility(View.VISIBLE);
        	guessView.setText(R.string.guess_rate);
        	maxAllowed = DEFAULT_SMS_MAX_COUNT;
        } else if (checkPeriod == -1) {
        	guessView.setVisibility(View.VISIBLE);
        	guessView.setText(R.string.guess_time);
        	checkPeriod = DEFAULT_SMS_CHECK_PERIOD;
        }
        
        checkPeriod /= 1000 * 60;
        
        String text = getText(R.string.limit).toString();
        text = text.replace("%sms", Integer.toString(maxAllowed));
        text = text.replace("%min", Integer.toString(checkPeriod));
        
        ((TextView)findViewById(R.id.limitText)).setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
