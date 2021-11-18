package com.swagVideo.in.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.swagVideo.in.MainApplication;
import com.swagVideo.in.R;
import com.swagVideo.in.data.api.REST;
import com.swagVideo.in.data.models.Token;
import com.swagVideo.in.utils.LocaleUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.swagVideo.in.activities.PhoneLoginBaseActivity.EXTRA_TOKEN;
import static org.greenrobot.eventbus.EventBus.TAG;
public class OtpActivty extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private KProgressHUD mProgress;
    private String mVerificationId;
    private boolean mVerificationInProgress;
    protected PhoneLoginActivityViewModel mModel;
    public static final String EXTRA_TOKEN = "token";
    private static final String TAG = "PhoneLoginBaseActivity";
    String namee,phonee,otpp;
    String s ="";
    String s1 ="";
    String s2 ="";
    String s3 ="";
    int cc1;
    Boolean sent= true;
    Boolean exists = true;
    EditText edit_one_mpin,edit_two_mpin,edit_three_mpin,edit_four_mpin;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleUtil.wrap(base));
    }
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        namee = intent.getStringExtra("name");
        phonee = intent.getStringExtra("phone");
        String ccc = intent.getStringExtra("cc");
     //   otp = intent.getStringExtra("otp");
        cc1 = Integer.parseInt(ccc);
//        mModel.cc = cc1;
//        mModel.name = namee;
//        mModel.phone = phonee;
//        Log.e("MSG",namee+" "+phonee+" "+cc1+" ");
        int dcc = getResources().getInteger(R.integer.default_calling_code);
        mModel = new ViewModelProvider(this, new PhoneLoginActivityViewModel.Factory(dcc))
                .get(PhoneLoginActivityViewModel.class);
        verify = (Button)findViewById(R.id.verify);
        edit_one_mpin = (EditText)findViewById(R.id.edit_one_mpin);
        edit_two_mpin = (EditText)findViewById(R.id.edit_two_mpin);
        edit_three_mpin = (EditText)findViewById(R.id.edit_three_mpin);
        edit_four_mpin = (EditText)findViewById(R.id.edit_four_mpin);
        edit_one_mpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           //  Log.e("sos",""+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //   Log.e("count",""+String.valueOf(s.length()));
                if (edit_one_mpin.getText().toString().length() == 1) {
                    edit_two_mpin.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable edit) {
                Log.e("sos",""+s);
                s = edit.toString();
            }
        });

        edit_two_mpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Log.e("sos",""+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //   Log.e("count",""+String.valueOf(s.length()));
                if (edit_two_mpin.getText().toString().length() == 1) {
                    edit_three_mpin.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable edit) {
                Log.e("sos",""+s);
                s1 = edit.toString();
            }
        });
        edit_three_mpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Log.e("sos",""+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //   Log.e("count",""+String.valueOf(s.length()));
                if (edit_three_mpin.getText().toString().length() == 1) {
                    edit_four_mpin.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable edit) {
                Log.e("sos",""+s);
                s2 = edit.toString();
            }
        });

        edit_four_mpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Log.e("sos",""+s);
            }

            @Override
            public void onTextChanged(CharSequence ss, int start, int before, int count) {
                //   Log.e("count",""+String.valueOf(s.length()));
                if (edit_three_mpin.getText().toString().length() == 1) {
                   // Log.e("model",""+mModel.cc+" "+mModel.phone+" "+mModel.otp+" "+mModel.name);
                    //verifyOtp(phone);
                }
            }

            @Override
            public void afterTextChanged(Editable edit) {
                Log.e("sos",""+s);
                s3 = edit.toString();
                otpp = s+s1+s2+s3;
                Log.e("otp",otpp);
                mModel.otp = otpp;
                Log.e("model",""+mModel.cc+" "+mModel.phone+" "+mModel.otp+" "+mModel.name);
                verifyOtp(cc1,phonee,otpp,namee);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(),MainActivity.class);
               // startActivity(intent);
            }
        });
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
     //   View verify = findViewById(R.id.verify);
        mModel.doesExist.observe(this, exists -> {
            Boolean sent = mModel.isSent.getValue();
            //name.setVisibility(sent && !exists ? View.VISIBLE : View.GONE);
        });
        mModel.isSent.observe(this, sent -> {
            Boolean exists = mModel.doesExist.getValue();
//            name.setVisibility(sent && !exists ? View.VISIBLE : View.GONE);
//            otp.setVisibility(sent ? View.VISIBLE : View.GONE);
//            if (sent) {
//                otp.requestFocus();
//            }
//
//            verify.setEnabled(sent);
        });
//        mModel.errors.observe(this, errors -> {
//            phone.setError(null);
//            otp.setError(null);
//            name.setError(null);
//            if (errors == null) {
//                return;
//            }
//            if (errors.containsKey("phone")) {
//                phone.setError(errors.get("phone"));
//            }
//            if (errors.containsKey("otp")) {
//                otp.setError(errors.get("otp"));
//            }
//            if (errors.containsKey("name")) {
//                name.setError(errors.get("name"));
//            }
//        });
//        phone.getEditText().requestFocus();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress) {
            generateOtp();
        }
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

    private void verifyOtpp() {
        mModel.errors.postValue(null);
        Map<String, String> errors = new HashMap<>();
        if (TextUtils.isEmpty(mModel.otp)) {
            errors.put("otp", getString(R.string.error_field_required));
        }

        if (errors.isEmpty()) {
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(mVerificationId, mModel.otp);
            loginWithCredential(credential);
        } else {
            mModel.errors.postValue(errors);
        }
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
                            Toast.makeText(OtpActivty.this, R.string.error_server, Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(OtpActivty.this, R.string.error_internet, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyOtp(int cc,String phone,String otp,String name) {
        mModel.errors.postValue(null);
        KProgressHUD progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.progress_title))
                .setCancellable(false)
                .show();
        REST rest = MainApplication.getContainer().get(REST.class);
//        Log.e("model",""+mModel.cc+" "+mModel.phone+" "+mModel.otp+" "+mModel.name);
        Log.e("model",""+cc+" "+phone+" "+otp+" "+name);
//        rest.loginPhone(mModel.cc + "", mModel.phone, mModel.otp, mModel.name)
        rest.loginPhone(cc + "", phone, otp, name)
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(
                            @Nullable Call<Token> call,
                            @Nullable Response<Token> response
                    ) {
                        progress.dismiss();
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Intent data = new Intent();
                                data.putExtra(EXTRA_TOKEN, response.body());
                                setResult(RESULT_OK, data);
                                finish();
                                Log.e("model",""+response.body());
                            } else if (response.code() == 422) {
                                try {
                                    String content = response.errorBody().string();
                                    showErrors(new JSONObject(content));
                                } catch (Exception ignore) {
                                }
                            } else {
                                Toast.makeText(OtpActivty.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(
                            @Nullable Call<Token> call,
                            @Nullable Throwable t
                    ) {
                        Log.e(TAG, "Failed when trying to verify OTP.", t);
                        Toast.makeText(OtpActivty.this, R.string.error_internet, Toast.LENGTH_SHORT).show();
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

}
