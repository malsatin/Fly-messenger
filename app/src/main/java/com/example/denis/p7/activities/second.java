package com.example.denis.p7.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denis.p7.R;
import com.example.denis.p7.TCPClient;
import com.example.denis.p7.algorithms.coding.HammingCode;
import com.example.denis.p7.algorithms.coding.ParityBit;
import com.example.denis.p7.algorithms.coding.RepetitionCode;
import com.example.denis.p7.algorithms.exceptions.FileTooBigException;
import com.example.denis.p7.algorithms.helpers.ByteHelper;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;

public class second extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ActionBar ab;
    FloatingActionButton fabAttach, fabSend;
    EditText editText;
    LinearLayout scrollLL, msgTextLL, msgImageLL;
    LinearLayout.LayoutParams msgTextLParamsLL, msgImageLParamsLL, msgLParamsTV, msgLParamsIV;
    TextView msgTV;
    ImageView msgIV;
    InputMethodManager imm;
    Intent intent;
    SendMsg sendMsg;
    GetMsgs getMsgs;
    final int REQUEST_CODE_IMAGE = 1, REQUEST_CODE_AUDIO = 2;
    String uri, ip = "138.197.176.233";
    TCPClient client;
    byte[] bytes;
    byte[] nickname;
    byte b;
    String s;
    int k = 0, port = 3129;
    byte codingType, compressionType, msgType;

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
        ab.setTitle(R.string.chatting);

        intent = getIntent();
        codingType = (byte) intent.getIntExtra(first.codingTypeString, 5);
        compressionType = (byte) intent.getIntExtra(first.compressionTypeString, 5);
        bytes = ByteHelper.getBytesFromString(intent.getStringExtra(first.nickname));
        byte space = ByteHelper.getBytesFromString(" ")[0];
        nickname = new byte[20];
        for (int i = 0; i < 20; i++) {
            nickname[i] = (bytes.length > i) ? bytes[i] : space;
        }

        client = new TCPClient(ip, port);

        scrollLL = (LinearLayout) findViewById(R.id.scrollLL);
        msgTextLL = (LinearLayout) findViewById(R.id.msgTextLL);
        msgTV = (TextView) findViewById(R.id.msgTV);
        msgImageLL = (LinearLayout) findViewById(R.id.msgImageLL);
        msgIV = (ImageView) findViewById(R.id.msgIV);
        msgTextLParamsLL = (LinearLayout.LayoutParams) msgTextLL.getLayoutParams();
        msgLParamsTV = (LinearLayout.LayoutParams) msgTV.getLayoutParams();
        msgImageLParamsLL = (LinearLayout.LayoutParams) msgImageLL.getLayoutParams();
        msgLParamsIV = (LinearLayout.LayoutParams) msgIV.getLayoutParams();

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
                ClearHistory clearHistory=new ClearHistory();
                clearHistory.execute();
                return true;
            case R.id.clear:
                getMsgs = new GetMsgs();
                getMsgs.execute(k);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSend:
                sendMsg = new SendMsg();
                bytes = ByteHelper.getBytesFromString(editText.getText().toString());
                msgType = (byte) 0;
                sendMsg.execute(bytes);
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
                    uri = data.getDataString();
                    sendMsg = new SendMsg();
                    msgType = (byte) 1;
                    try {
                        sendMsg.execute(ByteHelper.readBytesFromFile(Uri.parse(uri).getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (FileTooBigException e) {
                        e.printStackTrace();
                    }
                    break;

                case REQUEST_CODE_AUDIO:
                    uri = data.getDataString();
                    msgTV = new TextView(this);
                    msgTV.setText(data.toString());
                    msgTV.setContentDescription(uri);
                    msgTV.setOnClickListener(onClickListenerAV);
                    msgTV.setLayoutParams(msgTextLParamsLL);
                    scrollLL.addView(msgTV);
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

    class SendMsg extends AsyncTask<byte[], Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Integer doInBackground(byte[]... bytes) {
            int response = 4;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ByteArrayOutputStream codedStream = new ByteArrayOutputStream();
            ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();
            outStream.write(msgType);
            outStream.write(codingType);
            outStream.write(compressionType);
            try {
                outStream.write(nickname);
                switch (codingType) {
                    case (byte) 0:
                        codedStream.write((new HammingCode()).encodeByteString(bytes[0]));
                        break;
                    case (byte) 1:
                        codedStream.write((new ParityBit()).encodeByteString(bytes[0]));
                        break;
                    case (byte) 2:
                        codedStream.write((new RepetitionCode()).encodeByteString(bytes[0]));
                        break;
                }
//                switch(compressionType){
//                    case (byte)0:
//                        compressedStream.write((new Huffman()).compressByteString(codedStream.toByteArray()));
//                        break;
//                    case (byte)1:
//                        compressedStream.write((new LZ77()).compressByteString(codedStream.toByteArray()));
//                        break;
//                    case (byte)2:
//                        compressedStream.write((new RLE()).compressByteString(codedStream.toByteArray()));
//                        break;
//                }
                outStream.write(codedStream.toByteArray());//TODO replace coded on compressed
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] block = outStream.toByteArray();

            // Send bytes to server
            try {
                response = client.sendMessage(block);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            switch (response) {
                case 0:
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    editText.setText("");
                    fabAttach.setVisibility(View.VISIBLE);
                    fabSend.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    Toast.makeText(second.this, "Internal database error, probably size limit exceeded which is ~800MB", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(second.this, "Received damaged message", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(second.this, "Connection with server failed", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(second.this, "Some exception", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    class GetMsgs extends AsyncTask<Integer, Void, byte[][]> {

        @Override
        protected byte[][] doInBackground(Integer... alreadyHaveMessages) {
            byte[][] result = new byte[0][];
            // Get messages
            try {
                result = client.getMessages(alreadyHaveMessages[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(byte[][] bytes) {
            super.onPostExecute(bytes);
            if (bytes[0] == null) return;
            ByteArrayOutputStream decodedStream;
            ByteArrayOutputStream decompressedStream;
            for (int i = 0; i < bytes.length; i++) {
                decodedStream = new ByteArrayOutputStream();
                decompressedStream = new ByteArrayOutputStream();
                byte[] msg = new byte[bytes[i].length - 23];
                System.arraycopy(bytes[i], 23, msg, 0, msg.length);
//                switch (bytes[i][2]) {
//                    case (byte) 0:
//                        decompressedStream.write((new Huffman()).decompressByteString(msg));
//                        break;
//                    case (byte) 1:
//                        decompressedStream.write((new LZ77()).decompressByteString(msg));
//                        break;
//                    case (byte) 2:
//                        decompressedStream.write((new RLE()).decompressByteString(msg));
//                        break;
//                }
                try {
                    switch (bytes[i][1]) {
                        case (byte) 0:
                            decodedStream.write((new HammingCode()).decodeByteString(msg));//TODO replace msg
                            break;
                        case (byte) 1:
                            decodedStream.write((new ParityBit()).decodeByteString(msg));//TODO replace msg
                            break;
                        case (byte) 2:
                            decodedStream.write((new RepetitionCode()).decodeByteString(msg));//TODO replace msg
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (bytes[i][0]) {
                    case (byte) 0:
                        msgTextLL = new LinearLayout(second.this);
                        msgTextLL.setBackgroundResource(R.drawable.msg_in);
                        msgTV = new TextView(second.this);

                        msgTextLL.setLayoutParams(msgTextLParamsLL);
                        msgTV.setLayoutParams(msgLParamsTV);

                        s = ByteHelper.getStringFromBytes(decodedStream.toByteArray());
                        msgTV.setText(s);

                        msgTextLL.addView(msgTV);
                        scrollLL.addView(msgTextLL);
                        break;
                    case (byte) 1:
                        msgImageLL = new LinearLayout(second.this);
                        msgImageLL.setBackgroundResource(R.drawable.msg_photo);
                        msgIV = new ImageView(second.this);

                        msgImageLL.setLayoutParams(msgImageLParamsLL);
                        msgIV.setLayoutParams(msgLParamsIV);

                        String path = Environment.getDataDirectory().getPath();
                        try {
                            ByteHelper.writeBytesToFile(decodedStream.toByteArray(), path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // msgIV.setImageURI();
                        msgIV.setContentDescription(uri);
                        msgIV.setOnClickListener(onClickListenerIV);

                        msgImageLL.addView(msgIV);
                        scrollLL.addView(msgImageLL);
                        break;
                    case (byte) 2:
                        break;
                }
                k++;
            }
        }
    }

    class ClearHistory extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int response = client.requestClear();
            return response;
        }

        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            switch (response) {
                case 0:
                    Toast.makeText(second.this, "Database is empty", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(second.this, "Internal database error", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(second.this, "Connection with server failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}

