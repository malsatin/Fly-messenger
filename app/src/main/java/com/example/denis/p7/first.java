package com.example.denis.p7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class first extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText eTcNickname;
    final static String C_NICKNAME = "COMPANION_NICKNAME";
    final static String TAG = "MY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.e(TAG, "first.class onCreate");

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        eTcNickname = (EditText) findViewById(R.id.eTcNickname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, second.class);
                intent.putExtra(C_NICKNAME, eTcNickname.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "first.class onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "first.class onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "first.class onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "first.class onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "first.class onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "first.class onDestroy");
    }
}
