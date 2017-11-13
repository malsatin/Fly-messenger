package com.example.denis.p7;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.denis.p7.algorithms.helpers.ByteHelper;

public class second extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ActionBar ab;
    FloatingActionButton fabAttach;
    FloatingActionButton fabSend;
    EditText editText;
    LinearLayout lLscroll;
    LinearLayout.LayoutParams layoutParams;
    TextView msgTV;
    ImageView iV;
    InputMethodManager imm;
    Intent intent;
    final int REQUEST_CODE_IMAGE = 1;
    final int REQUEST_CODE_AUDIO = 2;
    String uri;
    int k = 0;
    TCPClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(first.TAG, "second.class onCreate");

        client = new TCPClient("localhost", 3128);

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


        lLscroll = (LinearLayout) findViewById(R.id.llscroll);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;


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
                startActivity(intent);
                return true;
            case R.id.fullInfo:
                return true;
            case R.id.clear:
                // Get messages
                byte[][] result = client.getMessages(0);

                LinearLayout msglL = new LinearLayout(this);
                msgTV = new TextView(this);

                layoutParams.setMargins(5, 5, 5, 5);
                msglL.setLayoutParams(layoutParams);
                layoutParams.setMargins(45, 20, 30, 0);
                msgTV.setLayoutParams(layoutParams);

                msgTV.setText(ByteHelper.getStringFromBytes(result[0]).toString());
                msglL.setBackgroundResource(R.drawable.msg_in);

                msglL.addView(msgTV);
                lLscroll.addView(msglL);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSend:

                byte[] bytes1=ByteHelper.getBytesFromString(editText.getText().toString());
                // Send bytes to server
                client.sendMessage(bytes1);
                Log.d(first.TAG, "is sended   ");

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
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");

                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
                }
                break;

            case R.id.audio:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");

                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE_AUDIO);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    LinearLayout lLmsg = new LinearLayout(this);
                    uri = data.getDataString();
                    iV = new ImageView(this);
                    layoutParams.setMargins(5, 5, 5, 5);
                    lLmsg.setLayoutParams(layoutParams);
                    iV.setLayoutParams(layoutParams);
                    lLmsg.setBackgroundResource(R.drawable.msg_photo);
                    iV.setImageURI(Uri.parse(uri));
                    iV.setContentDescription(uri);
                    iV.setOnClickListener(onClickListenerIV);
                    // linearLayout.text
                    lLmsg.addView(iV);
                    lLscroll.addView(lLmsg);
                    break;

                case REQUEST_CODE_AUDIO:
                    uri = data.getDataString();
                    msgTV = new TextView(this);
                    msgTV.setText(data.toString());
                    msgTV.setContentDescription(uri);
                    msgTV.setOnClickListener(onClickListenerAV);
                    msgTV.setLayoutParams(layoutParams);
                    lLscroll.addView(msgTV);
                    break;
            }
        }
    }

    View.OnClickListener onClickListenerIV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getContentDescription().toString()));
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {

                startActivity(intent);
            }
        }
    };

    View.OnClickListener onClickListenerAV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getContentDescription().toString()));
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {

                startActivity(intent);
            }
        }
    };

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
