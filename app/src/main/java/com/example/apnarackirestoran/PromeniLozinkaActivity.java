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
import android.widget.Toast;

import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class PromeniLozinkaActivity extends AppCompatActivity {

    private EditText editstaraLozinak, editnovaLozinka, editnovaLozinka1;
    private Akaunt logiranAcc = new Akaunt();
    private final String UrlPromeniPass="http://192.168.100.5/Android%20app/promenipass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promeni_lozinka);

        editstaraLozinak = (EditText) findViewById(R.id.PLStaraLozinka);
        editnovaLozinka = (EditText) findViewById(R.id.PLNovaLozinka);
        editnovaLozinka1 = (EditText) findViewById(R.id.PLNovaLozinka1);

        SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = prefs.getString("akaunt","");
        logiranAcc = gson.fromJson(json, Akaunt.class);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        startActivity(new Intent(PromeniLozinkaActivity.this, LoginActivity.class));
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

    public void Promeni(View view) {
        String staraLozinka = editstaraLozinak.getText().toString().trim();
        String NovaLozinka = editnovaLozinka.getText().toString().trim();
        String NovaLozinka1 = editnovaLozinka1.getText().toString().trim();
        if (staraLozinka.equals("")) {
            editstaraLozinak.setError("Задолжително поле!");
            editstaraLozinak.requestFocus();
            return;
        }else if (NovaLozinka.equals("")) {
            editnovaLozinka.setError("Задолжително поле!");
            editnovaLozinka.requestFocus();
            return;
        }else if(NovaLozinka.length() < 6) {
            editnovaLozinka.setError("Внесете повеќе од 6 карактери!");
            editnovaLozinka.requestFocus();
            return;
        }else if (NovaLozinka1.equals("")) {
            editnovaLozinka1.setError("Задолжително поле!");
            editnovaLozinka1.requestFocus();
            return;
        }else if(!NovaLozinka.equals(NovaLozinka1)){
            Toast.makeText(PromeniLozinkaActivity.this, "Ново внесената лозинка не се софпаѓа!", Toast.LENGTH_SHORT).show();
            editnovaLozinka.setError("!");
            editnovaLozinka.requestFocus();
            return;
        }else if(!staraLozinka.equals(logiranAcc.getPassword())){
            Toast.makeText(PromeniLozinkaActivity.this, "Внесената лознинка е погрешна!", Toast.LENGTH_SHORT).show();
            editstaraLozinak.setError("!");
            editstaraLozinak.requestFocus();
            return;
        }else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = "username";
                    field[1] = "novPas";
                    String[] data = new String[2];
                    data[0] = logiranAcc.getUsername();
                    data[1] = editnovaLozinka.getText().toString();
                    PutData putData = new PutData(UrlPromeniPass, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("1")) {
                                logiranAcc.setPassword(editnovaLozinka.getText().toString());
                                Gson gson = new Gson();
                                String logAkaunt = gson.toJson(logiranAcc);
                                SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("akaunt", logAkaunt);
                                editor.apply();
                                editstaraLozinak.setText("");
                                editnovaLozinka.setText("");
                                editnovaLozinka1.setText("");
                                Toast.makeText(PromeniLozinkaActivity.this, "Успешно ја променивте лозинката!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(PromeniLozinkaActivity.this, "Обидете се повторно!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }
}