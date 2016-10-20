package com.example.andrei.chatapplication.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrei.chatapplication.R;
import com.example.andrei.chatapplication.message.Message;

import java.util.List;

/**
 * Created by andrei on 19.10.2016.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private List<Message> mMessageList;

    public MessagesAdapter(List<Message> msgList) {
        mMessageList = msgList;
    }

    public void swap(List<Message> msgList) {
        msgList.clear();

        mMessageList.addAll(msgList);

        notifyDataSetChanged();
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder holder, int position) {
        Message msg = mMessageList.get(position);

        holder.from.setText(msg.getUserName());
        holder.message.setText(msg.getMessage());
        holder.date.setText(msg.getDate());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView from, message, date;

        public ViewHolder(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.text_view_from_message);
            message = (TextView) view.findViewById(R.id.text_view_message_message);
            date = (TextView) view.findViewById(R.id.text_view_date_message);
        }
    }
}
