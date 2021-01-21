package com.example.mandiri.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mandiri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PostDetailMaintenanceAct extends AppCompatActivity {

    String email,myUid,uid,pId
            ,name, myName, postIdMaintenance,postid;

    TextView nameUserDetail,nameTextViewDetail, jenisTextViewDetail,
            merkTextDetail,merkViewDetail,lokasiTextDetail,lokasiViewDetail,inventarisTextDetail,
            inventarisViewDetail;
    ImageButton moreBtnMainDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail_maintenance);

        Intent intent = getIntent();
        postid = intent.getStringExtra("postId");


        nameUserDetail = findViewById(R.id.nameUserDetail);
        nameTextViewDetail = findViewById(R.id.nameTextViewDetail);
        jenisTextViewDetail = findViewById(R.id.jenisTextViewDetail);
        moreBtnMainDetail = findViewById(R.id.moreBtnMainDetail);
        merkTextDetail = findViewById(R.id.merkTextDetail);
        merkViewDetail = findViewById(R.id.merkViewDetail);
        lokasiViewDetail = findViewById(R.id.lokasiViewDetail);
        inventarisTextDetail = findViewById(R.id.inventarisTextDetail);
        inventarisViewDetail = findViewById(R.id.inventarisViewDetail);

        loadPostInfo();

        checkUserStatus();

        loadUserInfo();

        moreBtnMainDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions();
            }
        });
    }

    private void loadUserInfo() {
        //get current user
        Query query = FirebaseDatabase.getInstance().getReference("Users");
        query.orderByChild("uid").equalTo(myUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            myName = ""+ds.child("name").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void showMoreOptions() {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //creating menu pop up
        PopupMenu popupMenu = new PopupMenu(this, moreBtnMainDetail, Gravity.END);

        //show update options
        if (uid.equals(myUid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Updated");

        } else if (uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Delete");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0){
                    Intent intent = new Intent(PostDetailMaintenanceAct.this, UpdateDateAct.class);
                    intent.putExtra("postId",pId);
                    startActivity(intent);
                } else if (id == 1){
                    beginDelete();
                }

                return false;
            }
        });
        popupMenu.show();

    }

    private void beginDelete() {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deletePost();
    }

    private void deletePost() {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostMaintenance");
        Query query = reference.orderByChild("pId").equalTo(postIdMaintenance);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
                //deleted
                Toast.makeText(PostDetailMaintenanceAct.this, "Deleted Successfully....", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostInfo() {

        //get post using the id of the post
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostMaintenance");

        Query query = reference.orderByChild("pId").equalTo(postid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            //keep checking the post until get the required posts
            for (DataSnapshot ds : snapshot.getChildren()){
                //get data
                uid = ""+ds.child("uid").getValue();
                name = ""+ds.child("uName").getValue();
                String email = ""+ds.child("uEmail").getValue();
                String jenis = ""+ds.child("jenisEdit").getValue();
                String merk = ""+ds.child("merkEdit").getValue();
                String lokasi = ""+ds.child("lokasiEdit").getValue();
                String inventaris = ""+ds.child("inventarisEdit").getValue();

                //set data
                nameUserDetail.setText(name);
                jenisTextViewDetail.setText(jenis);
                merkViewDetail.setText(merk);
                lokasiViewDetail.setText(lokasi);
                inventarisViewDetail.setText(inventaris);

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            //user is signed in
            email = user.getEmail();
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
        menu.findItem(R.id.action_logout).setVisible(false);
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
