package com.zeroteam.gurudistr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.instacart.library.truetime.TrueTime;
import com.zeroteam.gurudistr.models.UserModel;

import java.io.IOException;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity{
    private SharedPreferences prefs;
    private CollectionReference usersRef;
    private String uid, phoneNumber, password;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        initTrueTime();

        usersRef = FirebaseFirestore.getInstance().collection("users");

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        uid = prefs.getString("uid", "");
        phoneNumber = prefs.getString("phoneNumber", "");
        password = prefs.getString("password", "");

        autoLogIn();
    }

    private void autoLogIn(){
        final Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                }
            }
        };
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
            intent = new Intent(getApplicationContext(), LogIn.class);
            timer.start();
        } else {
            usersRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserModel userModel = Objects.requireNonNull(task.getResult()).toObject(UserModel.class);
                        if(Objects.requireNonNull(task.getResult()).exists()
                                && uid.equals(userModel.getUid())
                                && phoneNumber.equals(userModel.getPhoneNumber())
                                && password.equals(userModel.getPassword())){

                            intent = new Intent(getApplicationContext(), Sections.class);
                            intent.putExtra("uid", userModel.getUid());
                        } else {
                            prefs.edit().clear().apply();
                            intent = new Intent(getApplicationContext(), LogIn.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        timer.start();
                    }
                });
        }
    }

    private void initTrueTime(){
        System.out.println("INITIALIZING PROCESS IS GOOOOOOOING");
        new InitTrueTimeAsyncTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private static class InitTrueTimeAsyncTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try{
                TrueTime.build().initialize();
                System.out.println("initialized HEHEHEHHEHEHEHEEHHEEHHE");
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed(){
    }
}
