package com.example.mandiri.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mandiri.Fragments.DateFragment;
import com.example.mandiri.Fragments.home;
import com.example.mandiri.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardAct extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    BottomNavigationView navigationView;

    ActionBar actionBar;

    String mUID;

    private Fragment mSelectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Home");
        mSelectedFragment = new home();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.container, mSelectedFragment, "");
        ft1.commit();

        checkUserStatus();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //handle bottom navigation
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            //home fragment trasaction
                            actionBar.setTitle("Home");
                            mSelectedFragment = new home();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.container, mSelectedFragment, "");
                            ft1.commit();
                            return true;
                        case R.id.nav_date:
                            //profile fragment trasaction
                            actionBar.setTitle("Maintenance");
                            mSelectedFragment = new DateFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.container, mSelectedFragment, "");
                            ft2.commit();
                            return true;

                    }

                    return true;
                }
            };

    private void checkUserStatus() {
        // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user signed stay here
            // set email of logged user
            //mProfileTv.setText(user.getEmail());

            mUID = user.getUid();

            //save uid of currently signed in user in shared preferences
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();
        } else {
            // user not sign in, go to Register
            startActivity(new Intent(DashboardAct.this, LoginAct.class));
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
    /* inflate option menu*/
}
