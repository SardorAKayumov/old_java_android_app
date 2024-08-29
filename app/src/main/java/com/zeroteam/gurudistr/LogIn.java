package com.zeroteam.gurudistr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zeroteam.gurudistr.models.UserModel;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore rootRef;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String id;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private View fog;
    private ProgressBar progressBar;
    private TextView alert;

    private long onBackPressedTime;
    private Toast backToast;

    private RelativeLayout rlayoutPhoneNumber;
    private RelativeLayout rlayoutVerificationCode;
    private RelativeLayout rlayoutPassword;

    private EditText phoneNumberInput;
    private Button btnSendVerification;

    private EditText verificationCodeInput;
    private Button btnVerifyPhoneNumber;

    private EditText passwordInput;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();

        rlayoutPhoneNumber = findViewById(R.id.rlayoutPhoneNumberSignIn);
        rlayoutVerificationCode = findViewById(R.id.rlayoutVerificationCodeSignIn);
        rlayoutPassword = findViewById(R.id.rlayoutPasswordSignIn);

        phoneNumberInput = findViewById(R.id.phoneNumberInputSignIn);
        verificationCodeInput = findViewById(R.id.verificationCodeInputSignIn);
        passwordInput = findViewById(R.id.passwordInputSignIn);

        btnSendVerification = findViewById(R.id.btnSendVerificationCodeSignIn);
        btnVerifyPhoneNumber = findViewById(R.id.btnVerifyPhoneNumberSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSendVerification.setOnClickListener(this);
        btnVerifyPhoneNumber.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        fog = findViewById(R.id.fogSignIn);
        progressBar = findViewById(R.id.pbSignIn);
        alert = findViewById(R.id.alertSignIn);

        initializeCallbacks();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSendVerification) {
            final String phoneNumber = "+998" + phoneNumberInput.getText().toString();
            if (phoneNumber.length() == 13 && phoneNumber.charAt(4) == '9') {
                fog.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                phoneNumberInput.setEnabled(false);
                btnSendVerification.setEnabled(false);

                rootRef.collection("users").whereEqualTo("phoneNumber", phoneNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phoneNumber,
                                    60,
                                    TimeUnit.SECONDS,
                                    LogIn.this,
                                    mCallbacks
                            );
                        } else {
                            fog.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            phoneNumberInput.setEnabled(true);
                            btnSendVerification.setEnabled(true);
                            phoneNumberInput.setError("Error");
                            alert.setText("Phone number isn't registered!");
                        }
                    }
                });
            } else {
                phoneNumberInput.setError("Error");
                alert.setText("Phone number isn't valid");
            }
        }
        else if (view == btnVerifyPhoneNumber) {
            if (verificationCodeInput.getText().toString().length() == 6) {
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, verificationCodeInput.getText().toString()));
            } else {
                verificationCodeInput.setError("Error");
                alert.setText("Enter 6-digit verification code!");
            }
        }
        else if (view == btnSignIn) {
            if (!passwordInput.getText().toString().isEmpty()) {
                fog.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                passwordInput.setEnabled(false);
                btnSignIn.setEnabled(false);

                rootRef.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (Objects.requireNonNull(task.getResult()).exists()) {
                                UserModel userModel = Objects.requireNonNull(task.getResult()).toObject(UserModel.class);
                                if (passwordInput.getText().toString().equals(Objects.requireNonNull(userModel).getPassword())) {
                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                                    editor.putString("uid", userModel.getUid());
                                    editor.putString("phoneNumber", userModel.getPhoneNumber());
                                    editor.putString("password", userModel.getPassword());
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), Sections.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("uid", userModel.getUid());
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                                } else {
                                    fog.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    passwordInput.setError("Error");
                                    passwordInput.setEnabled(true);
                                    btnSignIn.setEnabled(true);
                                    alert.setText("Wrong password!");
                                }
                            }
                        } else {
                            fog.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            passwordInput.setError("Error");
                            passwordInput.setEnabled(true);
                            btnSignIn.setEnabled(true);
                            alert.setText(Objects.requireNonNull(task.getResult()).toString());
                        }
                    }
                });
            } else {
                passwordInput.setError("Error");
                alert.setText("Password isn't valid");
            }
        }
    }

    private void initializeCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                alert.setText("");
                rlayoutVerificationCode.setVisibility(View.VISIBLE);
                verificationCodeInput.setEnabled(false);
                verificationCodeInput.setHint("Auto-verifying");
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                fog.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                phoneNumberInput.setEnabled(true);
                btnSendVerification.setEnabled(true);
                alert.setText(e.getMessage());
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                fog.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                alert.setText("");
                rlayoutPhoneNumber.setVisibility(View.GONE);
                rlayoutVerificationCode.setVisibility(View.VISIBLE);
                verificationCodeInput.requestFocus();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fog.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        verificationCodeInput.setEnabled(false);
        btnVerifyPhoneNumber.setEnabled(false);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                fog.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    rlayoutVerificationCode.setVisibility(View.GONE);
                    rlayoutPassword.setVisibility(View.VISIBLE);
                    passwordInput.requestFocus();
                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                    id = Objects.requireNonNull(user).getUid();
                    mAuth.signOut();
                } else {
                    verificationCodeInput.setEnabled(true);
                    btnVerifyPhoneNumber.setEnabled(true);
                    alert.setText(Objects.requireNonNull(task.getResult()).toString());
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
            onBackPressedTime = System.currentTimeMillis();
        }
    }
}
