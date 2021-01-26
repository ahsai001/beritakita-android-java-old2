package com.ahsailabs.beritakita;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.ahsailabs.beritakita.bases.BaseApp;
import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.login.models.LoginData;
import com.ahsailabs.beritakita.utils.SessionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private CoordinatorLayout clRoot;
    NavigationView navigationView;

    public static void start(Context context) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        context.startActivity(mainIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        clRoot = findViewById(R.id.clRoot);

        fab.setOnClickListener(view -> {
            //open latihan ui activity
            //LatihanUiActivity.start(MainActivity.this);

            //masuk form tambah berita baru atau masuk ke form login jika belum login
            boolean isLoggedIn = SessionUtil.isLoggedIn(MainActivity.this);
            if(isLoggedIn){
                // masuk form tambah berita
                //InfoUtil.showSnackBar(clRoot, "Anda sudah login, silakan kirim berita baru");

                //start addbnewsactivity
                AddNewsActivity.start(this);
            } else {
                // masuk form login
                //LoginActivity.start(this);
                Login2Activity.start(this);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Log.d("MainActivity", ((BaseApp)getApplication()).data);


        //put refreshdrawer in drawerlistener
        /*drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                refreshDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });*/

        //atau

        //put refreshdrawer in DestinationChangedListener
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                refreshDrawer();
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic(Config.GROUP_CODE)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("beritakita", "sukses subscribe");
                } else {
                    Log.d("beritakita", "gagal subscribe");
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        // Get new Instance ID token
                        if(task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            Log.d("beritakita", "token:" + token);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDrawer();
    }

    private void refreshDrawer(){
        MenuItem navLogin = navigationView.getMenu().findItem(R.id.nav_login);
        MenuItem navLogout = navigationView.getMenu().findItem(R.id.nav_logout);

        LoginData loginData;

        //update menu drawer
        if(SessionUtil.isLoggedIn(this)){
            navLogin.setVisible(false);
            navLogout.setVisible(true);
            try {
                loginData = SessionUtil.getLoginData(this);
            } catch (AuthenticatorException e) {
                e.printStackTrace();
                return;
            } catch (OperationCanceledException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            navLogin.setVisible(true);
            navLogout.setVisible(false);

            loginData = new LoginData();
            loginData.setName("anonymous");
            loginData.setUsername("anonymous");
        }

        //update headerview with loginData
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvName);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);

        tvName.setText(loginData.getName());
        tvUserName.setText(String.format("@%s", loginData.getUsername()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.main_action_logout);
        if(SessionUtil.isLoggedIn(this)){
            menuItem.setTitle("Logout");
        } else {
            menuItem.setTitle("Login");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main_action_logout){
            if(SessionUtil.isLoggedIn(this)) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Logout Confirmation")
                        .setMessage("Are you sure?")
                        .setPositiveButton("logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SessionUtil.logout(MainActivity.this);
                                refreshDrawer();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            } else {
                //LoginActivity.start(this);
                Login2Activity.start(this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}