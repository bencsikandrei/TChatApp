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
import android.widget.Toast;

/**
 * @author Andrei
 *
 * Fragment for the VIEW of Subscribe Activity
 *
 * Has an AsyncTask for updating messages
 *
 * Uses one callback to create a user
 *
 *
 */

public class SubscribeFragment extends Fragment {

    private OnCreateAccListener mOnCreateAccHandler;

    private Button mNewAccButton;
    private EditText mEditTextLogin;
    private EditText mEditTextPasswd;
    private EditText mEditTextPasswdAgain;

    public SubscribeFragment() {
    }

    public static Fragment newInstance(Bundle args) {
        SubscribeFragment nAf = new SubscribeFragment();

        nAf.setArguments(args);

        return nAf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscribe, container, false);

        // the edit text for the login
        mEditTextLogin = (EditText) v.findViewById(R.id.subscribe_edit_text_login_uname);

        // the edit text for the login
        mEditTextPasswd = (EditText) v.findViewById(R.id.subscribe_edit_text_passwd);

        // password verification
        mEditTextPasswdAgain = (EditText) v.findViewById((R.id.subscribe_edit_text_passwd_again));

        mEditTextPasswdAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditTextLogin.getText().length() < 1 || mEditTextPasswd.getText().toString().length() < 0) {
                    mNewAccButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0 && mEditTextPasswd.getText().toString().length() > 0) {
                    mNewAccButton.setEnabled(true);
                }
            }
        });

        mEditTextPasswdAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String login = mEditTextLogin.getText().toString();

                    String passwd = mEditTextPasswd.getText().toString();

                    String passwdAgain = mEditTextPasswdAgain.getText().toString();

                    String urlPhoto = "";

                    subscribe(login, passwd, passwdAgain);
                }
                return false;
            }
        });

        // the button
        mNewAccButton = (Button) v.findViewById(R.id.subscribe_button_create);

        mNewAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = mEditTextLogin.getText().toString();

                String passwd = mEditTextPasswd.getText().toString();

                String passwdAgain = mEditTextPasswdAgain.getText().toString();

                String urlPhoto = "";

                subscribe(login, passwd, passwdAgain);

            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnCreateAccHandler = (OnCreateAccListener) context;

        } catch (ClassCastException cex) {
            throw
                    new ClassCastException(context.toString()
                            + " does not implement the required interface !");
        }
    }

    private void subscribe(String uname, String pass, String passAgain) {
        if (pass.equals(passAgain)) {
            mOnCreateAccHandler.onCreateAccClick(uname, pass, passAgain);
            getActivity().finish();
        } else
            Toast.makeText(getContext(), "The passwords do not match !", Toast.LENGTH_SHORT)
                    .show();
    }
    public interface OnCreateAccListener {
        void onCreateAccClick(String... params);
    }

}
