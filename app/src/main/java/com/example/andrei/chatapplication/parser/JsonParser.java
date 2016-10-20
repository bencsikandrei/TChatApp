package com.example.andrei.chatapplication.parser;

import com.example.andrei.chatapplication.message.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sca on 03/06/15.
 */
public class JsonParser {

    public static List<Message> getMessages(String json) throws JSONException {
        List<Message> messages = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        Message msg;
        for (int i = 0; i < array.length(); i++) {
            obj = array.getJSONObject(i);
            msg = new Message(obj.optString("username"), obj.optString("message"), obj.optLong("date"));
            messages.add(msg);
        }

        return messages;
    }

    public static String getToken(String response) throws JSONException {
        return new JSONObject(response).optString("token");
    }
}
