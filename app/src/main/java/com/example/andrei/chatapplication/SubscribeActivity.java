package com.example.andrei.chatapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.andrei.chatapplication.helper.DisplayProgress;
import com.example.andrei.chatapplication.helper.LoggingHelper;
import com.example.andrei.chatapplication.helper.SingleFragmentActivity;
import com.example.andrei.chatapplication.network.NetworkHelper;


/**
 * @author Andrei
 * Make the class for the Subscribe
 *
 * Uses an AsyncTask to do the POST for subscribe
 *
 * Uses a Fragment to display the whole view
 *
 *
 *
 *
 */
public class SubscribeActivity extends SingleFragmentActivity
        implements SubscribeFragment.OnCreateAccListener {


    private CreateAccountAsyncTask mCreateAccountAsyncTask;
    private DisplayProgress mDisplayProgress = new DisplayProgress(this);

    @Override
    public Fragment createFragment() {
        Bundle args = new Bundle();

        return SubscribeFragment.newInstance(args);
    }


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
    public void onCreateAccClick(String... params) {
        // take the data and make the call
        mDisplayProgress.displayProgressDialog("Creating account ..");
        mCreateAccountAsyncTask = new CreateAccountAsyncTask(this);
        mCreateAccountAsyncTask.execute(params);

    }

    private class CreateAccountAsyncTask extends AsyncTask<String, Void, String> {
        Context context;

        public CreateAccountAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return "Internet is not available!";
            }
            return NetworkHelper.signup(params[0], params[1], params[2]).json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDisplayProgress.hideProgressDialog();
        }
    }
}
