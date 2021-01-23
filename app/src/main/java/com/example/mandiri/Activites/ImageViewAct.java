package com.example.mandiri.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.mandiri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ImageViewAct extends AppCompatActivity {
    String postImageFull,postimage, myUid,myEmail;

    ImageView fullImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        fullImage = findViewById(R.id.fullImage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        postimage = intent.getStringExtra("fullImage");

        fullImage = findViewById(R.id.fullImage);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child("pImage");

        checkUserStatus();

        String pImage = getIntent().getStringExtra("pImage");
        Picasso.get().load(pImage).into(fullImage);
    }

    private  void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            //user is signed in
            myEmail = user.getEmail();
            myUid = user.getUid();
        }
        else {
            //user not signed in , go to main activity
            startActivity(new Intent(this,LoginAct.class));
            finish();
        }
    }
}
