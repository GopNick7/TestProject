package com.justappp.nekit.newtest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText edtEnterPhoneNumber, edtEnterCode;
    TextView txtPhone;
    ImageView imgContext;
    Button btnGetCode, btnConfirm;

    String resultString = null;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imgContext = (ImageView) findViewById(R.id.img_context);
        registerForContextMenu(imgContext);

        edtEnterPhoneNumber = (EditText) findViewById(R.id.edt_enter_phone_number);
        edtEnterPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 12) {
                    btnGetCode.setEnabled(true);
                    btnGetCode.setBackgroundResource(R.drawable.btn_style_active);
                }
            }
        });

        txtPhone = (TextView) findViewById(R.id.txt_phone);

        btnGetCode = (Button) findViewById(R.id.btn_get_code);
        btnGetCode.setEnabled(false);
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(MainActivity.this, R.string.notification, Toast.LENGTH_SHORT);
                toast.setGravity(0, 0, -250);
                toast.show();
                edtEnterCode.setEnabled(true);

                String myURL = "http://s3.logist.ua/testdata/data.php";
                String params = edtEnterPhoneNumber.getText().toString();
                byte[] data = null;
                InputStream is = null;

                try {
                    URL url = new URL(myURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    conn.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
                    OutputStream os = conn.getOutputStream();
                    data = params.getBytes("UTF-8");
                    os.write(data);

                    conn.connect();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    is = conn.getInputStream();

                    byte[] buffer = new byte[8192];

                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    data = baos.toByteArray();
                    resultString = new String(data, "UTF-8");

                } catch (MalformedURLException e) {

                    resultString = "MalformedURLException сообщение : " + e.getMessage();
                } catch (IOException e) {

                    resultString = "IOException сообщение : " + e.getMessage();
                } catch (Exception e) {

                    resultString = "Exception сообщение : " + e.getMessage();
                }

                toast = Toast.makeText(MainActivity.this, resultString, Toast.LENGTH_SHORT);
                toast.setGravity(0, 0, -250);
                toast.show();
            }
        });

        edtEnterCode = (EditText) findViewById(R.id.edt_enter_code);
        edtEnterCode.setEnabled(false);
        edtEnterCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setBackgroundResource(R.drawable.btn_style_active);
                }
            }
        });

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setEnabled(false);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUriString = "geo:50.44,30.57?z=5";
                Uri geoUri = Uri.parse(geoUriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
                startActivity(mapIntent);
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 0, Menu.NONE, "Украина +38");
        menu.add(Menu.NONE, 1, Menu.NONE, "Россия +7");
        menu.add(Menu.NONE, 2, Menu.NONE, "США +1");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                edtEnterPhoneNumber.setText("+38");
                txtPhone.setText("+38 0ХХ ХХХ ХХ ХХ");
                break;
            case 1:
                edtEnterPhoneNumber.setText("+7");
                txtPhone.setText("+7 XХХ ХХХ ХХ ХХ");
                break;
            case 2:
                edtEnterPhoneNumber.setText("+1");
                txtPhone.setText("+1 XХХ ХХХ ХХ ХХ");
                break;
            default:
                return super.onContextItemSelected(item);
        }
        edtEnterPhoneNumber.setSelection(edtEnterPhoneNumber.getText().length());
        return true;
    }
}