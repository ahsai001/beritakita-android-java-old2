package com.ahsailabs.beritakita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.addnews.models.AddNewsResponse;
import com.ahsailabs.beritakita.utils.HttpUtil;
import com.ahsailabs.beritakita.utils.InfoUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ahsailabs.beritakita.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewsActivity extends AppCompatActivity {
    private TextInputLayout tilTitle;
    private TextInputEditText tietTitle;
    private TextInputLayout tilSummary;
    private TextInputEditText tietSummary;
    private TextInputLayout tilBody;
    private TextInputEditText tietBody;
    private MaterialButton mbtnPhoto;
    private ImageView ivPhoto;

    private ExtendedFloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        hideLoading();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fab.isExtended()){
                    InfoUtil.showSnackBar(view, getString(R.string.wording_please_wait));
                } else {
                    validateAndSendData();
                }
            }
        });

        loadViews();
    }

    private void validateAndSendData() {
        //get all data
        String strTitle = tietTitle.getText().toString();
        String strSummary = tietSummary.getText().toString();
        String strBody = tietBody.getText().toString();

        //validasi data
        if(TextUtils.isEmpty(strTitle)){
            tilTitle.setError("title cannot be empty");
            return;
        }

        if(TextUtils.isEmpty(strSummary)){
            tilSummary.setError("summary cannot be empty");
            return;
        }

        if(TextUtils.isEmpty(strBody)){
            tilBody.setError("body cannot be empty");
            return;
        }
        //send data to server
        tilTitle.setError(null);
        tilSummary.setError(null);
        tilBody.setError(null);
        sendData(strTitle, strSummary, strBody);
    }

    private void sendData(String strTitle, String strSummary, String strBody) {
        showLoading();
        AndroidNetworking.post(Config.getAddNewsUrl())
                .setOkHttpClient(HttpUtil.getCLient(this))
                .addBodyParameter("title", strTitle)
                .addBodyParameter("summary", strSummary)
                .addBodyParameter("body", strBody)
                .addBodyParameter("groupcode", Config.GROUP_CODE)
                .setTag("addnews")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(AddNewsResponse.class, new ParsedRequestListener<AddNewsResponse>() {
                    @Override
                    public void onResponse(AddNewsResponse response) {
                        if(response.getStatus() == 1){
                            InfoUtil.showToast(AddNewsActivity.this, "suskses posting pertamax");
                        } else {
                            InfoUtil.showToast(AddNewsActivity.this, response.getMessage());
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        InfoUtil.showToast(AddNewsActivity.this, anError.getMessage());
                    }
                });

    }

    private void showLoading() {
        fab.extend();
    }

    private void hideLoading(){
        fab.shrink();
    }

    private void loadViews() {
        tilTitle = findViewById(R.id.tilTitle);
        tietTitle = findViewById(R.id.tietTitle);
        tilSummary = findViewById(R.id.tilSummary);
        tietSummary = findViewById(R.id.tietSummary);
        tilBody = findViewById(R.id.tilBody);
        tietBody = findViewById(R.id.tietBody);
        mbtnPhoto = findViewById(R.id.mbtnPhoto);
        ivPhoto = findViewById(R.id.ivPhoto);

    }


    public static void start(Context context){
        Intent addNewsIntent = new Intent(context, AddNewsActivity.class);
        context.startActivity(addNewsIntent);
    }
}