package com.transformation.zezoo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    View view;
    Resources res;
    Context context;
    private AutoCompleteTextView mNameView;
    private EditText mPasswordView, mManagerView;
    private View mProgressView;
    private View mLoginFormView;
    TextInputLayout til_email, til_pass;
    Button mEmailSignInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        context = view.getContext();
        res = context.getResources();
        mNameView = (AutoCompleteTextView) view.findViewById(R.id.point);
        til_email = (TextInputLayout) view.findViewById(R.id.til_point);
        til_pass = (TextInputLayout) view.findViewById(R.id.til_password);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        mManagerView = (EditText) view.findViewById(R.id.manager);
        mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mNameView.setText(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", "user"));
        mManagerView.setText(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("managerName", ""));
        mPasswordView.setText(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("passwordName", ""));
        mEmailSignInButton.setEnabled(false);
        mEmailSignInButton.setText("حفظ التعديل");
        mEmailSignInButton.setTextColor(res.getColor(R.color.black_overlay));
        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailSignInButton.setEnabled(true);
                mEmailSignInButton.setTextColor(res.getColor(R.color.spec_black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailSignInButton.setEnabled(true);
                mEmailSignInButton.setTextColor(res.getColor(R.color.spec_black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailSignInButton.setEnabled(true);
                mEmailSignInButton.setTextColor(res.getColor(R.color.spec_black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNameView.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putString("basicName", mNameView.getText().toString()).commit();
                    context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putString("managerName", mManagerView.getText().toString()).commit();
                    context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putString("passwordName", mPasswordView.getText().toString()).commit();
                    hideKeyboard();
                    MainActivity m = (MainActivity) getActivity();
                    if (!context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("managerName", "").matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                        m.profile = new ProfileDrawerItem().withEmail(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", "")).withIcon(R.mipmap.profile).withIdentifier(100).withSelectedTextColorRes(R.color.spec_black).withTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/font_1.ttf"));
                    } else {
                        m.profile = new ProfileDrawerItem().withName(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("managerName", "")).withEmail(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", "مستخدم")).withIcon(R.mipmap.profile).withIdentifier(100).withSelectedTextColorRes(R.color.spec_black).withTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/font_1.ttf"));
                    }
                    m.selectionMode = 5;
                    m.result.setSelection(1);
                } else {
                    mNameView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                }
            }
        });

        return view;
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
