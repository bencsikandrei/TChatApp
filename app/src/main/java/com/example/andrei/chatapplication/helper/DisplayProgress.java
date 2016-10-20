package com.example.andrei.chatapplication.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * Created by andrei on 19.10.2016.
 */

public class DisplayProgress {
    Context mContext;
    ProgressDialog progressDialog;

    public DisplayProgress(Context context) {
        mContext = context;
    }

    /**
     * display a Progress Dialog.
     */
    public void displayProgressDialog() {
        progressDialog = new ProgressDialog(mContext);

        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Login in progress ...");
        progressDialog.show();
    }

    /**
     * Method to close progress dialog.
     */
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            Log.w("HelloWorld", "trying to close Progress dialog that is not exist or opened");
        }
    }

}
