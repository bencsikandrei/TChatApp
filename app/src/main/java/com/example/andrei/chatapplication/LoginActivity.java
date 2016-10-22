package com.example.andrei.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.andrei.chatapplication.account.AccountLab;
import com.example.andrei.chatapplication.helper.DisplayProgress;
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
 *
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
        Bundle args = new Bundle();

        return LoginFragment.newInstance();

    }

    @Override
    public void onLoginClick(String... params) {
        // take the data and make the call
        //mDisplayProgress.displayProgressDialog();

        mAsyncTaskLogin = new AsyncTaskLogin(LoginActivity.this, params);

        try {
            mAsyncTaskLogin.execute(params);


        } catch (Exception ex) {

            Toast.makeText(this, "Task was interrupted!", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onNewClick() {
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
        protected HttpResponse doInBackground(String... params) {

            if (!NetworkHelper.isInternetAvailable(context)) {
                return new HttpResponse(404, "");
            }
            HttpResponse http;
            http = NetworkHelper.signin(params[0], params[1]);

            if (http != null && http.code == HttpURLConnection.HTTP_OK) {

                try {

                    mToken = JsonParser.getToken(http.json);

                    Log.i("Andrei", "onLoginClick: " + mToken);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("Andrei", "Do in background ->  " + http.json);
            }
            return http;

        }

        @Override
        protected void onPostExecute(final HttpResponse resp) {
            super.onPostExecute(resp);

            if (resp != null && resp.code == HttpURLConnection.HTTP_OK) {
                //mDisplayProgress.hideProgressDialog();
                AccountLab.getInstance().createAccount(uname, passwd);
            } else {
                Toast.makeText(LoginActivity.this, "Please check credentials and try again!", Toast.LENGTH_SHORT).show();
            }
            if (resp != null && resp.code == HttpURLConnection.HTTP_OK) {
                Intent i = new Intent(LoginActivity.this, ChatActivity.class);

                i.putExtra(EXTRA_TOKEN, mToken);

                startActivity(i);
            }

        }

    }
}
