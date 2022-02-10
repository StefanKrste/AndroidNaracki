package com.example.apnarackirestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AccInfoActivity extends AppCompatActivity {

    private TextView txtIme,txtPrezime,txtUsername,txtMail;
    private EditText Tel;
    private Akaunt logiranAcc = new Akaunt();
    private final String UrlPromeniTel= "http://192.168.100.5/Android%20app/promenitel.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_info);

        SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = prefs.getString("akaunt","");
        logiranAcc = gson.fromJson(json, Akaunt.class);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        txtIme = (TextView) findViewById(R.id.InfoName);
        txtIme.setText(" "+logiranAcc.getIme());
        txtPrezime = (TextView) findViewById(R.id.InfoSurname);
        txtPrezime.setText(" "+logiranAcc.getPrezime());
        txtUsername = (TextView) findViewById(R.id.InfoUserName);
        txtUsername.setText(" "+logiranAcc.getUsername());
        txtMail = (TextView) findViewById(R.id.InfoEmail);
        txtMail.setText(" "+logiranAcc.getMail());
        Tel = (EditText) findViewById(R.id.InfoPhone);
        Tel.setText(logiranAcc.getTel());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_accinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_masi:
                Intent intentKelnerPocetna = new Intent(this, KelnerPocetnaActivity.class);
                startActivity(intentKelnerPocetna);
                return true;
            case R.id.action_signout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Одјави се");
                builder.setMessage("Дали сте сигурни дека сакате да се одјавите?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AccInfoActivity.this, LoginActivity.class));
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Не", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_podesi:
                Intent IntentPovrzi = new Intent(this, Podesuvanja.class);
                startActivity(IntentPovrzi);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Zacuvaj(View view) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "tel";
                String[] data = new String[2];
                data[0] = logiranAcc.getUsername();
                data[1] = Tel.getText().toString();
                PutData putData = new PutData(UrlPromeniTel, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("1")) {
                            logiranAcc.setTel(Tel.getText().toString());
                            Gson gson = new Gson();
                            String logAkaunt = gson.toJson(logiranAcc);
                            SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("akaunt", logAkaunt);
                            editor.apply();
                            Toast.makeText(AccInfoActivity.this, "Успешно го променивте телефонскиот број!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AccInfoActivity.this, "Обидете се повторно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void PromeniLozinka(View view) {
        Intent IntentPromenaNaLozinka = new Intent(AccInfoActivity.this, PromeniLozinkaActivity.class);
        startActivity(IntentPromenaNaLozinka);
    }
}