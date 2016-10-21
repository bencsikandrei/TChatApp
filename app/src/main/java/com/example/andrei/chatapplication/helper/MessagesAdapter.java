package com.example.andrei.chatapplication.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrei.chatapplication.R;
import com.example.andrei.chatapplication.account.AccountLab;
import com.example.andrei.chatapplication.message.Message;

import java.util.List;

/**
 * @author Andrei
 * Message adapter for the RecyclerView - used on messages in the chat
 */


public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SEND = 0;
    private static final int VIEW_TYPE_RECEIVE = 1;
    private List<Message> mMessageList;

    public MessagesAdapter(List<Message> msgList) {
        mMessageList = msgList;
    }

    /* change the adapter - whe refreshing */
    public void swap(List<Message> msgList) {
        msgList.clear();

        mMessageList.addAll(msgList);

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECEIVE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_receive_list_row, parent, false);

            return new ViewHolderReceive(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_send_list_row, parent, false);

            return new ViewHolderSend(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message msg = mMessageList.get(position);
        if (getItemViewType(position) == VIEW_TYPE_RECEIVE) {

            ((MessagesAdapter.ViewHolderReceive) holder).from.setText(msg.getUserName());
            ((MessagesAdapter.ViewHolderReceive) holder).message.setText(msg.getMessage());
            ((MessagesAdapter.ViewHolderReceive) holder).date.setText(msg.getDate());
        } else {
            ((MessagesAdapter.ViewHolderSend) holder).message.setText(msg.getMessage());
            ((MessagesAdapter.ViewHolderSend) holder).date.setText(msg.getDate());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessageList.get(position).getUserName().equals(AccountLab.getInstance().getAccount().getLogin())) {
            return VIEW_TYPE_SEND;
        } else {
            return VIEW_TYPE_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolderReceive extends RecyclerView.ViewHolder {
        /* the text views for the messages */
        public TextView from;
        public TextView message;
        public TextView date;

        public ViewHolderReceive(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.receive_text_view_from_message);
            message = (TextView) view.findViewById(R.id.receive_text_view_message_message);
            date = (TextView) view.findViewById(R.id.receive_text_view_date_message);
        }
    }

    public class ViewHolderSend extends RecyclerView.ViewHolder {
        /* the text views for the messages */
        //public TextView from;
        public TextView message;
        public TextView date;

        public ViewHolderSend(View view) {
            super(view);
            //from = (TextView) view.findViewById(R.id.text_view_from_message);
            message = (TextView) view.findViewById(R.id.send_text_view_message_message);
            date = (TextView) view.findViewById(R.id.send_text_view_date_message);
        }
    }

    /* TODO add a second viewholder class for the messages that are sent by me */

}
