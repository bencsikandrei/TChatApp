package com.example.andrei.chatapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.andrei.chatapplication.helper.MessagesAdapter;
import com.example.andrei.chatapplication.message.Message;
import com.example.andrei.chatapplication.network.NetworkHelper;
import com.example.andrei.chatapplication.parser.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.andrei.chatapplication.LoginActivity.EXTRA_TOKEN;

/**
 * @author Andrei
 *
 * Fragment for the VIEW of Chat Activity
 *
 * Has an AsyncTask for updating messages
 *
 * Uses one callback to send a message
 *
 *
 */

public class ChatFragment extends Fragment {
    private final Handler mHandler = new Handler();
    String mToken;
    private String mStringMessages;
    private List<Message> mMsgList = new ArrayList<>();
    private EditText mEditMyMessage;
    private ImageButton mButtonSendMsg;
    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;
    /* a timer for refresh -> TODO */
    private Timer mTimer;
    private TimerTask mTimerTask;
    //private MessagesAdapter mMessagesAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private OnSendButtonClick mOnSendButtonHandler;


    public static Fragment newInstance(String token) {

        ChatFragment cf = new ChatFragment();

        Bundle args = new Bundle();

        args.putString(EXTRA_TOKEN, token);

        cf.setArguments(args);

        return cf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();

        mToken = args.getString(EXTRA_TOKEN);

        updateMessages(mToken);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        /* swipe refresh */
        mSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateMessages(mToken);

            }
        });

        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mEditMyMessage = (EditText) v.findViewById(R.id.edit_text_send_message);

        mButtonSendMsg = (ImageButton) v.findViewById(R.id.image_button_send);

        mButtonSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringMessages = mOnSendButtonHandler.onSendButtonClick(mEditMyMessage.getText().toString());
                mEditMyMessage.setText("");
                updateMessages(mToken);
            }
        });
        /* setup the recycler */
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_messages);
        /* set layout */
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setAdapter(new MessagesAdapter(mMsgList));

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            mOnSendButtonHandler = (OnSendButtonClick) context;
            //mOnRefreshButtonHandler = (OnRefreshButtonClick) context;

        } catch (ClassCastException cex) {
            Log.e("Andrei", "onAttach: Class must implement the required interfaces ..");
        }
    }

    /**
     * refresh message list !
     */
    private void getMessageList() {
        Log.d("Andrei: getMessageList", "getMessageList: Called " + mStringMessages);
        try {

            mMsgList.clear();

            mMsgList.addAll(JsonParser.getMessages(mStringMessages));

        } catch (Exception ex) {
            Log.e("Andrei: getMessageList", "getMessageList: parsing went wrong ..");
        }
    }

    /* call the AsyncTask to fetch messages from server */
    private void updateMessages(String token) {

        try {
            new AsyncTaskMessages(getActivity()).execute(token);

            Log.d("Andrei:UpdateMessage", "UpdateMessage:" + mStringMessages);

        } catch (Exception ex) {
            Log.e("Andrei:UpdateMessage", "UpdateMessage: error fetching the messages..");
        }
    }

    /*
    start the timer and make it reload every 10 seconds
 */
    private void startTimer() {
        // initialize timer
        mTimer = new Timer();
        // initialize timer task
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
                        updateMessages(mToken);
                    }
                });
            }
        };
    }

    /* necessary callbacks */
    public interface OnSendButtonClick {
        String onSendButtonClick(String... params);
    }

    /**
     * replaces the callback
     */
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

            return NetworkHelper.getMessages(params[0]).json;

        }

        /* update the views */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mStringMessages = s;

            getMessageList();

            mLinearLayoutManager.scrollToPosition(mMsgList.size() - 1);

            mRecyclerView.getAdapter().notifyDataSetChanged();

            if (mSwipeContainer.isRefreshing()) {
                mSwipeContainer.setRefreshing(false);
            }
        }

    }
}
