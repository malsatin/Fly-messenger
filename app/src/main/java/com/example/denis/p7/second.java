package com.example.denis.p7;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class second extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ActionBar ab;
    FloatingActionButton fabAttach;
    FloatingActionButton fabSend;
    EditText editText;
    LinearLayout linearLayout;
    LinearLayout.LayoutParams layoutParams;
    TextView textView;
    InputMethodManager imm;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(first.TAG, "second.class onCreate");

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.bar));
        intent = getIntent();
        ab.setTitle(intent.getStringExtra(first.C_NICKNAME));


        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fabAttach.setVisibility(View.INVISIBLE);
                fabSend.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // прячем клавиатуру. butCalculate - это кнопка
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        fabAttach = (FloatingActionButton) findViewById(R.id.fabAttach);
        fabSend = (FloatingActionButton) findViewById(R.id.fabSend);
        fabSend.setOnClickListener(this);

        linearLayout = (LinearLayout) findViewById(R.id.llscroll);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        layoutParams.setMargins(5, 5, 5, 5);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(this, first.class);
                Log.e(first.TAG,"      home");
                startActivity(intent);
                return true;
            case R.id.fullInfo:
                return true;
            case R.id.clear:
                linearLayout.removeAllViews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSend:
                textView = new TextView(this);
                textView.setText(editText.getText());
                //   textView.setBackgroundColor(getColor(R.color.message));
                //   textView.setTextColor(getColor(R.color.black));
                textView.setLayoutParams(layoutParams);
                linearLayout.addView(textView);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                editText.setText("");
                fabAttach.setVisibility(View.VISIBLE);
                fabSend.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_attach, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.image:
                break;
            case R.id.audio:
                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(first.TAG, "second.class onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(first.TAG, "second.class onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(first.TAG, "second.class onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(first.TAG, "second.class onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(first.TAG, "second.class onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(first.TAG, "second.class onDestroy");
    }
}
