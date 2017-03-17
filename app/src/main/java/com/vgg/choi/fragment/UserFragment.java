package com.vgg.choi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vgg.choi.R;
import com.vgg.choi.activity.LoginActivity;
import com.vgg.sdk.SdkHelper;

/**
 * Created by LeHai on 3/9/2017.
 */

public class UserFragment extends Fragment {
    View loginView;
    SdkHelper sdkHelper = new SdkHelper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        loginView = view.findViewById(R.id.user_login_button);
        if (loginView != null) {
            loginView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
            loginView.setVisibility(sdkHelper.isLogin()? View.GONE : View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginView.setVisibility(sdkHelper.isLogin()? View.GONE : View.VISIBLE);
    }

    void login() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }
}
