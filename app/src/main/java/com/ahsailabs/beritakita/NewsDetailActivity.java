package com.ahsailabs.beritakita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.detail.DetailViewModel;
import com.ahsailabs.beritakita.ui.detail.models.NewsDetail;
import com.ahsailabs.beritakita.ui.detail.models.NewsDetailResponse;
import com.ahsailabs.beritakita.utils.HttpUtil;
import com.ahsailabs.beritakita.utils.InfoUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {
    public static final String PARAM_NEWS_ID = "param_news_id";
    private TextView tvTitle;
    private TextView tvUser;
    private TextView tvDate;
    private MaterialTextView tvBody;
    private ImageView ivPhoto;
    private ScrollView svMain;

    private LinearLayout llLoadingPanel;
    private ProgressBar pbLoadingIndicator;
    private DetailViewModel detailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String newsId = getIntent().getStringExtra(PARAM_NEWS_ID);

        loadViews();

        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        if(savedInstanceState == null || detailViewModel.newsDetail.getValue()==null) {
            loadData(newsId);
        } else {
            updateViews(detailViewModel.newsDetail.getValue());
        }

        detailViewModel.newsDetail.observe(this, new Observer<NewsDetail>() {
            @Override
            public void onChanged(NewsDetail newsDetail) {
                updateViews(newsDetail);
            }
        });

    }

    private void loadData(String newsId) {
        showLoading();
        AndroidNetworking.get(Config.getNewsDetailUrl().replace("{id}", newsId))
                .setOkHttpClient(HttpUtil.getCLient(this))
                .setTag("newsdetail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(NewsDetailResponse.class, new ParsedRequestListener<NewsDetailResponse>() {
                    @Override
                    public void onResponse(NewsDetailResponse response) {
                        if(response.getStatus() == 1){
                            //show detail in views
                            detailViewModel.newsDetail.setValue(response.getNewsDetail());
                        } else {
                            InfoUtil.showToast(NewsDetailActivity.this, response.getMessage());
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(ANError anError) {
                        InfoUtil.showToast(NewsDetailActivity.this, anError.getMessage());
                        hideLoading();
                    }
                });
    }

    private void updateViews(NewsDetail newsDetail) {
        tvTitle.setText(newsDetail.getTitle());
        getSupportActionBar().setTitle(newsDetail.getTitle());

        tvDate.setText(newsDetail.getCreatedAt());
        tvUser.setText(newsDetail.getCreatedBy());
        tvBody.setText(newsDetail.getBody());

        if(!TextUtils.isEmpty(newsDetail.getPhoto())){
            Picasso.get().load(newsDetail.getPhoto()).into(ivPhoto);
        }
    }

    private void showLoading(){
        svMain.setVisibility(View.GONE);
        llLoadingPanel.setVisibility(View.VISIBLE);
        pbLoadingIndicator.setProgress(50);
    }

    private void hideLoading(){
        svMain.setVisibility(View.VISIBLE);
        llLoadingPanel.setVisibility(View.GONE);
        pbLoadingIndicator.setProgress(0);
    }

    private void loadViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvUser = findViewById(R.id.tvUser);
        tvDate = findViewById(R.id.tvDate);
        tvBody = findViewById(R.id.tvBody);
        ivPhoto = findViewById(R.id.ivPhoto);
        svMain = findViewById(R.id.svMain);

        llLoadingPanel = findViewById(R.id.llLoadingPanel);
        pbLoadingIndicator = findViewById(R.id.pbLoadingIndicator);
    }

    @Override
    protected void onDestroy() {
        AndroidNetworking.cancel("newsdetail");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context, String newsId){
        Intent detailIntent = new Intent(context, NewsDetailActivity.class);
        detailIntent.putExtra(PARAM_NEWS_ID, newsId);
        context.startActivity(detailIntent);
    }
}