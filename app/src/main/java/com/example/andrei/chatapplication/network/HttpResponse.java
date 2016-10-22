package com.example.andrei.chatapplication.network;

/**
 * Created by andrei on 21.10.2016.
 */

public class HttpResponse {
    public int code;
    public String json;

    public HttpResponse(int code, String s) {
        this.code = code;
        this.json = s;
    }
}
