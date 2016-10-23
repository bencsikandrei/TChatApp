package com.example.andrei.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.andrei.chatapplication.helper.LoggingHelper;
import com.example.andrei.chatapplication.helper.SingleFragmentActivity;
import com.example.andrei.chatapplication.network.NetworkHelper;

/**
 * @author Andrei
 *         Make the class for the actual chat
 *         <p>
 *         Uses a RecyclerView for showing messages (for now using the same ViewHolderReceive for all messages)
 *         <p>
 *         Uses a Fragment to displa the whole chat
 */
public class ChatActivity extends SingleFragmentActivity
        implements ChatFragment.OnSendButtonClick {

    public static final String EXTRA_MESSAGES = "eu.tb.afbencsi.messages";

    /* token received from the Login Activity */
    private String mToken;
    /* messages received as a HTTP response */
    private String mMessages;

    // private DisplayProgress mDisplayProgress = new DisplayProgress(this);
    public static Intent newIntent(Context packageContext, String token) {
        Intent i = new Intent(packageContext, ChatActivity.class);
        i.putExtra(LoginActivity.EXTRA_TOKEN, token);
        return i;
    }
    /**
     * The method used to create the fragment and pass it necessary args
     */
    @Override
    public Fragment createFragment() {

        mToken = getIntent().getStringExtra(LoginActivity.EXTRA_TOKEN);

        ChatFragment cf = (ChatFragment) ChatFragment.newInstance(mToken);

        return cf;
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
    public String onSendButtonClick(String... params) {

        //mDisplayProgress.displayProgressDialog();
        new AsyncTaskSend(this).execute(params[0], mToken);

        return mMessages;

    }


    /**
     * Used to send the messages !
     * Called in the callback
     */
    private class AsyncTaskSend extends AsyncTask<String, Void, String> {

        Context context;

        public AsyncTaskSend(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            if (!NetworkHelper.isInternetAvailable(context)) {
                return "Internet is not available!";
            }

            return NetworkHelper.sendMessage(params[0], params[1]).json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
