package com.example.denis.p7.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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
import com.example.denis.p7.algorithms.compression.Huffman;
import com.example.denis.p7.algorithms.compression.LZ77;
import com.example.denis.p7.algorithms.compression.RLE;
import com.example.denis.p7.algorithms.exceptions.DecompressionException;
import com.example.denis.p7.algorithms.exceptions.FileTooBigException;
import com.example.denis.p7.algorithms.helpers.BitStream;
import com.example.denis.p7.algorithms.helpers.ByteHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class second extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ActionBar ab;
    FloatingActionButton fabAttach, fabSend;
    EditText editText;
    LinearLayout scrollLL, msgTextLL, msgImageLL, msgFileLL;
    LinearLayout.LayoutParams msgTextInLParamsLL, msgTextOutLParamsLL, msgInLParamsTV, msgOutLParamsTV; //simple msg
    LinearLayout.LayoutParams msgLParamsIV, msgSenderLParamsTV; //image msg
    LinearLayout.LayoutParams msgFileLParamsIV, msgFileLParamsTV; //file msg
    TextView msgTV, senderTV, infoTV, msgFileTV;
    ImageView msgIV, msgFileIV;
    InputMethodManager imm;
    Intent intent;
    String MY_TAG_2="MY_TAG_2";
    SendMsg sendMsg;
    GetMsgs getMsgs;
    final int REQUEST_CODE_IMAGE = 1, REQUEST_CODE_AUDIO = 2, REQUEST_CODE_TEXT_FILE = 3;
    String uri, ip = "138.197.176.233";
    TCPClient client;
    byte[] bytes;
    byte[] nickname;
    byte b;
    byte space;
    String s;
    int k, port = 3129;
    byte codingType, compressionType;
    ProgressDialog pds;

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
        k = 0;

        client = new TCPClient(ip, port);

        scrollLL = (LinearLayout) findViewById(R.id.scrollLL);

        //simple msg
        msgTextLL = (LinearLayout) findViewById(R.id.msgTextInLL);
        msgTextInLParamsLL = (LinearLayout.LayoutParams) msgTextLL.getLayoutParams();
        msgTextLL = (LinearLayout) findViewById(R.id.msgTextOutLL);
        msgTextOutLParamsLL = (LinearLayout.LayoutParams) msgTextLL.getLayoutParams();
        msgTV = (TextView) findViewById(R.id.msgInTV);
        msgInLParamsTV = (LinearLayout.LayoutParams) msgTV.getLayoutParams();
        msgTV = (TextView) findViewById(R.id.msgOutTV);
        msgOutLParamsTV = (LinearLayout.LayoutParams) msgTV.getLayoutParams();

        //image msg
        msgIV = (ImageView) findViewById(R.id.msgIV);
        msgLParamsIV = (LinearLayout.LayoutParams) msgIV.getLayoutParams();
        senderTV = (TextView) findViewById(R.id.senderTV);
        msgSenderLParamsTV = (LinearLayout.LayoutParams) senderTV.getLayoutParams();

        //file msg
        msgFileIV = (ImageView) findViewById(R.id.msgFileIV);
        msgFileLParamsIV = (LinearLayout.LayoutParams) msgFileIV.getLayoutParams();
        msgFileTV = (TextView) findViewById(R.id.msgFileTV);
        msgFileLParamsTV = (LinearLayout.LayoutParams) msgFileTV.getLayoutParams();

        // keyboard
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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

        fabAttach = (FloatingActionButton) findViewById(R.id.fabAttach);
        fabSend = (FloatingActionButton) findViewById(R.id.fabSend);
        fabSend.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);

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
            case R.id.update:
                if (getMsgs == null || getMsgs.getStatus() != AsyncTask.Status.RUNNING) {
                    getMsgs = new GetMsgs();
                    getMsgs.execute(k);
                }
                return true;
            case R.id.clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Enter password for clear database from server:");
                EditText password = new EditText(this);
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                builder.setView(password);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (password.getText().toString().equals("wow")) {
                            ClearHistory clearHistory = new ClearHistory();
                            clearHistory.execute();
                            scrollLL.removeAllViews();
                        } else
                            Toast.makeText(second.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSend:
                sendMsg = new SendMsg((byte) 0);
                bytes = ByteHelper.getBytesFromString(editText.getText().toString());
                sendMsg.execute(bytes);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_attach, popup.getMenu());
        verifyStoragePermissions(second.this);
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

            case R.id.textFile:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*");

                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE_TEXT_FILE);
                }
                break;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            s = getFilePath(uri);
            byte type = (byte) 5;
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    type = (byte) 1;
                    break;
                case REQUEST_CODE_AUDIO:
                    type = (byte) 2;
                    break;
                case REQUEST_CODE_TEXT_FILE:
                    type = (byte) 3;
                    break;
            }
            sendMsg = new SendMsg(type, ByteHelper.getBytesFromString(getFileExtension(s)));
            try {
                sendMsg.execute(ByteHelper.readBytesFromFile(s));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileTooBigException e) {
                e.printStackTrace();
            }
        }
    }

    // метод возвращает полный реальный путь до файла, включая имя и расширение
    public String getFilePath(Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            } finally {
                if (cursor != null) cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    // метод возвращает из полного пути расширение файла
    public String getFileExtension(String path) {
        int pos = path.lastIndexOf(".");
        if (pos != -1) return path.substring(pos + 1);
        else return "";
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    View.OnClickListener onClickListenerIV = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getContentDescription().toString()));
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        //getting info from first activity to codingType, compressionType, nickname fields
        codingType = (byte) first.codingType;
        compressionType = (byte) first.compressionType;
        bytes = ByteHelper.getBytesFromString(first.nickname);
        space = ByteHelper.getBytesFromString(" ")[0];
        nickname = new byte[20];
        for (int i = 0; i < 20; i++) {
            nickname[i] = (bytes.length > i) ? bytes[i] : space;
        }

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
        byte msgType;
        ProgressDialog pds;
        byte[] msgExtension;

        SendMsg(byte msgType, byte[] msgExtension) {
            this.msgType = msgType;
            this.msgExtension = msgExtension;
        }

        SendMsg(byte msgType) {
            this.msgType = msgType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pds = new ProgressDialog(second.this);
            pds.setProgressStyle(R.style.AppCompatAlertDialogStyle); //Set style
            pds.setMessage("Sending message...");
            pds.setIndeterminate(true);
            pds.setCancelable(false);
            pds.show();
        }


        @Override
        protected Integer doInBackground(byte[]... bytes) {
            int response = 4;
            BitStream compressedBitStream = new BitStream();
            BitStream codedBitStream = new BitStream();
            BitStream outBitStream = new BitStream();
            outBitStream.addByte(msgType);
            outBitStream.addByte(codingType);
            outBitStream.addByte(compressionType);
            outBitStream.addByteArray(nickname);
            if (msgType != (byte) 0) {
                byte[] exten = new byte[20];
                for (int i = 0; i < 20; i++) {
                    exten[i] = (msgExtension.length > i) ? msgExtension[i] : space;
                }
                outBitStream.addByteArray(exten);
            }

            //compressing message
            Log.d(MY_TAG_2,"Compressing message");
            switch (compressionType) {
                case (byte) 0:
                    compressedBitStream = (new Huffman()).compressByteString(bytes[0]);
                    break;
                case (byte) 1:
                    compressedBitStream = (new LZ77()).compressByteString(bytes[0]);
                    break;
                case (byte) 2:
                    compressedBitStream = (new RLE()).compressByteString(bytes[0]);
                    break;
            }

            //coding message
            Log.d(MY_TAG_2,"Coding message");
            switch (codingType) {
                case (byte) 0:
                    codedBitStream = (new HammingCode()).encodeBitStream(compressedBitStream);
                    break;
                case (byte) 1:
                    try {
                        codedBitStream = (new ParityBit()).encodeBitStream(compressedBitStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case (byte) 2:
                    codedBitStream = (new RepetitionCode()).encodeBitStream(compressedBitStream);
                    break;
            }

            outBitStream.addByteArray(codedBitStream.toByteArray());
            byte[] block = outBitStream.toByteArray();
            //pds.setMessage("Sending on server...");
            // pds.setMessage("Sending message...");

            // Send bytes to server
            Log.d(MY_TAG_2,"Send bytes to server");
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
            pds.dismiss();
            Log.d(MY_TAG_2,"Message was send ");
        }
    }


    class GetMsgs extends AsyncTask<Integer, Void, byte[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pds = new ProgressDialog(second.this);
            pds.setProgressStyle(R.style.AppCompatAlertDialogStyle); //Set style
            pds.setMessage("Updating chat..."); //Message
            pds.setIndeterminate(true);
            pds.setCancelable(false);
            pds.show();
        }

        @Override
        protected byte[][] doInBackground(Integer... alreadyHaveMessages) {
            byte[][] result = new byte[0][];
            // Get messages
            Log.d(MY_TAG_2,"Getting bytes to server");
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
            if (bytes.length == 0) {
                pds.dismiss();
                return;
            }
            BitStream decompressedBitStream = new BitStream();
            BitStream decodedBitStream = new BitStream();

            for (int i = 0; i < bytes.length; i++) {
                String info = "Size[bytes] - Algorithm:\n";
                int delta = (bytes[i][0] == 0) ? 23 : 43;
                String path;
                byte[] exten = new byte[20];
                byte[] msg = new byte[bytes[i].length - delta];
                System.arraycopy(bytes[i], delta, msg, 0, msg.length);
                byte[] nickname = new byte[20];
                System.arraycopy(bytes[i], 3, nickname, 0, 20);

                //decoding messages
                Log.d(MY_TAG_2,"Decoding messages");
                info += msg.length;
                try {
                    switch (bytes[i][1]) {
                        case (byte) 0:
                            decodedBitStream.addByteArray((new HammingCode()).decodeByteString(msg));
                            info += "   HammingCode\n";
                            break;
                        case (byte) 1:
                            decodedBitStream.addByteArray((new ParityBit()).decodeByteString(msg));
                            info += "   ParityBit\n";
                            break;
                        case (byte) 2:
                            decodedBitStream.addByteArray((new RepetitionCode()).decodeByteString(msg));
                            info += "   RepetitionCode\n";
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //decompression messages
                Log.d(MY_TAG_2,"Decompression messages");
                info += decodedBitStream.size() / 8;
                try {
                    switch (bytes[i][2]) {
                        case (byte) 0:
                            decompressedBitStream = (new Huffman()).decompressBitStream(decodedBitStream);
                            info += "   Huffman\n";
                            break;
                        case (byte) 1:
                            decompressedBitStream = (new LZ77()).decompressBitStream(decodedBitStream);
                            info += "   LZ77\n";
                            break;
                        case (byte) 2:
                            decompressedBitStream = (new RLE()).decompressBitStream(decodedBitStream);
                            info += "   RLE\n";
                            break;
                    }
                } catch (DecompressionException e) {
                    e.printStackTrace();
                }
                info += decompressedBitStream.size() / 8 + "   Source";


                //Mapping file on screen
                Log.d(MY_TAG_2,"Mapping file on screen ");
                switch (bytes[i][0]) {
                    //simple text msg
                    case (byte) 0:
                        msgTextLL = new LinearLayout(second.this);
                        msgTextLL.setOrientation(LinearLayout.VERTICAL);
                        senderTV = new TextView(second.this);
                        msgTV = new TextView(second.this);
                        msgTV.setTextColor(getResources().getColor(R.color.black));
                        msgTV.setTextSize(getResources().getDimension(R.dimen.middleText));
                        infoTV = new TextView(second.this);

                        senderTV.setText("From:  " + ByteHelper.getStringFromBytes(nickname));
                        if (!ByteHelper.getStringFromBytes(nickname).replaceAll(" ", "").equals(ByteHelper.getStringFromBytes(second.this.nickname).replaceAll(" ", ""))) {
                            msgTextLL.setBackgroundResource(R.drawable.msg_in);
                            msgTextLL.setLayoutParams(msgTextInLParamsLL);
                            senderTV.setLayoutParams(msgInLParamsTV);
                            msgTV.setLayoutParams(msgInLParamsTV);
                            infoTV.setLayoutParams(msgInLParamsTV);
                        } else {
                            msgTextLL.setBackgroundResource(R.drawable.msg_out);
                            msgTextLL.setLayoutParams(msgTextOutLParamsLL);
                            senderTV.setLayoutParams(msgOutLParamsTV);
                            msgTV.setLayoutParams(msgOutLParamsTV);
                            infoTV.setLayoutParams(msgOutLParamsTV);
                        }

                        s = ByteHelper.getStringFromBytes(decompressedBitStream.toByteArray());
                        msgTV.setText(s);
                        infoTV.setText(info);

                        msgTextLL.addView(senderTV);
                        msgTextLL.addView(msgTV);
                        msgTextLL.addView(infoTV);
                        scrollLL.addView(msgTextLL);
                        break;

                    //image file
                    case (byte) 1:
                        msgImageLL = new LinearLayout(second.this);
                        msgImageLL.setBackgroundResource(R.drawable.msg_photo);
                        msgImageLL.setOrientation(LinearLayout.VERTICAL);
                        senderTV = new TextView(second.this);
                        msgIV = new ImageView(second.this);
                        infoTV = new TextView(second.this);

                        senderTV.setText("From:  " + ByteHelper.getStringFromBytes(nickname));
                        if (!ByteHelper.getStringFromBytes(nickname).replaceAll(" ", "").equals(ByteHelper.getStringFromBytes(second.this.nickname).replaceAll(" ", ""))) {
                            msgImageLL.setLayoutParams(msgTextInLParamsLL);
                        } else {
                            msgImageLL.setLayoutParams(msgTextOutLParamsLL);
                        }
                        senderTV.setLayoutParams(msgSenderLParamsTV);
                        msgIV.setLayoutParams(msgLParamsIV);
                        infoTV.setLayoutParams(msgSenderLParamsTV);

                        System.arraycopy(bytes[i], 23, exten, 0, 20);
                        path =  getApplicationInfo().dataDir
                                + "/f" + k + "." + ByteHelper.getStringFromBytes(exten);
                        try {
                            ByteHelper.writeBytesToFile(decompressedBitStream.toByteArray(), path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            uri = new File(path).toURI().toURL().toExternalForm();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        Picasso.with(second.this).load(Uri.parse(uri)).fit().into(msgIV);
                        //msgIV.setImageURI(Uri.parse(uri));

                        msgIV.setContentDescription(uri);
                        msgIV.setOnClickListener(onClickListenerIV);
                        infoTV.setText(info);

                        msgImageLL.addView(senderTV);
                        msgImageLL.addView(msgIV);
                        msgImageLL.addView(infoTV);
                        scrollLL.addView(msgImageLL);
                        break;

                    //other file
                    default:
                        msgFileLL = new LinearLayout(second.this);
                        msgFileLL.setBackgroundResource(R.drawable.msg_photo);
                        msgFileLL.setOrientation(LinearLayout.HORIZONTAL);
                        msgFileIV = new ImageView(second.this);
                        if (bytes[i][0] == (byte) 2) msgFileIV.setImageResource(R.mipmap.audio);
                        else msgFileIV.setImageResource(R.mipmap.text);
                        msgFileTV = new TextView(second.this);

                        msgFileTV.setText("From:  " + ByteHelper.getStringFromBytes(nickname) + "\n");
                        if (!ByteHelper.getStringFromBytes(nickname).replaceAll(" ", "").equals(ByteHelper.getStringFromBytes(second.this.nickname).replaceAll(" ", ""))) {
                            msgFileLL.setLayoutParams(msgTextInLParamsLL);
                        } else {
                            msgFileLL.setLayoutParams(msgTextOutLParamsLL);
                        }
                        msgFileIV.setLayoutParams(msgFileLParamsIV);
                        msgFileTV.setLayoutParams(msgFileLParamsTV);

                        System.arraycopy(bytes[i], 23, exten, 0, 20);
                        path = getApplicationInfo().dataDir + "/f" + k + "." + ByteHelper.getStringFromBytes(exten);
                        try {
                            ByteHelper.writeBytesToFile(decompressedBitStream.toByteArray(), path);     //TODO replace on decodedStream.toByteArray()
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            uri = new File(path).toURI().toURL().toExternalForm();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        msgFileIV.setContentDescription(uri);
                        msgFileIV.setOnClickListener(onClickListenerAV);
                        msgFileTV.setText(msgFileTV.getText() + "Type of file:  " + ByteHelper.getStringFromBytes(exten).replaceAll(" ", "") + "\n\n" + info);

                        msgFileLL.addView(msgFileIV);
                        msgFileLL.addView(msgFileTV);
                        scrollLL.addView(msgFileLL);
                        break;
                }
                k++;
            }
            pds.dismiss();
            Log.d(MY_TAG_2,"Messages were got ");
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


