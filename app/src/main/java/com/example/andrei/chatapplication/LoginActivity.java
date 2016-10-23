package com.example.andrei.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.andrei.chatapplication.account.AccountLab;
import com.example.andrei.chatapplication.helper.DisplayProgress;
import com.example.andrei.chatapplication.helper.LoggingHelper;
import com.example.andrei.chatapplication.helper.SingleFragmentActivity;
import com.example.andrei.chatapplication.network.HttpResponse;
import com.example.andrei.chatapplication.network.NetworkHelper;
import com.example.andrei.chatapplication.parser.JsonParser;

import org.json.JSONException;

import java.net.HttpURLConnection;

/**
 * @author Andrei
 * Make the class for the Login
 *
 * Uses an AsyncTask to do the POST for login
 *
 * Uses a Fragment to displa the whole view
 *
 *  Launcher for the app
 *
 *
 */
public class LoginActivity extends SingleFragmentActivity
        implements LoginFragment.OnLoginClickListener, LoginFragment.OnNewAccClickListener {

    public static final String EXTRA_TOKEN = "eu.tb.afbencsi.token";
    public static final String EXTRA_MESSAGES = "eu.tb.afbencsi.msg";

    private DisplayProgress mDisplayProgress = new DisplayProgress(this);

    private AsyncTaskLogin mAsyncTaskLogin;

    private String mToken;

    @Override
    public Fragment createFragment() {
        LoggingHelper.logDebug(this.getClass().getName(), "createFragment");

        return LoginFragment.newInstance();

    }

    /**
     * lifecycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggingHelper.logDebug(this.getClass().getName(), "onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoggingHelper.logDebug(this.getClass().getName(), "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoggingHelper.logDebug(this.getClass().getName(), "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoggingHelper.logDebug(this.getClass().getName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoggingHelper.logDebug(this.getClass().getName(), "onDestroy");
    }

    @Override
    public void onLoginClick(String... params) {

        LoggingHelper.logDebug(this.getClass().getName(), "onLoginClick");
        mAsyncTaskLogin = new AsyncTaskLogin(LoginActivity.this, params);

        try {

            mAsyncTaskLogin.execute(params);

        } catch (Exception ex) {

            Toast.makeText(this, "Task was interrupted!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNewClick() {

        LoggingHelper.logDebug(this.getClass().getName(), "onNewClick");

        Intent i = new Intent(this, SubscribeActivity.class);

        startActivity(i);
    }

    private class AsyncTaskLogin extends AsyncTask<String, Void, HttpResponse> {

        Activity context;
        String uname, passwd;

        public AsyncTaskLogin(Activity context, String... params) {
            this.context = context;
            this.uname = params[0];
            this.passwd = params[1];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take the data and make the call
            mDisplayProgress.displayProgressDialog("Logging in...\n");
        }

        @Override
        protected HttpResponse doInBackground(String... params) {

            if (!NetworkHelper.isInternetAvailable(context)) {
                return new HttpResponse(404, "");
            }
            HttpResponse http;
            http = NetworkHelper.signin(params[0], params[1]);

            if (http != null && http.code == HttpURLConnection.HTTP_OK) {

                try {

                    mToken = JsonParser.getToken(http.json);

                    LoggingHelper.logInfo(LoginActivity.this.getClass().getName(), "doInBackground");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LoggingHelper.logInfo(LoginActivity.this.getClass().getName(), "doInBackground");
            }
            return http;

        }

        @Override
        protected void onPostExecute(final HttpResponse resp) {
            super.onPostExecute(resp);

            mDisplayProgress.hideProgressDialog();

            if (resp != null && resp.code == HttpURLConnection.HTTP_OK) {

                AccountLab.getInstance().createAccount(uname, passwd);

                LoggingHelper.logInfo(LoginActivity.this.getClass().getName(), "onPostExecute -> token " + mToken);

                Intent i = ChatActivity.newIntent(LoginActivity.this, mToken);

                startActivity(i);

            } else {
                Toast.makeText(LoginActivity.this, "Please check credentials and try again!", Toast.LENGTH_SHORT).show();
            }


        }

    }
}
