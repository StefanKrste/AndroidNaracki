package com.example.apnarackirestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class KelnerPocetnaActivity extends AppCompatActivity {

    private Akaunt logiranAcc = new Akaunt();
    private Objekt PosledenObjekt = new Objekt();
    private LinearLayout linearLayoutMasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelner_pocetna);

        linearLayoutMasi = (LinearLayout) findViewById(R.id.masiLayout);

        SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = prefs.getString("akaunt","");
        logiranAcc = gson.fromJson(json, Akaunt.class);
        String json1 = prefs.getString("objekt","");
        PosledenObjekt = gson.fromJson(json1, Objekt.class);

        if(!logiranAcc.getPosledenObjekt().equals("")) {
            for (int i = Integer.parseInt(logiranAcc.getOdMasa()) - 1; i < Integer.parseInt(logiranAcc.getDoMasa()); i++) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                linearLayout.setLayoutParams(param);
                for (int k = 0; k <= 1; k++) {
                    TextView textView = new TextView(this);
                    float height = getResources().getDimension(R.dimen.heightMasa);
                    LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, (int) height, 0.5f);
                    txtParams.setMargins(10, 0, 10, 0);
                    textView.setLayoutParams(txtParams);
                    if (i + k < Integer.parseInt(logiranAcc.getDoMasa())) {
                        textView.setText(String.valueOf(i + k + 1));
                        textView.setGravity(Gravity.CENTER);
                        textView.setBackgroundResource(R.drawable.masa1);
                        textView.setTextSize(100);
                        textView.setTextColor(Color.rgb(0, 0, 0));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent IntentSmetka = new Intent(KelnerPocetnaActivity.this, SmetkaActivity.class);
                                IntentSmetka.putExtra("brMasa", textView.getText());
                                startActivity(IntentSmetka);
                            }
                        });
                    }
                    linearLayout.addView(textView);
                }
                linearLayoutMasi.addView(linearLayout);
                i++;
            }
        }

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_masi, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_podesi:
                Intent intentKelnerPovrzi = new Intent(this, Podesuvanja.class);
                startActivity(intentKelnerPovrzi);
                return true;
            case R.id.action_signout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Одјави се");
                builder.setMessage("Дали сте сигурни дека сакате да се одјавите?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(KelnerPocetnaActivity.this, LoginActivity.class));
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
            case R.id.action_accinfo:
                Intent AccInfo = new Intent(this, AccInfoActivity.class);
                startActivity(AccInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}