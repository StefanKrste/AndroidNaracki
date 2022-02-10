package com.example.apnarackirestoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPhone, editTextPassword, editTextPassword2, ediTextName, editTextUserName, ediTextSurname;
    private final String UrlNajaviSe="http://192.168.100.5/Android%20app/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ediTextName = (EditText) findViewById(R.id.RegisterName);
        ediTextSurname = (EditText) findViewById(R.id.RegisterSurname);
        editTextEmail = (EditText) findViewById(R.id.RegisterEmail);
        editTextPhone = (EditText) findViewById(R.id.RegisterPhone);
        editTextPassword = (EditText) findViewById(R.id.RegisterPassword);
        editTextPassword2 = (EditText) findViewById(R.id.RegisterPassword2);
        editTextUserName = (EditText) findViewById(R.id.RegisterUserName);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void Register(View view) {
        Intent intentLogin = new Intent(this, LoginActivity.class);

        String name = ediTextName.getText().toString().trim();
        String surname = ediTextSurname.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String username = editTextUserName.getText().toString().trim();

        if(name.equals("")) {
            ediTextName.setError("Задолжително поле!");
            ediTextName.requestFocus();
            return;
        }

        if(surname.equals("")) {
            ediTextSurname.setError("Задолжително поле!");
            ediTextSurname.requestFocus();
            return;
        }

        if(email.equals("")) {
            editTextEmail.setError("Задолжително поле!");
            editTextEmail.requestFocus();
            return;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Внесете валидна е-маил адреса!");
                editTextEmail.requestFocus();
                return;
        }

        if(username.equals("")) {
            editTextUserName.setError("Задолжително поле!");
            editTextUserName.requestFocus();
            return;
        }else if(username.length()<6){
            editTextUserName.setError("Внесете повеќе од 6 карактери!");
            editTextUserName.requestFocus();
            return;
        }

        if(password.equals("")) {
            editTextPassword.setError("Задолжително поле!");
            editTextPassword.requestFocus();
            return;
        } else if(password.length() < 6) {
                editTextPassword.setError("Внесете повеќе од 6 карактери!");
                editTextPassword.requestFocus();
                return;
        }

        if(password2.equals("")) {
            editTextPassword2.setError("Задолжително поле!");
            editTextPassword2.requestFocus();
            return;
        }

        if(!password.equals(password2)) {
            editTextPassword.setError("Лозинктие не се поклопуваат!");
            editTextPassword.requestFocus();
            return;
        }

        if(phone.equals("")) {
            editTextPhone.setError("Задолжително поле!");
            editTextPhone.requestFocus();
            return;
        } else if(!Patterns.PHONE.matcher(phone).matches()) {
                editTextPhone.setError("Внесете валиден телефонски број!");
                editTextPhone.requestFocus();
                return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[6];
                field[0] = "username";
                field[1] = "password";
                field[2] = "mail";
                field[3] = "ime";
                field[4] = "prezime";
                field[5] = "tel";
                String[] data = new String[6];
                data[0] = username;
                data[1] = password;
                data[2] = email;
                data[3] = name;
                data[4] = surname;
                data[5] = phone;
                PutData putData = new PutData(UrlNajaviSe, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Sign Up Success")) {
                            startActivity(intentLogin);
                        } else if (result.equals("Mail exists")){
                            editTextEmail.setError("Веќе постои акаунт со кој го користи тој е-маил");
                            editTextEmail.setText("");
                            editTextEmail.requestFocus();
                        } else if (result.equals("Username exists")){
                            editTextUserName.setError("Корисничкото име веќе постои");
                            editTextUserName.setText("");
                            editTextUserName.requestFocus();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Неуспешна регистрација.Обидете се повторно!", Toast.LENGTH_SHORT).show();
                        }
                        System.out.println(result);
                    }
                }
            }
        });
    }
}