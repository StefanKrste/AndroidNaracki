package com.example.apnarackirestoran;

import static com.example.apnarackirestoran.R.color.white;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class SmetkaActivity extends AppCompatActivity {

    private TextView brMasi, Vkupno;
    private TableLayout Tabela;
    private LinkedList<Artikl> MasaArtikli=new LinkedList<Artikl>();
    private ArrayList<String> Artikli=new ArrayList<String>();
    private Objekt PosledenObjekt = new Objekt();
    private Akaunt logiranAcc = new Akaunt();
    private RequestQueue requestQueue;
    private final String UrlZemiArtikli="http://192.168.100.5/Android%20app/zemiartikl.php";
    private final String UrlIzbrisiArtikli="http://192.168.100.5/Android%20app/izbrisiartikl.php";
    private final String UrlZemiMasaArtikli="http://192.168.100.5/Android%20app/zemimasaartikli.php";
    private final String UrlIsprintajSmetka="http://192.168.100.5/Android%20app/isprintajsmetka.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetka);

        brMasi = (TextView) findViewById(R.id.brMasi);
        Tabela = (TableLayout) findViewById(R.id.table);
        Vkupno = (TextView) findViewById(R.id.Vkupno);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        brMasi.setText(extras.getString("brMasa"));

        SharedPreferences prefs = getSharedPreferences("Storage" , MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = prefs.getString("listaArtikli","");
        Artikli = gson.fromJson(json, ArrayList.class);
        String json1 = prefs.getString("objekt","");
        PosledenObjekt = gson.fromJson(json1, Objekt.class);
        String json2 = prefs.getString("akaunt","");
        logiranAcc = gson.fromJson(json2, Akaunt.class);

        ProcitajLista ();
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
                        startActivity(new Intent(SmetkaActivity.this, LoginActivity.class));
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

    public void DosadiArtikl(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //layout.setBackgroundColor(Color.argb(255, 255, 255, 255));

        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT );
        txtParams.setMargins(10,25,10,10);

        TextView txtNaziv = new TextView(this);
        txtNaziv.setText("Внесете назив или шифра:");
        txtNaziv.setTextSize(17);
        txtNaziv.setLayoutParams(txtParams);
        txtNaziv.setTextColor(Color.rgb(0, 0, 0));
        layout.addView(txtNaziv);

        AutoCompleteTextView acTextView = new AutoCompleteTextView(this);
        layout.addView(acTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, Artikli);
        acTextView.setThreshold(1);
        acTextView.setSingleLine(true);
        acTextView.setAdapter(adapter);

        TextView txtKol = new TextView(this);
        txtKol.setText("Внесете количина:");
        txtKol.setTextSize(17);
        txtKol.setLayoutParams(txtParams);
        txtKol.setTextColor(Color.rgb(0, 0, 0));
        layout.addView(txtKol);

        EditText kolicina = new EditText(this);
        kolicina.setInputType(InputType.TYPE_CLASS_NUMBER);
        kolicina.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.5f));
        kolicina.setText("1");
        kolicina.setSelectAllOnFocus(true);
        kolicina.setMaxLines(1);
        layout.addView(kolicina);

        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                kolicina.requestFocus();
            }
        });

        acTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus){
                v.post(new Runnable(){
                    @Override
                    public void run(){
                        if (!v.hasFocus()) {
                            int Pobaraj = 0;
                            for(int i=0;i<Artikli.size();i++){
                                if(acTextView.getText().toString().trim().equals(Artikli.get(i))){
                                    Pobaraj=1;
                                    break;
                                }
                            }
                            if(Pobaraj==0){
                                acTextView.setText("");
                                acTextView.requestFocus();
                                acTextView.setError("Внесете валиден влез!");
                            }
                        }
                    }
                });
            }
        });

        builder.setView(layout);
        builder.setPositiveButton("Додади", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("Исклучи", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        //Override na positive button za da ne se gasi avtomatski dokolku de se popolneti polinjata
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                if (!kolicina.getText().toString().trim().equals("") && !acTextView.getText().toString().trim().equals("")) {
                    int Pobaraj = 0;
                    for(int i=0;i<Artikli.size();i++){
                        if(acTextView.getText().toString().trim().equals(Artikli.get(i))){
                            Pobaraj=1;
                            break;
                        }
                    }
                    if(Pobaraj==0){
                        acTextView.setText("");
                        acTextView.setError("Внесете валиден влез!");
                        acTextView.requestFocus();
                    }else {
                        String splitted[] = acTextView.getText().toString().split("-");
                        String SifraArtikl = splitted[0];
                        for(int i=1;i<splitted.length-1;i++){
                            SifraArtikl = (SifraArtikl+"-"+splitted[i]);
                        }
                        SifraArtikl=SifraArtikl.trim();

                        ZemiArtiklOdBaza(SifraArtikl,kolicina.getText().toString(),PosledenObjekt.getNegativnaKol());

                        wantToCloseDialog = true;
                    }
                }
                else {
                    if (acTextView.getText().toString().trim().equals("")){
                        acTextView.requestFocus();
                        acTextView.setError("Задолжително поле!");
                    }else if(kolicina.getText().toString().trim().equals("")){
                        kolicina.requestFocus();
                        kolicina.setError("Задолжително поле!");
                    }
                }
                if(wantToCloseDialog){
                    alert.dismiss();
                }
            }
        });
    }

    public void Isprintaj(View view) {
        if(Double.parseDouble(Vkupno.getText().toString())>0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Дали сте сигурни дека сакате да направите сметка за селектираните артикли?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject parametri = new JSONObject();
                    try {
                        Double Vkupno = 0.0;
                        ArrayList<String> NizaID = new ArrayList<String>();
                        for (int i = 0; i < MasaArtikli.size(); i++) {
                            if (MasaArtikli.get(i).isSelektirnao()) {
                                NizaID.add(String.valueOf(MasaArtikli.get(i).getID()));
                                Vkupno = Vkupno + (MasaArtikli.get(i).getKol() * Double.parseDouble(MasaArtikli.get(i).getMaloprodaznaCena()));
                            }
                        }
                        Gson gson = new Gson();
                        String niza = gson.toJson(NizaID);
                        parametri.put("NizaID", niza);
                        parametri.put("ImePrezime", logiranAcc.getIme() + " " + logiranAcc.getPrezime());
                        parametri.put("SifraObjekt", PosledenObjekt.getSifra().toString());
                        parametri.put("idFirma", logiranAcc.getIdFirma());
                        parametri.put("Vkupno", Vkupno.toString());
                        parametri.put("BrMasa", brMasi.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UrlIsprintajSmetka, parametri, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                response.getString("error");
                                Toast.makeText(SmetkaActivity.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                                return;
                            } catch (JSONException e) {
                            }
                            try {
                                response.getString("greska");
                                Toast.makeText(SmetkaActivity.this, "Немате доволно количина од избраниот артикл!", Toast.LENGTH_SHORT).show();
                                return;
                            } catch (JSONException e) {
                            }
                            try {
                                String idSmetka = response.getString("idSmetka");

                                for (int i = MasaArtikli.size() - 1; i >= 0; i--) {
                                    if (MasaArtikli.get(i).isSelektirnao()) {
                                        double vkupno = Double.parseDouble(Vkupno.getText().toString());
                                        Vkupno.setText(String.valueOf(vkupno - (MasaArtikli.get(i).getKol() * Double.parseDouble(MasaArtikli.get(i).getMaloprodaznaCena()))));
                                        MasaArtikli.remove(i);
                                        Tabela.removeView(Tabela.getChildAt(i));
                                        System.out.println(i);
                                    }
                                }
                                System.out.println("TREBA DA SE ISPRINTE SMETKATA SO ID- " + idSmetka);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SmetkaActivity.this, "Настана некаков проблем! Обидете се повторно", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
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
    }

    private void ZemiArtiklOdBaza(String SifraArtikl,String Kol,int NegativnaKol){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject parametri = new JSONObject();
        try{
            parametri.put("SifraArtikl",SifraArtikl);
            parametri.put("Kol",Kol);
            parametri.put("NegativnaKol",NegativnaKol);
            parametri.put("SifraObjekt",PosledenObjekt.getSifra().toString());
            parametri.put("idFirma",logiranAcc.getIdFirma());
            parametri.put("BrMasa",brMasi.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlZemiArtikli,parametri,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    response.getString("error");
                    Toast.makeText(SmetkaActivity.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    response.getString("greska");
                    Toast.makeText(SmetkaActivity.this, "Немате доволно количина од избраниот артикл!", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                }
                try {
                    JSONObject artiklJ = response.getJSONArray("artikl").getJSONObject(0);

                    Artikl artikl = new Artikl();

                    artikl.setID(artiklJ.getInt("id"));
                    artikl.setSifraArtikl(artiklJ.getString("sifra_na_artikl"));
                    artikl.setNazivArtikl(artiklJ.getString("naziv_na_atikl"));
                    artikl.setEdinicaMerka(artiklJ.getString("edinica_merka"));
                    artikl.setMaloprodaznaCena(artiklJ.getString("maloprodazna_cena"));
                    artikl.setDanocnaTarifa(artiklJ.getInt("danocna_tarifa"));
                    artikl.setMKProizvod(artiklJ.getInt("mk_proizvod"));
                    artikl.setKol(Integer.parseInt(Kol));
                    artikl.setMagacinskaCena(artiklJ.getDouble("magacinska_cena_"+PosledenObjekt.getSifra().toString()));
                    artikl.setSelektirnao(true);

                    MasaArtikli.add(artikl);
                    double vkupno = Double.parseDouble(Vkupno.getText().toString());
                    Vkupno.setText(String.valueOf(vkupno+(artikl.getKol()*Double.parseDouble(artikl.getMaloprodaznaCena()))));

                    SozdadiRed(artikl,true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SmetkaActivity.this, "Настана некаков проблем! Обидете се повторно", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void ProcitajLista(){
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject parametri = new JSONObject();
                try{
                    parametri.put("BrMasa",brMasi.getText().toString());
                    parametri.put("idFirma",logiranAcc.getIdFirma());
                    parametri.put("SifraObjekt",String.valueOf(PosledenObjekt.getSifra()));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,UrlZemiMasaArtikli,parametri,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            response.getString("error");
                            Toast.makeText(SmetkaActivity.this, "Немате интернет конекција!", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (JSONException e) {
                        }
                        try {
                            response.getString("prazno");
                            return;
                        } catch (JSONException e) {
                        }
                        try {
                            JSONArray MArtikli = response.getJSONArray("MArtikli");
                            for (int i = 0; i < MArtikli.length(); i++) {
                                JSONObject MArtiklJ = MArtikli.getJSONObject(i);

                                Artikl artikl = new Artikl();

                                artikl.setID(MArtiklJ.getInt("id"));
                                artikl.setSifraArtikl(MArtiklJ.getString("sifra"));
                                artikl.setNazivArtikl(MArtiklJ.getString("naziv_artikl"));
                                artikl.setEdinicaMerka(MArtiklJ.getString("ed_merka"));
                                artikl.setKol(MArtiklJ.getInt("kol"));
                                artikl.setMaloprodaznaCena(MArtiklJ.getString("maloprodazna"));
                                artikl.setMagacinskaCena(MArtiklJ.getDouble("magacinska_cena"));
                                artikl.setMKProizvod(MArtiklJ.getInt("mk_proizvod"));
                                artikl.setSelektirnao(true);

                                MasaArtikli.add(artikl);
                                double vkupno = Double.parseDouble(Vkupno.getText().toString());
                                Vkupno.setText(String.valueOf(vkupno+(artikl.getKol()*Double.parseDouble(artikl.getMaloprodaznaCena()))));

                                SozdadiRed(artikl,true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SmetkaActivity.this, "Неможе да го пронајдеме Објектот!", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
    }

    private void SozdadiRed(Artikl artikl , boolean Selektiraj){
        TableRow row = (TableRow) LayoutInflater.from(SmetkaActivity.this).inflate(R.layout.red_vo_tabela, null);
        ((TextView) row.findViewById(R.id.rowSifraArtikl)).setText(artikl.getSifraArtikl());
        ((TextView) row.findViewById(R.id.rowNazivArtikl)).setText(artikl.getNazivArtikl());
        ((TextView) row.findViewById(R.id.rowKolArtikl)).setText(artikl.getKol().toString());
        ((TextView) row.findViewById(R.id.rowMaloprodaznaArtikl)).setText(artikl.getMaloprodaznaCena());
        ((TextView) row.findViewById(R.id.rowVkMaloprodaznaArtikl)).setText(String.valueOf(artikl.getKol()*Double.parseDouble(artikl.getMaloprodaznaCena())));

        CheckBox selektiraj = (CheckBox) row.findViewById(R.id.rowSelektiraj);
        selektiraj.setChecked(Selektiraj);
        selektiraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<MasaArtikli.size();i++) {
                    if(MasaArtikli.get(i).getID()==artikl.getID()) {
                        if (selektiraj.isChecked()) {
                            MasaArtikli.get(i).setSelektirnao(true);
                            double vkupno = Double.parseDouble(Vkupno.getText().toString());
                            Vkupno.setText(String.valueOf(vkupno+(artikl.getKol()*Double.parseDouble(artikl.getMaloprodaznaCena()))));
                        } else {
                            MasaArtikli.get(i).setSelektirnao(false);
                            double vkupno = Double.parseDouble(Vkupno.getText().toString());
                            Vkupno.setText(String.valueOf(vkupno-(artikl.getKol()*Double.parseDouble(artikl.getMaloprodaznaCena()))));
                        }
                        break;
                    }
                }
            }
        });

        TextView izbrisi = (TextView) row.findViewById(R.id.rowIzbrisi);
        izbrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izbrisi.setClickable(false);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[6];
                        field[0] = "IDsmetkainfo";
                        field[1] = "SifraArtikl";
                        field[2] = "Kol";
                        field[3] = "MagacinskaCena";
                        field[4] = "idFirma";
                        field[5] = "SifraObjekt";
                        String[] data = new String[6];
                        data[0] = String.valueOf(artikl.getID());
                        data[1] = artikl.getSifraArtikl();
                        data[2] = String.valueOf(artikl.getKol());
                        data[3] = String.valueOf(artikl.getMagacinskaCena());
                        data[4] = logiranAcc.getIdFirma();
                        data[5] = PosledenObjekt.getSifra().toString();
                        PutData putData = new PutData(UrlIzbrisiArtikli, "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Success")) {
                                    for (int i=0; i<MasaArtikli.size();i++) {
                                        if(MasaArtikli.get(i).getID()==artikl.getID()) {
                                            MasaArtikli.remove(i);
                                            row.setVisibility(View.GONE);
                                            if(selektiraj.isChecked()) {
                                                double vkupno = Double.parseDouble(Vkupno.getText().toString());
                                                Vkupno.setText(String.valueOf(vkupno - (artikl.getKol() * Double.parseDouble(artikl.getMaloprodaznaCena()))));
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    Toast.makeText(SmetkaActivity.this, "Настана некаков проблем! Обидете се повторно", Toast.LENGTH_SHORT).show();
                                }
                                System.out.println(result);
                            }
                        }
                    }
                });
                izbrisi.setClickable(true);
            }
        });

        Tabela.addView(row);
    }
}