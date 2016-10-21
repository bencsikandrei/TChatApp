package com.example.andrei.chatapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
public class LoginFragment extends Fragment {

    private OnLoginClickListener mLoginHandler;
    private OnNewAccClickListener mNewAccHandler;


    private Button mLoginButton;
    private Button mNewAccButton;

    private EditText mLoginEdit;
    private EditText mPasswdEdit;

    public static Fragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginEdit = (EditText) v.findViewById(R.id.subscribe_edit_text_login_uname);
        mPasswdEdit = (EditText) v.findViewById(R.id.subscribe_edit_text_passwd_again);
        mLoginButton = (Button) v.findViewById(R.id.subscribe_button_create);
        /* enable the button when sufficient input is given */
        mLoginEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mLoginEdit.getText().length() < 1 || mPasswdEdit.getText().toString().length() < 0) {
                    mLoginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0 && mPasswdEdit.getText().toString().length() > 0) {
                    mLoginButton.setEnabled(true);
                }
            }
        });
        /* same for passwd */
        mPasswdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mLoginEdit.getText().length() < 1 || mPasswdEdit.getText().toString().length() < 1) {
                    mLoginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0 && mLoginEdit.getText().toString().length() > 0) {
                    mLoginButton.setEnabled(true);
                }
            }
        });
        mPasswdEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String uname = mLoginEdit.getText().toString();

                    String passwd = mPasswdEdit.getText().toString();

                    login(uname, passwd);

                    handled = true;
                }
                return handled;
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = mLoginEdit.getText().toString();

                String passwd = mPasswdEdit.getText().toString();

                login(uname, passwd);
            }
        });

        mNewAccButton = (Button) v.findViewById(R.id.button_new_account);

        mNewAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewAccHandler.onNewClick();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mLoginHandler = (OnLoginClickListener) context;
            mNewAccHandler = (OnNewAccClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnLoginClickListener and OnNewAccClickListener");
        }

    }

    private void login(String uname, String passwd) {

        mLoginHandler.onLoginClick(uname, passwd);
    }
    public interface OnLoginClickListener {
        void onLoginClick(String... params);
    }

    public interface OnNewAccClickListener {
        void onNewClick();
    }
}
