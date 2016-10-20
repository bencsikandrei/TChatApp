package com.example.andrei.chatapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import com.example.andrei.chatapplication.helper.MessagesAdapter;
import com.example.andrei.chatapplication.message.Message;
import com.example.andrei.chatapplication.network.NetworkHelper;
import com.example.andrei.chatapplication.parser.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrei on 18.10.2016.
 */

public class ChatFragment extends Fragment {
    String mToken;
    private String mStringMessages;
    private List<Message> mMsgList = new ArrayList<>();
    private EditText mEditMyMessage;
    private TextView mTextViewMessages;

    private ImageButton mButtonSendMsg;
    private ImageButton mRefreshButton;

    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;

    private MessagesAdapter mMessagesAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    private OnSendButtonClick mOnSendButtonHandler;
    private OnRefreshButtonClick mOnRefreshButtonHandler;

    public static Fragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mToken = args.getString(LoginActivity.EXTRA_TOKEN);
        mStringMessages = updateMessages(mToken);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mStringMessages = updateMessages(mToken);

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
                mStringMessages = updateMessages(mToken);
            }
        });
        /* setup the recycler */
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_messages);
        /* set layout */
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        /* set adapter */
        mMessagesAdapter = new MessagesAdapter(mMsgList);

        mRecyclerView.setAdapter(mMessagesAdapter);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mOnSendButtonHandler = (OnSendButtonClick) context;
            mOnRefreshButtonHandler = (OnRefreshButtonClick) context;

        } catch (ClassCastException cex) {
            Log.e("Andrei", "onAttach: Class must implement the required interfaces ..");
        }
    }

    private void getMessageList() {
        Log.d("Andrei: getMessageList", "getMessageList: Called " + mStringMessages);
        try {

            mMsgList.clear();
            mMsgList.addAll(JsonParser.getMessages(mStringMessages));
            /*mMessagesAdapter.swap(mMsgList);
            mRecyclerView.swapAdapter(mMessagesAdapter, true);*/

        } catch (Exception ex) {
            Log.e("Andrei: getMessageList", "getMessageList: parsing went wrong ..");
        }
    }

    private String updateMessages(String token) {

        try {
            mStringMessages = new AsyncTaskMessages(getActivity()).execute(token).get();
            Log.d("Andrei:UpdateMessage", "UpdateMessage:" + mStringMessages);
        } catch (Exception ex) {
            Log.e("Andrei:UpdateMessage", "UpdateMessage: error fetching the messages..");
        }
        return mStringMessages;

    }

    public interface OnSendButtonClick {
        String onSendButtonClick(String... params);
    }

    public interface OnRefreshButtonClick {
        String onRefreshButtonClick();
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

            getMessageList();


            mLinearLayoutManager.scrollToPosition(mMsgList.size() - 1);

            mMessagesAdapter.notifyDataSetChanged();

            if (mSwipeContainer.isRefreshing()) {
                mSwipeContainer.setRefreshing(false);
            }
        }

    }
}
