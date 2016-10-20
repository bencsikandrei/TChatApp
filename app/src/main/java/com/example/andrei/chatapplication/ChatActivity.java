package com.example.andrei.chatapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;

import com.example.andrei.chatapplication.helper.SingleFragmentActivity;
import com.example.andrei.chatapplication.network.NetworkHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends SingleFragmentActivity
        implements ChatFragment.OnSendButtonClick, ChatFragment.OnRefreshButtonClick {

    public static final String EXTRA_MESSAGES = "eu.tb.afbencsi.messages";
    private final Handler mHandler = new Handler();
    private String mToken;
    private String mMessages;
    private EditText mMessageSend;
    private Timer mTimer;
    private TimerTask mTimerTask;

    // private DisplayProgress mDisplayProgress = new DisplayProgress(this);
    /*
        start the timer and make it reload every 10 seconds
     */
    private void startTimer() {
        // initialize timer
        mTimer = new Timer();
        // initialize timer task TODO
        initializeTimerTask();
        // schedule
        mTimer.schedule(mTimerTask, 0, 10000);
    }

    /*
        stop the timer
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /*
        make it an anonymous class
     */
    private void initializeTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateMessages();
                    }
                });
            }
        };
    }

    private void updateMessages() {

        try {
            mMessages = new AsyncTaskMessages(this).execute(mToken).get();
            Log.d("Andrei:UpdateMessage", "UpdateMessage:" + mMessages);
        } catch (Exception ex) {
            Log.e("Andrei:UpdateMessage", "UpdateMessage: error fetching the messages..");
        }

    }

    private void updateFragment() {
        FragmentManager fm = getSupportFragmentManager();

        Fragment nf = ChatFragment.newInstance();
        Bundle args = new Bundle();

        args.putString(LoginActivity.EXTRA_TOKEN, mToken);
        args.putString(EXTRA_MESSAGES, mMessages);

        nf.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .remove(fm.findFragmentById(R.id.fragment_container))
                .add(R.id.fragment_container, nf)
                .commit();
    }

    @Override
    public Fragment createFragment() {

        mToken = getIntent().getStringExtra(LoginActivity.EXTRA_TOKEN);
        mMessages = getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGES);

        ChatFragment cf = (ChatFragment) ChatFragment.newInstance();

        Bundle args = new Bundle();

        updateMessages();

        args.putString(LoginActivity.EXTRA_TOKEN, mToken);
        args.putString(EXTRA_MESSAGES, mMessages);

        cf.setArguments(args);

        return cf;
    }

    @Override
    public String onSendButtonClick(String... params) {

        //mDisplayProgress.displayProgressDialog();
        new AsyncTaskSend(this).execute(params[0], mToken);
        updateMessages();
        return mMessages;

    }

    @Override
    public String onRefreshButtonClick() {
        //mDisplayProgress.displayProgressDialog();
        updateMessages();
        return mMessages;
    }

    private class AsyncTaskMessages extends AsyncTask<String, Void, String> {

        Context context;

        public AsyncTaskMessages(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return "Internet is not available!";
            }

            return NetworkHelper.getMessages(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }

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

            return NetworkHelper.sendMessage(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
