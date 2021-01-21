package com.example.mandiri.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mandiri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddPostMaintenanceAct extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference userDbref;
    FirebaseDatabase firebaseDatabase;

    //user info
    String name,email,uid, dp;
    LinearLayout nameLinear, merkLinear, lokasiLinear, inventarisLinear, dateLinear;
    TextView nameTextView,merkText, lokasiText, inventarisText, dateText;
    EditText jenisEdit, merkEdit, lokasiEdit, inventarisEdit, dateEdit;
    Button uploadM;

    ActionBar actionBar;

    //progress bar
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_maintenance);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Post Maintenance");

        //enable back in action bar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameLinear = findViewById(R.id.nameLinear);
        merkLinear = findViewById(R.id.merkLinear);
        lokasiLinear = findViewById(R.id.lokasiLinear);
        inventarisLinear = findViewById(R.id.inventarisLinear);
        //dateLinear = findViewById(R.id.dateLinear);
        nameTextView = findViewById(R.id.nameTextView);
        merkText = findViewById(R.id.merkText);
        lokasiText = findViewById(R.id.lokasiText);
        inventarisText = findViewById(R.id.inventarisText);
        //dateText = findViewById(R.id.dateText);
        jenisEdit = findViewById(R.id.jenisEdit);
        merkEdit = findViewById(R.id.merkEdit);
        lokasiEdit = findViewById(R.id.lokasiEdit);
        inventarisEdit = findViewById(R.id.investorEdit);
        //dateEdit = findViewById(R.id.dateEdit);
        uploadM = findViewById(R.id.uploadM);

        checkUserStatus();

        //init
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDbref = firebaseDatabase.getReference("Users");


        pd = new ProgressDialog(this);

        //get some info or current user to include post
        userDbref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbref.orderByChild("mail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    name = ""+ds.child("name").getValue();
                    email = ""+ds.child("mail").getValue();
                    dp = ""+ds.child("image").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String jenis = jenisEdit.getText().toString().trim();
                String merk = merkEdit.getText().toString().trim();
                String lokasi = lokasiEdit.getText().toString().trim();
                String inventaris = inventarisEdit.getText().toString().trim();

                if (TextUtils.isEmpty(jenis)){
                    Toast.makeText(AddPostMaintenanceAct.this, "Enter Type...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(merk)){
                    Toast.makeText(AddPostMaintenanceAct.this, "Enter Merk...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lokasi)){
                    Toast.makeText(AddPostMaintenanceAct.this, "Enter Location...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(inventaris)){
                    Toast.makeText(AddPostMaintenanceAct.this, "Enter Inventory.... ", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadData(jenis, merk, lokasi, inventaris);

            }
        });
    }

    private void uploadData(final String jenis, String merk, String lokasi, String inventaris){
        pd.setMessage("Publishing Post...");
        pd.show();

        final String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("pId", timeStamp);
        hashMap.put("uName", name);
        hashMap.put("uEmail", email);
        hashMap.put("jenisEdit", jenis);
        hashMap.put("merkEdit", merk);
        hashMap.put("lokasiEdit", lokasi);
        hashMap.put("inventarisEdit", inventaris);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostMaintenance");
        databaseReference.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(AddPostMaintenanceAct.this, "Post Published...", Toast.LENGTH_SHORT).show();
                        //reset views
                        jenisEdit.setText("");
                        merkEdit.setText("");
                        lokasiEdit.setText("");
                        inventarisEdit.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPostMaintenanceAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        // get current user
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user signed stay here
            email = user.getEmail();
            uid = user.getUid();
        } else {
            // user not sign in, go to Register
            startActivity(new Intent(this, RegisterAct.class));
            finish();
        }
    }
}
