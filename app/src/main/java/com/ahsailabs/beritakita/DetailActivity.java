package com.ahsailabs.beritakita;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ahsailabs.beritakita.R;
import com.google.android.material.textview.MaterialTextView;

public class DetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvUser;
    private TextView tvDate;
    private MaterialTextView tvBody;
    private ImageView ivPhoto;
    private ScrollView svMain;

    private LinearLayout llLoadingPanel;
    private ProgressBar pbLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        loadViews();
        loadData();
    }

    private void loadData() {

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
}