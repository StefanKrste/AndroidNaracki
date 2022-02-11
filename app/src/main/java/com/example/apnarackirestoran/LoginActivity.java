package com.example.apnarackirestoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword;
    private final String UrlzemiAkaunt="http://192.168.100.5/Android%20app/login.php";
    private final String UrlPosledenPovrzan="http://192.168.100.5/Android%20app/zemiposledenobjekt.php";
    private final String UrlListaArtikli="http://192.168.100.5/Android%20app/listaartikli.php";
    private RequestQueue requestQueue;
    private Akaunt LogiranAcc = new Akaunt();
    private Objekt PosledenObjekt = new Objekt();
    private ArrayList<String> Artikli=new ArrayList<String>();
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        editTextUserName = (EditText) findViewById(R.id.LoginUsername);
        editTextPassword = (EditText) findViewById(R.id.LoginPassword);

        TextView txt = (TextView) findViewById(R.id.txtRegister);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void Login(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        Intent intentKeknerBezFirma = new Intent(this, Podesuvanja.class);
        Intent intentKelnerPocetna = new Intent(this, KelnerPocetnaActivity.class);

        if (username.equals("")) {
            editTextUserName.setError("Задолжително поле!");
            editTextUserName.requestFocus();
            return;
        }else if (password.equals("")) {
            editTextPassword.setError("Задолжително поле!");
            editTextPassword.requestFocus();
            return;
        } else {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject parametri = new JSONObject();
            try{
                parametri.put("username",username);
                parametri.put("password",password);
            }catch (JSONException e){
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlzemiAkaunt,parametri,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                    try {
                        response.getString("error");
                        Toast.makeText(LoginActivity.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                        return;
                    } catch (JSONException e) {
                    }
                    try {
                        response.getString("greska");
                        Toast.makeText(LoginActivity.this, "Неуспешна најава.Погрешно корисничко име или лозинка!", Toast.LENGTH_SHORT).show();
                        return;
                    } catch (JSONException e) {
                    }
                    try {
                        JSONObject akaunt = response.getJSONArray("akaunt").getJSONObject(0);

                        LogiranAcc.setUsername(akaunt.getString("username"));
                        LogiranAcc.setPassword(akaunt.getString("password"));
                        LogiranAcc.setMail(akaunt.getString("mail"));
                        LogiranAcc.setIme(akaunt.getString("ime"));
                        LogiranAcc.setPrezime(akaunt.getString("prezime"));
                        LogiranAcc.setTel(akaunt.getString("tel"));
                        LogiranAcc.setIdFirma(akaunt.getString("id_firma"));
                        LogiranAcc.setPosledenObjekt(akaunt.getString("posleden_objekt"));
                        LogiranAcc.setOdMasa(akaunt.getString("od_masa"));
                        LogiranAcc.setDoMasa(akaunt.getString("do_masa"));

                        Gson gson = new Gson();
                        String logAkaunt = gson.toJson(LogiranAcc);
                        SharedPreferences prefs = getSharedPreferences("Storage", MODE_MULTI_PROCESS);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("akaunt", logAkaunt);
                        editor.apply();

                        if (!LogiranAcc.getPosledenObjekt().equals("") && !LogiranAcc.getIdFirma().equals("")) {
                            ZemiPosledenObjekt(LogiranAcc.getIdFirma().trim(), LogiranAcc.getPosledenObjekt().trim());
                            KreirajListaArtikli(LogiranAcc.getIdFirma().trim());
                        } else {
                            PosledenObjekt = new Objekt(0, "", 0, 0, "");
                            String objekt = gson.toJson(PosledenObjekt);
                            editor.putString("objekt", objekt);
                            editor.apply();
                        }

                        if (!LogiranAcc.getIdFirma().equals("")) {
                            startActivity(intentKelnerPocetna);
                        } else {
                            startActivity(intentKeknerBezFirma);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Настана некаков проблем! Обидете се повторно", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void ZemiPosledenObjekt(String idFirma, String SifraObjekt){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject parametri = new JSONObject();
        try{
            parametri.put("idfirma",idFirma);
            parametri.put("objekt",SifraObjekt);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlPosledenPovrzan,parametri,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    response.getString("error");
                    Toast.makeText(LoginActivity.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    response.getString("greska");
                    Toast.makeText(LoginActivity.this, "Неуспешно пронаоѓање на објект!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    JSONObject objekt = response.getJSONArray("objekt").getJSONObject(0);

                    PosledenObjekt.setSifra(objekt.getInt("id"));
                    PosledenObjekt.setNazivNaobjekt(objekt.getString("naziv_na_objekt"));
                    PosledenObjekt.setNegativnaKol(objekt.getInt("negativna_kol"));
                    PosledenObjekt.setBrojMasi(objekt.getInt("broj_masi"));
                    PosledenObjekt.setPovrzanMag(objekt.getString("povrzan_magacin"));

                    Gson gson = new Gson();
                    String objektJ = gson.toJson(PosledenObjekt);
                    SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("objekt", objektJ);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Неможе да го пронајдеме Објектот!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void Register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void KreirajListaArtikli(String idFirma){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject parametri = new JSONObject();
        try{
            parametri.put("idfirma",idFirma);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlListaArtikli,parametri,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    response.getString("error");
                    Toast.makeText(LoginActivity.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    response.getString("greska");
                    Toast.makeText(LoginActivity.this, "Објектот нема артикли!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    JSONArray artikli = response.getJSONArray("artikli");
                    for (int i = 0; i < artikli.length(); i++) {
                        JSONObject artiklJ = artikli.getJSONObject(i);

                        Artikli.add(artiklJ.getString("sifra_na_artikl")+" - "+artiklJ.getString("naziv_na_atikl"));
                    }

                    Gson gson = new Gson();
                    String listaArtikli = gson.toJson(Artikli);
                    SharedPreferences prefs = getSharedPreferences("Storage", MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("listaArtikli", listaArtikli);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Неможе да го пронајдеме Објектот!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}



