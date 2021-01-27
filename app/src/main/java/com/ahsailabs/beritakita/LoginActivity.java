package com.ahsailabs.beritakita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.login.models.LoginData;
import com.ahsailabs.beritakita.ui.login.models.LoginResponse;
import com.ahsailabs.beritakita.utils.HttpUtil;
import com.ahsailabs.beritakita.utils.InfoUtil;
import com.ahsailabs.beritakita.utils.SessionUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout llLoadingPanel;
    private ProgressBar pbLoadingIndicator;
    private TextInputLayout tilUserName;
    private TextInputEditText etUsername;
    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private LinearLayout llLoginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadViews();
        registerClickListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerClickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilPassword.setError(null);
                tilUserName.setError(null);

                //get all data
                String strUserName = etUsername.getText().toString();
                String strPassword = etPassword.getText().toString();


                //validasi inputan
                if(TextUtils.isEmpty(strUserName)){
                    tilUserName.setError("Username wajib diisi");
                    return;
                }

                if(TextUtils.isEmpty(strPassword)){
                    tilPassword.setError("Password wajib diisi");
                    return;
                }

                doLogin(strUserName, strPassword);
            }
        });
    }

    private void doLogin(String strUserName, String strPassword) {
        showLoading();
        AndroidNetworking.post(Config.getLoginUrl())
                .setOkHttpClient(HttpUtil.getCLient(LoginActivity.this))
                .addBodyParameter("username", strUserName)
                .addBodyParameter("password", strPassword)
                .setTag("login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(LoginResponse response) {
                        hideLoading();
                        if (response.getStatus() == 1) {
                            LoginData loginData = response.getData();
                            SessionUtil.login(LoginActivity.this, loginData);
                            InfoUtil.showToast(LoginActivity.this, "Login anda berhasil");
                            finish();
                        } else {
                            switch (response.getStatus()){
                                case -6: InfoUtil.showToast(LoginActivity.this, "Username atau password anda salah"); break;
                                case -1: InfoUtil.showToast(LoginActivity.this, "Anda tidak berhak menggunakan api ini"); break;
                                default:
                                    InfoUtil.showToast(LoginActivity.this, response.getMessage());
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        InfoUtil.showToast(LoginActivity.this, anError.getMessage());
                    }
                });
    }

    private void hideLoading() {
        llLoginForm.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        llLoadingPanel.setVisibility(View.INVISIBLE);
        pbLoadingIndicator.setProgress(0);
    }

    private void showLoading() {
        llLoginForm.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);
        llLoadingPanel.setVisibility(View.VISIBLE);
        pbLoadingIndicator.setProgress(50);
    }

    private void loadViews() {
        tilUserName = findViewById(R.id.tilUserName);
        etUsername = findViewById(R.id.etUsername);
        tilPassword = findViewById(R.id.tilPassword);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        llLoadingPanel = findViewById(R.id.llLoadingPanel);
        pbLoadingIndicator = findViewById(R.id.pbLoadingIndicator);
        llLoginForm = findViewById(R.id.llLoginForm);
    }

    @Override
    protected void onDestroy() {
        AndroidNetworking.cancel("login");
        super.onDestroy();
    }

    public static void start(Context context){
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
    }
}