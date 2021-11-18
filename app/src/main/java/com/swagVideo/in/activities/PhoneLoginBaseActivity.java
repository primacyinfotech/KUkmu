package com.swagVideo.in.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.swagVideo.in.MainApplication;
import com.swagVideo.in.R;
import com.swagVideo.in.data.api.REST;
import com.swagVideo.in.data.models.Exists;
import com.swagVideo.in.data.models.Token;
import com.swagVideo.in.utils.LocaleUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
public class PhoneLoginBaseActivity extends AppCompatActivity {
    public static final String EXTRA_TOKEN = "token";
    protected PhoneLoginActivityViewModel mModel;
    private static final String TAG = "PhoneLoginBaseActivity";
   // TextInputEditText phonenumber;
    EditText name;
    TextInputLayout phone;
    /////////////////////////
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private KProgressHUD mProgress;
    private String mVerificationId;
    private boolean mVerificationInProgress;
    Boolean sent= true;
    Boolean exists = true;
    //////////////////////////////////////////
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleUtil.wrap(base));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      // setContentView(R.layout.activity_phone_login);
        setContentView(R.layout.activity_phone_login1);
        //phonenumber = (TextInputEditText)findViewById(R.id.phonenumber);
        name = (EditText)findViewById(R.id.name);
        ImageButton close = findViewById(R.id.header_back);
        close.setImageResource(R.drawable.ic_baseline_close_24);
        close.setOnClickListener(view -> finish());
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.login_label);
        findViewById(R.id.header_more).setVisibility(View.GONE);
        int dcc = getResources().getInteger(R.integer.default_calling_code);
        mModel = new ViewModelProvider(this, new PhoneLoginActivityViewModel.Factory(dcc))
                .get(PhoneLoginActivityViewModel.class);
        CountryCodePicker cc = findViewById(R.id.cc);
        cc.setCountryForPhoneCode(mModel.cc);
        cc.setOnCountryChangeListener(() -> mModel.cc = cc.getSelectedCountryCodeAsInt());
         phone = (TextInputLayout)findViewById(R.id.phone);
        cc.registerCarrierNumberEditText(phone.getEditText());
        phone.getEditText().setText(mModel.phone);
        phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mModel.phone = editable.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
       // TextInputLayout otp = findViewById(R.id.otp);
//        otp.getEditText().setText(mModel.otp);
//        otp.getEditText().addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                mModel.otp = editable.toString();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//        });
//        TextInputLayout name = findViewById(R.id.name);
//        name.getEditText().setText(mModel.name);
//        name.getEditText().addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                mModel.name = editable.toString();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//        });


        phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //   Log.e("count",""+String.valueOf(s.length()));

            }

            @Override
            public void afterTextChanged(Editable s) {
                int counts = Integer.parseInt(String.valueOf(s.length()));
                if (counts == 10) {
                    mModel.phone = s.toString();
                    final String phoneId = phone.getEditText().toString();
                    final String namee = name.getText().toString();
                    if (TextUtils.isEmpty(phoneId)) {
                        phone.setError("Enter your Phone Number");
                        phone.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(namee)) {
                        name.setError("Enter your Name");
                        name.requestFocus();
                        return;
                    }
                    /// mModel.phone = phoneId;
                    generateOtp(namee);
                }
            }
        });
        View generate = findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneId = phone.getEditText().toString();
                final String namee = name.getText().toString();
                if (TextUtils.isEmpty(phoneId)) {
                    phone.setError("Enter your Phone Number");
                    phone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(namee)) {
                    name.setError("Enter your Name");
                    name.requestFocus();
                    return;
                }
                String cc= String.valueOf(mModel.cc);
                Intent myIntent = new Intent(getApplicationContext(),OtpActivty.class);
                myIntent.putExtra("name", namee);
                myIntent.putExtra("phone", mModel.phone);
                myIntent.putExtra("cc", cc);
               // myIntent.putExtra("otp",  mModel.otp);
                //Optional parameters
                PhoneLoginBaseActivity.this.startActivity(myIntent);
//
            }
        });

       // View verify = findViewById(R.id.verify);
        mModel.doesExist.observe(this, exists -> {
            sent = mModel.isSent.getValue();
          //  name.setVisibility(sent && !exists ? View.VISIBLE : View.GONE);
        });
        mModel.isSent.observe(this, sent -> {
            exists = mModel.doesExist.getValue();
         //   name.setVisibility(sent && !exists ? View.VISIBLE : View.GONE);
//            otp.setVisibility(sent ? View.VISIBLE : View.GONE);
//            if (sent) {
//                otp.requestFocus();
//            }

          //  verify.setEnabled(sent);
        });
        mModel.errors.observe(this, errors -> {
            phone.setError(null);
          //  otp.setError(null);
          //  name.setError(null);
            if (errors == null) {
                return;
            }
            if (errors.containsKey("phone")) {
                phone.setError(errors.get("phone"));
            }
//            if (errors.containsKey("otp")) {
//                otp.setError(errors.get("otp"));
//            }
            if (errors.containsKey("name")) {
             //   name.setError(errors.get("name"));
            }
        });
        phone.getEditText().requestFocus();
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
                Log.d(TAG, "Phone verification successfully completed, credential:" + credential);
                mVerificationInProgress = false;
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }

                loginWithCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                Log.d(TAG, "Phone verification failed with Firebase.", e);
                mVerificationInProgress = false;
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("phone", getString(R.string.error_invalid_phone));
                    mModel.errors.postValue(errors);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.error_internet,
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "Phone verification code was sent: " + verificationId);
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }
                mVerificationId = verificationId;
                mModel.doesExist.postValue(true);
                mModel.isSent.postValue(true);
            }
        };
    }

    public static class PhoneLoginActivityViewModel extends ViewModel {

        public int cc;
        public String phone = "";
        public String otp = "";
        public String name = "";
        public MutableLiveData<Boolean> doesExist = new MutableLiveData<>(false);
        public MutableLiveData<Boolean> isSent = new MutableLiveData<>(false);

        public MutableLiveData<Map<String, String>> errors = new MutableLiveData<>();

        public PhoneLoginActivityViewModel(int cc) {
            this.cc = cc;
        }

        private static class Factory implements ViewModelProvider.Factory {

            private final int mCallingCode;

            public Factory(int cc) {
                mCallingCode = cc;
            }

            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                //noinspection unchecked
                return (T)new PhoneLoginActivityViewModel(mCallingCode);
            }
        }
    }
    private void generateOtp(String namee) {
        mModel.errors.postValue(null);
        KProgressHUD progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.progress_title))
                .setCancellable(false)
                .show();
        REST rest = MainApplication.getContainer().get(REST.class);
        rest.loginPhoneOtp(mModel.cc + "", mModel.phone)
                .enqueue(new Callback<Exists>() {

                    @Override
                    public void onResponse(
                            @Nullable Call<Exists> call,
                            @Nullable Response<Exists> response
                    ) {
                        int code = response != null ? response.code() : -1;
                        int message = -1;
                        if (code == 200) {
                            boolean exists = response.body().exists;
                            mModel.doesExist.postValue(exists);
                            mModel.isSent.postValue(true);
                            message = R.string.login_otp_sent;
                            Log.e("OTPSEND",""+message);

                        } else if (code == 422) {
                            try {
                                String content = response.errorBody().string();
                                showErrors(new JSONObject(content));
                            } catch (Exception ignore) {
                            }
                        } else {
                            message = R.string.error_internet;
                        }

                        if (message != -1) {
                            Toast.makeText(PhoneLoginBaseActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(
                            @Nullable Call<Exists> call,
                            @Nullable Throwable t
                    ) {
                        Log.e(TAG, "Failed when trying to generate OTP.", t);
                        Toast.makeText(PhoneLoginBaseActivity.this, R.string.error_internet, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }
    private void showErrors(JSONObject json) throws Exception {
        JSONObject errors = json.getJSONObject("errors");
        Map<String, String> messages = new HashMap<>();
        String[] keys = new String[]{"cc", "phone", "otp", "name"};
        for (String key : keys) {
            JSONArray fields = errors.optJSONArray(key);
            if (fields != null) {
                messages.put(key, fields.getString(0));
            }
        }

        mModel.errors.postValue(messages);
    }
    private void loginWithCredential(PhoneAuthCredential credential) {
        mProgress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.progress_title))
                .setCancellable(false)
                .show();
        mModel.errors.postValue(null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        user.getIdToken(false).addOnCompleteListener(this, task1 -> {
                            if (task.isSuccessful()) {
                                String token = task1.getResult().getToken();
                                loginWithServer(token);
                            } else if (mProgress != null && mProgress.isShowing()) {
                                mProgress.dismiss();
                            }
                        });
                    } else {
                        if (mProgress != null && mProgress.isShowing()) {
                            mProgress.dismiss();
                        }

                        Log.e(TAG, "Signin with phone credential failed.", task.getException());
                        Map<String, String> errors = new HashMap<>();
                        errors.put("otp", getString(R.string.error_invalid_otp));
                        mModel.errors.postValue(errors);
                    }
                });
    }
    private void loginWithServer(String token) {
        Log.v(TAG, "Transmitting Firebase ID token to server: " + token);
        REST rest = MainApplication.getContainer().get(REST.class);
        rest.loginFirebase(token)
                .enqueue(new Callback<Token>() {

                    @Override
                    public void onResponse(
                            @Nullable Call<Token> call,
                            @Nullable Response<Token> response
                    ) {
                        if (mProgress != null && mProgress.isShowing()) {
                            mProgress.dismiss();
                        }

                        if (response != null && response.isSuccessful()) {
                            Intent data = new Intent();
                            data.putExtra(EXTRA_TOKEN, response.body());
                            setResult(RESULT_OK, data);
                            finish();
                        } else {
                            Toast.makeText(PhoneLoginBaseActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            @Nullable Call<Token> call,
                            @Nullable Throwable t
                    ) {
                        Log.e(TAG, "Login with Firebase phone auth has failed.", t);
                        if (mProgress != null && mProgress.isShowing()) {
                            mProgress.dismiss();
                        }

                        Toast.makeText(PhoneLoginBaseActivity.this, R.string.error_internet, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void generateOtp() {
        mProgress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.progress_title))
                .setCancellable(false)
                .show();
        mModel.errors.postValue(null);
        Map<String, String> errors = new HashMap<>();
        if (TextUtils.isEmpty(mModel.cc + "")) {
            errors.put("cc", getString(R.string.error_field_required));
        } else if (TextUtils.isEmpty(mModel.phone)) {
            errors.put("phone", getString(R.string.error_field_required));
        }

        if (errors.isEmpty()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+" + mModel.cc + mModel.phone,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);
            mVerificationInProgress = true;
        } else {
            mModel.errors.postValue(errors);
        }
    }
}
