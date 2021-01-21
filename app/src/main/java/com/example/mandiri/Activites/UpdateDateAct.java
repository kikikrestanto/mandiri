package com.example.mandiri.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mandiri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UpdateDateAct extends AppCompatActivity {

    final String TAG = "UpdateActivity";
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    String myUid, myEmail, pId, postId, uName, postIdMaintenance;

    TextView noText, noView, tanggalText, tanggalView, tindakanText, tindakanView,
            ketText, ketView;

    Button updateButton;

    Calendar calendar = Calendar.getInstance();
    Date tanggalViewDate;
    DatePickerDialog.OnDateSetListener setListener;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_date);

        noText = findViewById(R.id.noText);
        noView = findViewById(R.id.noView);
        tanggalText = findViewById(R.id.tanggalText);
        tanggalView = findViewById(R.id.tanggalView);
        tindakanText = findViewById(R.id.tindakanText);
        tindakanView = findViewById(R.id.tindakanView);
        ketText = findViewById(R.id.ketText);
        ketView = findViewById(R.id.ketView);
        updateButton = findViewById(R.id.updateButton);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        Toast.makeText(this,postId, Toast.LENGTH_SHORT).show();

        checkUserStatus();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomor = noView.getText().toString().trim();
                String tanggal = tanggalView.getText().toString().trim();
                String tindakan = tindakanView.getText().toString().trim();
                String keterangan = ketView.getText().toString().trim();

                if (TextUtils.isEmpty(nomor)){
                    Toast.makeText(UpdateDateAct.this, "Enter No....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tanggal)){
                    Toast.makeText(UpdateDateAct.this, "Enter Tanggal ....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tindakan)){
                    Toast.makeText(UpdateDateAct.this, "Enter Action ....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(keterangan)){
                    Toast.makeText(UpdateDateAct.this, "Enter Information ....", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateData(nomor, tanggal, tindakan, keterangan);
            }
        });

        tanggalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(UpdateDateAct.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
            setListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    Log.d(TAG, "onDateSet: mm/dd/yyy :" + month + "/" + day + "/" + year);
                    String date = month + "/" + day + "/" + year;
                    tanggalView.setText(date);
                    tanggalViewDate = calendar.getTime();
                }
            };
    }

    private void updateData(final String nomor, String tanggal, String tindakan, String keterangan) {
        pd = new ProgressDialog(this);
        pd.setMessage("Updated...");

        final String date = String.valueOf(tanggalViewDate.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostMaintenance").child(postId).child("Updated");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", myUid);
        hashMap.put("uName", uName);
        hashMap.put("uEmail", myEmail);
        hashMap.put("nomor", nomor);
        hashMap.put("tanggal", date);
        hashMap.put("tindakan", tindakan);
        hashMap.put("keterangan", keterangan);

        reference.child(date).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UpdateDateAct.this, "Updated Success...", Toast.LENGTH_SHORT).show();
                //reset views
                noView.setText("");
                tanggalView.setText("");
                tindakanView.setText("");
                ketView.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateDateAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            startActivity(new Intent(this,SplashAct.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //hide some menu item
        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
