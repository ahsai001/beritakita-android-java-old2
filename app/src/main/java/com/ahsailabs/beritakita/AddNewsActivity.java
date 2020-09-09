package com.ahsailabs.beritakita;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.addnews.models.AddNewsResponse;
import com.ahsailabs.beritakita.utils.HttpUtil;
import com.ahsailabs.beritakita.utils.InfoUtil;
import com.ahsailabs.beritakita.utils.PermissionUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ahsailabs.beritakita.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

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

    private EasyImage easyImage;
    private PermissionUtil permissionUtil;
    private MediaFile mediaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Form Tambah Berita");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        setupEasyImage();
        loadViews();
        setupListeners();
    }

    private void setupListeners() {
        mbtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddNewsActivity.this);
                builder.setTitle("How do you get the image?");
                final String[] options = {"Camera", "Gallery"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(options[which].equals("Camera")){
                            permissionUtil = PermissionUtil.checkPermissionAndGo(AddNewsActivity.this, 1003, new Runnable() {
                                @Override
                                public void run() {
                                    easyImage.openCameraForImage(AddNewsActivity.this);
                                }
                            }, Manifest.permission.CAMERA);
                        } else {
                            easyImage.openDocuments(AddNewsActivity.this);
                        }
                    }
                });

                builder.create().show();
            }
        });
    }

    private void setupEasyImage() {
        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("How do you get an image?")
                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .build();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(permissionUtil != null){
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(easyImage != null){
            easyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
                @Override
                public void onImagePickerError(@NotNull Throwable throwable, @NotNull MediaSource mediaSource) {

                }

                @Override
                public void onMediaFilesPicked(@NotNull MediaFile[] mediaFiles, @NotNull MediaSource mediaSource) {
                    mediaFile = mediaFiles[0];
                    Picasso.get().load(mediaFile.getFile()).into(ivPhoto);
                }

                @Override
                public void onCanceled(@NotNull MediaSource mediaSource) {

                }
            });
        }
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

        if(mediaFile == null){
            mbtnPhoto.setError("Gambar tidak boleh kosong");
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
        AndroidNetworking.upload(Config.getAddNewsUrl())
                .setOkHttpClient(HttpUtil.getCLient(this))
                .addMultipartFile("photo", mediaFile.getFile())
                .addMultipartParameter("title", strTitle)
                .addMultipartParameter("summary", strSummary)
                .addMultipartParameter("body", strBody)
                .addMultipartParameter("groupcode", Config.GROUP_CODE)
                .setTag("addnews")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(AddNewsResponse.class, new ParsedRequestListener<AddNewsResponse>() {
                    @Override
                    public void onResponse(AddNewsResponse response) {
                        if(response.getStatus() == 1){
                            InfoUtil.showToast(AddNewsActivity.this, "suskses tambah berita");

                            //tutup halaman tambah berita
                            finish();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            //onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        AndroidNetworking.cancel("addnews");
        super.onDestroy();
    }

    public static void start(Context context){
        Intent addNewsIntent = new Intent(context, AddNewsActivity.class);
        context.startActivity(addNewsIntent);
    }
}