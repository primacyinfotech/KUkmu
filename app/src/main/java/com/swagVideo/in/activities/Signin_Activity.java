package com.swagVideo.in.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.swagVideo.in.R;
import com.swagVideo.in.utils.LocaleUtil;

public class Signin_Activity extends AppCompatActivity {
    public static final String EXTRA_TOKEN = "token";
    private static final String TAG = "PhoneLoginBaseActivity";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleUtil.wrap(base));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login1);

    }
}
