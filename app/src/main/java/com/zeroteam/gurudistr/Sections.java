package com.zeroteam.gurudistr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Sections extends AppCompatActivity implements View.OnClickListener{
    private Toast backToast;
    private long onBackPressedTime;

    private LinearLayout distributionLayout;
    private LinearLayout moderationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        distributionLayout = findViewById(R.id.distributionLayout);
        moderationLayout = findViewById(R.id.moderationLayout);
        distributionLayout.setOnClickListener(this);
        moderationLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.distributionLayout:
                Intent intent = new Intent(getApplicationContext(), TerritoriesList.class);
                intent.putExtra("uid", getIntent().getStringExtra("uid"));
                startActivity(intent);
                break;
            case R.id.moderationLayout:
                break;
            default:
        }
    }

    private void signOut(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.clear();
        editor.apply();
        getIntent().removeExtra("uid");
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "SignOut Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if(onBackPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
        } else{
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
            onBackPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sections, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.sign_out_button:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
