package com.example.apnarackirestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Podesuvanja extends AppCompatActivity {

    private EditText editTextKluc , odMasa , doMasa;
    private TextView txtPovrzanaFirma , txtCrta , txtObsegMasi;
    private Button btnPovrzi , btnPrekiniVrska , btnObnoviLista;
    public Button buttonDropDown;
    private final String UrlzemiObjekti="http://192.168.100.5/Android%20app/zemiobjekti.php";
    private final String Urlpovrzi="http://192.168.100.5/Android%20app/povrzi.php";
    private final String UrlNazivFirma="http://192.168.100.5/Android%20app/nazivfirma.php";
    private final String UrlPostaviObjekt="http://192.168.100.5/Android%20app/postaviobjekt.php";
    private final String UrlPrekinivrska="http://192.168.100.5/Android%20app/prekinivrska.php";
    private final String UrlListaArtikli="http://192.168.100.5/Android%20app/listaartikli.php";
    private final String UrlDoMasa="http://192.168.100.5/Android%20app/domasa.php";
    private final String UrlOdMasa="http://192.168.100.5/Android%20app/odmasa.php";
    private RequestQueue requestQueue;
    private Akaunt logiranAcc = new Akaunt();
    private Objekt PosledenObjekt = new Objekt();
    private long PosledenRestart = 0;

    private List<String> listNazivObjekti=new ArrayList<String>();
    private List<Objekt> listObjekti=new ArrayList<Objekt>();
    private ArrayList<String> Artikli=new ArrayList<String>();

    String popUpContents[];
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesuvanja);

        editTextKluc = (EditText) findViewById(R.id.PovrziSeKluc);
        odMasa = (EditText) findViewById(R.id.OdMasa);
        doMasa = (EditText) findViewById(R.id.DoMasa);
        txtPovrzanaFirma = (TextView) findViewById(R.id.txtPvrzanaFirma);
        txtCrta = (TextView) findViewById(R.id.txtCrtica);
        txtObsegMasi = (TextView) findViewById(R.id.txtObsegMasi);
        btnObnoviLista = (Button) findViewById(R.id.buttonObnoviListaArtikli);
        btnPovrzi = (Button) findViewById(R.id.buttonPovrzi);
        btnPrekiniVrska = (Button) findViewById(R.id.buttonPrekiniVrska);
        buttonDropDown = (Button) findViewById(R.id.buttonShowDropDown);

        SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = prefs.getString("akaunt","");
        logiranAcc = gson.fromJson(json, Akaunt.class);
        String json1 = prefs.getString("objekt","");
        PosledenObjekt = gson.fromJson(json1, Objekt.class);

        if(!logiranAcc.getIdFirma().equals("")){
            NapraviListiIObjekti();
            btnPovrzi.setVisibility(View.INVISIBLE);
            editTextKluc.setVisibility(View.INVISIBLE);
            if(!logiranAcc.getPosledenObjekt().equals("")){
                buttonDropDown.setText(PosledenObjekt.getNazivNaobjekt());
            }
            if(!logiranAcc.getOdMasa().equals("")){
                odMasa.setText(logiranAcc.getOdMasa());
            }
            if(!logiranAcc.getDoMasa().equals("")){
                doMasa.setText(logiranAcc.getDoMasa());
            }
        }else{
            btnPrekiniVrska.setVisibility(View.INVISIBLE);
            buttonDropDown.setVisibility(View.INVISIBLE);
            txtCrta.setVisibility(View.INVISIBLE);
            txtObsegMasi.setVisibility(View.INVISIBLE);
            odMasa.setVisibility(View.INVISIBLE);
            doMasa.setVisibility(View.INVISIBLE);
            btnObnoviLista.setVisibility(View.INVISIBLE);
        }

        odMasa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus){
                if (!v.hasFocus()) {
                    if(Integer.parseInt(odMasa.getText().toString())<1){
                        odMasa.setText("1");
                    }else if (Integer.parseInt(odMasa.getText().toString())>PosledenObjekt.getBrojMasi()){
                        odMasa.setText(PosledenObjekt.getBrojMasi());
                    }
                    PostaviOdMasi(logiranAcc.getUsername(),odMasa.getText().toString());
                }
            }
        });

        doMasa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus){
                if (!v.hasFocus()) {
                    if(Integer.parseInt(doMasa.getText().toString())<1){
                        doMasa.setText("1");
                    }else if (Integer.parseInt(doMasa.getText().toString())>PosledenObjekt.getBrojMasi()){
                        doMasa.setText(PosledenObjekt.getBrojMasi().toString());
                    }
                    PostaviDoMasi(logiranAcc.getUsername(),doMasa.getText().toString());
                }
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
    }

    public PopupWindow popupWindow() {
        ListView listView = new ListView(this);
        listView.setAdapter(Adapter(popUpContents));
        listView.setOnItemClickListener(new DropdownOnItemClickListener());

        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(listView);

        return popupWindow;
    }

    class DropdownOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
            Context mContext = v.getContext();
            Podesuvanja podesuvanja = ((Podesuvanja) mContext);

            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);

            podesuvanja.popupWindow.dismiss();

            String selectedItemText = ((TextView) v).getText().toString();
            podesuvanja.buttonDropDown.setText(selectedItemText);
            postaviObjektvoSQL(selectedItemText);
        }
    }

    private void postaviObjektvoSQL(String selectedItem){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "objekt";
                String[] data = new String[2];
                int sifraObjekt = 0 ;
                for(int i=0;i<listObjekti.size();i++){
                    if(selectedItem.equals(listObjekti.get(i).getNazivNaobjekt())){
                        sifraObjekt=listObjekti.get(i).getSifra();
                        break;
                    }
                }
                data[0] = logiranAcc.getUsername();
                data[1] = String.valueOf(sifraObjekt);
                PutData putData = new PutData(UrlPostaviObjekt, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        System.out.println(result);
                        if (result.equals("1")) {

                            logiranAcc.setPosledenObjekt(selectedItem);
                            Gson gson = new Gson();
                            String logAkaunt = gson.toJson(logiranAcc);
                            SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("akaunt", logAkaunt);
                            editor.apply();

                            for(int i=0; i<listObjekti.size();i++){
                                if(selectedItem.equals(listObjekti.get(i).getNazivNaobjekt())){
                                    PosledenObjekt=listObjekti.get(i);
                                    PostaviDoMasi(logiranAcc.getUsername(),PosledenObjekt.getBrojMasi().toString());
                                    doMasa.setText(PosledenObjekt.getBrojMasi().toString());
                                    PostaviOdMasi(logiranAcc.getUsername(),"1");
                                    odMasa.setText("1");
                                    String objekt = gson.toJson(PosledenObjekt);
                                    editor.putString("objekt", objekt);
                                    editor.apply();
                                }
                            }

                            Toast.makeText(Podesuvanja.this, "Успешно избравте објект!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Podesuvanja.this, "Настана грешка!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private ArrayAdapter<String> Adapter(String Array[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Array) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView listItem = new TextView(Podesuvanja.this);
                listItem.setText(getItem(position));
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_povrzi, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_masi:
                IzbrisiFokus();
                Intent intentKelnerPocetna = new Intent(this, KelnerPocetnaActivity.class);
                startActivity(intentKelnerPocetna);
                return true;
            case R.id.action_signout:
                IzbrisiFokus();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Одјави се");
                builder.setMessage("Дали сте сигурни дека сакате да се одјавите?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Podesuvanja.this, LoginActivity.class));
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
                IzbrisiFokus();
                Intent AccInfo = new Intent(this, AccInfoActivity.class);
                startActivity(AccInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Povrzi(View view) {
        String kluc = editTextKluc.getText().toString().trim();
        if (kluc.equals("")) {
            editTextKluc.setError("Задолжително поле!");
            editTextKluc.requestFocus();
            return;
        } else {
            editTextKluc.setText("");
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = "username";
                    field[1] = "kod_povrzuvanje";
                    String[] data = new String[2];
                    data[0] = logiranAcc.getUsername();
                    data[1] = kluc;
                    PutData putData = new PutData(Urlpovrzi, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            System.out.println(result);
                            if (!result.equals("0") && !result.equals("Error: Database connection")) {

                                logiranAcc.setIdFirma(result);
                                Gson gson = new Gson();
                                String logAkaunt = gson.toJson(logiranAcc);
                                SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("akaunt", logAkaunt);
                                editor.apply();

                                NapraviListiIObjekti();

                                btnPovrzi.setVisibility(View.INVISIBLE);
                                editTextKluc.setVisibility(View.INVISIBLE);
                                btnPrekiniVrska.setVisibility(View.VISIBLE);
                                buttonDropDown.setVisibility(View.VISIBLE);
                                txtCrta.setVisibility(View.VISIBLE);
                                txtObsegMasi.setVisibility(View.VISIBLE);
                                odMasa.setVisibility(View.VISIBLE);
                                doMasa.setVisibility(View.VISIBLE);
                                btnObnoviLista.setVisibility(View.VISIBLE);


                                Toast.makeText(Podesuvanja.this, "Успешно поврзување!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Podesuvanja.this, "Неуспешно поврзување!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    private void PostaviLista(){
        popUpContents = new String[listNazivObjekti.size()];
        listNazivObjekti.toArray(popUpContents);
        popupWindow = popupWindow();
    }

    private void NapraviListiIObjekti(){
        JSONObject parametri = new JSONObject();
        try{
            parametri.put("idFirma",logiranAcc.getIdFirma());
        }catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        listObjekti.clear();
        listNazivObjekti.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlzemiObjekti,parametri,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    response.getString("error");
                    Toast.makeText(Podesuvanja.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    response.getString("greska");
                    Toast.makeText(Podesuvanja.this, "Фирмата нема објекти!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    JSONArray Jobjekti = response.getJSONArray("objekti");
                    for (int i = 0; i < Jobjekti.length(); i++) {
                        Objekt objekt = new Objekt(0,"",0,0,"");
                        JSONObject Jobjekt = Jobjekti.getJSONObject(i);

                        String sifra = Jobjekt.getString("id");
                        objekt.setSifra(Integer.parseInt(sifra));
                        String naziv = Jobjekt.getString("naziv_na_objekt");
                        listNazivObjekti.add(naziv);
                        objekt.setNazivNaobjekt(naziv);
                        String negativna = Jobjekt.getString("negativna_kol");
                        objekt.setNegativnaKol(Integer.parseInt(negativna));
                        String brojMasi = Jobjekt.getString("broj_masi");
                        objekt.setBrojMasi(Integer.parseInt(brojMasi));
                        String PovMagacin = Jobjekt.getString("povrzan_magacin");
                        objekt.setPovrzanMag(PovMagacin);
                        listObjekti.add(objekt);
                    }
                    PostaviLista();
                    NazivNaFirma();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("problem");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void PrekiniVrska(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Потврда");
        builder.setMessage("Дали сте сигурни дека сакате да ја прекинете врската со фирмата?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PrekiniVrskaSQL();
                txtPovrzanaFirma.setText("");
                odMasa.setText("");
                doMasa.setText("");
                buttonDropDown.setText("Изберете објект");
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
    }

    private void PrekiniVrskaSQL(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "username";
                String[] data = new String[1];
                data[0] = logiranAcc.getUsername();
                PutData putData = new PutData(UrlPrekinivrska, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        System.out.println(result);
                        if (result.equals("1")) {

                            logiranAcc.setIdFirma("");
                            logiranAcc.setPosledenObjekt("");
                            Gson gson = new Gson();
                            String logAkaunt = gson.toJson(logiranAcc);
                            SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("akaunt", logAkaunt);
                            editor.apply();

                            PosledenObjekt = new Objekt(0,"",0,0,"");
                            String objekt = gson.toJson(PosledenObjekt);
                            editor.putString("objekt", objekt);
                            editor.apply();

                            btnPovrzi.setVisibility(View.VISIBLE);
                            editTextKluc.setVisibility(View.VISIBLE);
                            btnPrekiniVrska.setVisibility(View.INVISIBLE);
                            buttonDropDown.setVisibility(View.INVISIBLE);
                            txtCrta.setVisibility(View.INVISIBLE);
                            txtObsegMasi.setVisibility(View.INVISIBLE);
                            odMasa.setVisibility(View.INVISIBLE);
                            doMasa.setVisibility(View.INVISIBLE);
                            btnObnoviLista.setVisibility(View.INVISIBLE);

                            listNazivObjekti.clear();
                            listObjekti.clear();

                            txtPovrzanaFirma.setText("");
                        }
                    }
                }
            }
        });
    }

    private void NazivNaFirma(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "idFirma";
                String[] data = new String[1];
                data[0] = logiranAcc.getIdFirma();
                PutData putData = new PutData(UrlNazivFirma, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        System.out.println(result);
                        if (!result.equals("0") && !result.equals("Error: Database connection")) {

                            //SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                            //SharedPreferences.Editor editor = prefs.edit();
                            //editor.putString("NazivFirma", result);
                            //editor.apply();

                            txtPovrzanaFirma.setText(result);
                        }
                    }
                }
            }
        });
    }

    public void PokaziLista(View view) {
        popupWindow.showAsDropDown(view, -5, 0);
    }

    public void ObnoviListaArtikli(View view) {
        if (SystemClock.elapsedRealtime() - PosledenRestart < 5000){
            Toast.makeText(Podesuvanja.this, "Веќе ја обновивте листата следна обнова ќе може да направите за 5 секунди", Toast.LENGTH_SHORT).show();
            return;
        }
        PosledenRestart = SystemClock.elapsedRealtime();
        Artikli.clear();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject parametri = new JSONObject();
        try{
            parametri.put("idfirma",logiranAcc.getIdFirma());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlListaArtikli,parametri,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    response.getString("error");
                    Toast.makeText(Podesuvanja.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    response.getString("greska");
                    Toast.makeText(Podesuvanja.this, "Објектот нема артикли!", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(Podesuvanja.this, "Успешно ја обновивте листата на артикли!", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Podesuvanja.this, "Неможе да го пронајдеме Објектот!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void IzbrisiFokus() {
        odMasa.clearFocus();
        doMasa.clearFocus();
    }

    private void PostaviDoMasi(String Username, String doMasi){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "do_masa";
                String[] data = new String[2];
                data[0] = Username;
                data[1] = doMasi;
                PutData putData = new PutData(UrlDoMasa, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("1")) {
                            logiranAcc.setDoMasa(doMasa.getText().toString());
                            Gson gson = new Gson();
                            String logAkaunt = gson.toJson(logiranAcc);
                            SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("akaunt", logAkaunt);
                            editor.apply();
                        }else{
                            Toast.makeText(Podesuvanja.this, "Обидете се повторно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void PostaviOdMasi(String Username, String odMasi){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "od_masa";
                String[] data = new String[2];
                data[0] = Username;
                data[1] = odMasi;
                PutData putData = new PutData(UrlOdMasa, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("1")) {
                            logiranAcc.setOdMasa(odMasa.getText().toString());
                            Gson gson = new Gson();
                            String logAkaunt = gson.toJson(logiranAcc);
                            SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("akaunt", logAkaunt);
                            editor.apply();
                        }else{
                            Toast.makeText(Podesuvanja.this, "Обидете се повторно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
