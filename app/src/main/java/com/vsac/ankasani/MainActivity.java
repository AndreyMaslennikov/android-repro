package com.vsac.ankasani;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.appcenter.auth.SignInResult;
import com.microsoft.appcenter.push.PushListener;
import com.microsoft.appcenter.utils.async.AppCenterConsumer;
import com.microsoft.identity.client.Logger;
import com.onesignal.OneSignal;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.push.Push;
import com.microsoft.appcenter.auth.Auth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Push.setListener((PushListener) new MyPushListener());
        AppCenter.setLogLevel(Log.VERBOSE);
        AppCenter.start(getApplication(), "4a239618-f969-486f-b6d1-c7850d652d74",
                Analytics.class, Crashes.class, Push.class, Auth.class);
        Analytics.trackEvent("VSAC2-OneSignal");

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button signIn=findViewById(R.id.button3);
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                Auth.signIn().thenAccept(new AppCenterConsumer<SignInResult>() {

                    @Override
                    public void accept(SignInResult signInResult) {

                        if (signInResult.getException() == null) {
                            Gson gson=new GsonBuilder().serializeNulls().create();
                            String jsonOutput=gson.toJson(signInResult);
                            Log.v("ANKASANI",jsonOutput);
                            TextView t=findViewById(R.id.textView);
                            t.setText(jsonOutput);
                            // Sign-in succeeded.
                            String accountId = signInResult.getUserInformation().getAccountId();
                            Log.v("ANKASANI",accountId);
                           // TextView t=findViewById(R.id.textView3);
                           // t.setText(accountId);
                           // String idToken = signInResult.getUserInformation().getIdToken();
                           // TextView t1=findViewById(R.id.textView5);
                           // t.setText(idToken);
                           // String accessToken = signInResult.getUserInformation().getAccessToken();
                            //TextView t3=findViewById(R.id.textView7);
                           // t.setText(accessToken);
                        } else {

                            // Do something with sign in failure.
                            Exception signInFailureException = signInResult.getException();
                        }
                    }
                });
            }
        });

        Button signOut=findViewById(R.id.button4);
        signOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                Auth.signOut();
                TextView t=findViewById(R.id.textView);
                t.setText("Signed out");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
