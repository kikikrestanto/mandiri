package com.example.mandiri.Activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandiri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashAct extends AppCompatActivity {

    ImageView logoSplash;
    String email , uid;

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    DatabaseReference mRef;


    String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //bar transparant
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mUser != null){
                    mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Intent intent = new Intent(SplashAct.this,DashboardAct.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent = new Intent(SplashAct.this,LoginAct.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else{
                    Intent intent = new Intent(SplashAct.this,LoginAct.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,1000);
    }



}
