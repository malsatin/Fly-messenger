package com.example.denis.p7.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.denis.p7.R;

public class first extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText nicknameET;
    static int codingType;
    static int compressionType;
    static String nickname;
    final static String TAG = "MY_TAG";
    Spinner codingSpinner;
    Spinner compressionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.e(TAG, "first.class onCreate");

        nicknameET = (EditText) findViewById(R.id.nicknameET);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        codingSpinner = (Spinner) findViewById(R.id.codingSpinner);
        compressionSpinner = (Spinner) findViewById(R.id.compressionSpinner);

        codingSpinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{getString(R.string.coding1), getString(R.string.coding2), getString(R.string.coding3)}));
        compressionSpinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{getString(R.string.compression1), getString(R.string.compression2), getString(R.string.compression3)}));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, second.class);
                codingType = codingSpinner.getSelectedItemPosition();
                compressionType = compressionSpinner.getSelectedItemPosition();
                nickname = nicknameET.getText().toString();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "first.class onDestroy");
        System.exit(0);
    }
}
