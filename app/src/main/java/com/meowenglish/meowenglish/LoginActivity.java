package com.meowenglish.meowenglish;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    String[] Gender = { "Мужской", "Женский", "Свой "};

    private EditText edLogin, edEmail, edPasswordSignUp, edPassword, edName, edOccupation;
    private Button edSignIn, edSignUp/*создать акк*/, onCSignUp /*зарегестрироваться*/;
    private Spinner edGender;
    ArrayAdapter<String> adapter;
    FirebaseUser User;

    private FirebaseAuth mAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        Toast.makeText(this,"Тест", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        User = mAuth.getCurrentUser();
        if  (User != null)
        {
            finishLogin();
        }
    }

    public void finishLogin()
    {
        User = mAuth.getCurrentUser();
        if (User.isEmailVerified()) {
            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }


    private void init()
    {
        edLogin = findViewById(R.id.edLogin);
        edEmail = findViewById(R.id.edEmail);
        edPasswordSignUp = findViewById(R.id.edPasswordSignUp);
        edPassword = findViewById(R.id.edPassword);
        edName = findViewById(R.id.edName);

        edGender = (Spinner) findViewById(R.id.edGender);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Gender);
        edGender.setAdapter(adapter);

        edOccupation = findViewById(R.id.edOccupation);
        edSignIn = findViewById(R.id.edSignIn);
        edSignUp = findViewById(R.id.edSignUp);
        onCSignUp = findViewById(R.id.onCSignUp);
        mAuth = FirebaseAuth.getInstance();
        disableRegistInter();
    }

    /*Кнопка регистрации*/
    public void onClickSignUp(View view)
    {
        if (!TextUtils.isEmpty(edEmail.getText().toString()) && !TextUtils.isEmpty(edName.getText().toString())
                && !TextUtils.isEmpty(edPasswordSignUp.getText().toString()) && !TextUtils.isEmpty(edOccupation.getText().toString()))
        {
            mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPasswordSignUp.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                        SendEmailVerif();
                        disableRegistInter();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Регестрация не удалась", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this,"Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    /*Кнопка создания аккаунта*/
    public void onClickCreateAcc(View view)
    {
        enableRegistInter();
    }

    /*Кнопка войти*/
    public void onClickSignIn(View view)
    {
        if (!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()))
        {
            User = mAuth.getCurrentUser();
            if (User.getEmail().toString() == edLogin.getText().toString()) {
                if (!User.isEmailVerified())
                    Toast.makeText(getApplicationContext(), "Подтвердите электронную почту", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
                            finishLogin();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка входа", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }

    }

    private void SendEmailVerif() {
        if (User != null)
        {
            User.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Проверь свой Email, для подтверждения аккаунта", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка, письмо не отправлено", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    }

    /*Убирает интерфейс регистрации и включает интрефейс входа*/
    private void disableRegistInter()
    {
        edLogin.setVisibility(View.VISIBLE);
        edEmail.setVisibility(View.GONE);
        edPasswordSignUp.setVisibility(View.GONE);
        edPassword.setVisibility(View.VISIBLE);
        edName.setVisibility(View.GONE);
        edGender.setVisibility(View.GONE);
        edOccupation.setVisibility(View.GONE);
        edSignIn.setVisibility(View.VISIBLE);
        edSignUp.setVisibility(View.VISIBLE);
        onCSignUp.setVisibility(View.GONE);
    }

    /*Убирает интерфейс входа и включает интрефейс регистрации*/
    private void enableRegistInter()
    {
        edLogin.setVisibility(View.GONE);
        edEmail.setVisibility(View.VISIBLE);
        edPasswordSignUp.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.GONE);
        edName.setVisibility(View.VISIBLE);
        edGender.setVisibility(View.VISIBLE);
        edOccupation.setVisibility(View.VISIBLE);
        edSignIn.setVisibility(View.GONE);
        edSignUp.setVisibility(View.GONE);
        onCSignUp.setVisibility(View.VISIBLE);
    }
}
