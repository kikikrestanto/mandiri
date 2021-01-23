package com.example.mandiri.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mandiri.Adapters.AdapterComments;
import com.example.mandiri.Models.ModelComment;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostDetailAct extends AppCompatActivity {

    String hisUid, myUid, myEmail, myName, myDp,
            postId, pLikes, hisDp, hisName, pImage, commentCount;

    boolean mProcessComment = false;

    ProgressDialog pd;

    ImageView uPictureIV, pImageIv;
    TextView uNameTv, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv, pCommentsTv;
    ImageButton moreBtn;
    LinearLayout profileLayout;
    RecyclerView recyclerView;

    List<ModelComment> commentList;
    AdapterComments adapterComments;

    EditText commentEt;
    ImageButton sendBtn;
    ImageView cAvatarIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post Detail");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get id of post using intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        uPictureIV = findViewById(R.id.uPictureIv);
        pImageIv = findViewById(R.id.pImageIv);
        uNameTv = findViewById(R.id.uNameTv);
        pTimeTv = findViewById(R.id.pTimeTv);
        //pTitleTv = findViewById(R.id.pTitleTv);
        pDescriptionTv = findViewById(R.id.pDescriptionTv);
        //pLikesTv = findViewById(R.id.pLikesTv);
        pCommentsTv = findViewById(R.id.pCommentsTv);
        moreBtn = findViewById(R.id.moreBtn);
        profileLayout = findViewById(R.id.profileLayout);
        recyclerView = findViewById(R.id.recycleView);

        commentEt = findViewById(R.id.commentEt);
        sendBtn = findViewById(R.id.sendBtn);
        cAvatarIv = findViewById(R.id.cAvatarIv);

        loadPostInfo();

        checkUserStatus();

        loadUserInfo();

       // setLikes();


        //set subtitle of action bar
        actionBar.setSubtitle("SignedIn as: "+myEmail);

        loadComments();

        //send comment button click
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions();

            }
        });


    }

    private void loadComments() {
        //layout for recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        //set layout
        recyclerView.setLayoutManager(layoutManager);

        //init comments list
        commentList = new ArrayList<>();

        //path of the post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelComment modelComment = ds.getValue(ModelComment.class);
                    commentList.add(modelComment);

                    //pass myUid and postId as parameter of constructor of comment adapter


                    //setup adapter
                    adapterComments= new AdapterComments(getApplicationContext(),commentList,myUid, postId);
                    //set adapter
                    recyclerView.setAdapter(adapterComments);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showMoreOptions() {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //creating menu pop up currently having option Delete, will add more options later
        PopupMenu popupMenu = new PopupMenu(this, moreBtn, Gravity.END);


        //show delete options
        if (hisUid.equals(myUid)){
            //add item in menus
            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");

        }

        // item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    //delete is clicked
                    beginDelete();
                }
                return false;
            }
        });
        //show menu
        popupMenu.show();
    }

    private void beginDelete(){
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //post can be with or without image
        if (pImage.equals("noImage")){
            //post is without image
            deleteWithoutImage();
        }
        else{
            //post is with image
            deleteWithImage();
        }
    }

    private void deleteWithImage() {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //progress bar

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");


        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image delete, now deleted database

                        Query fQuery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(postId);
                        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    ds.getRef().removeValue(); // remove values from database where pid matches
                                }
                                //deleted
                                Toast.makeText(PostDetailAct.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // failed, can't go further
                        pd.dismiss();
                        Toast.makeText(PostDetailAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteWithoutImage() {

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //progress bar

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");

        Query fQuery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(postId);
        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue(); // remove values from database where pid matches
                }
                //deleted
                Toast.makeText(PostDetailAct.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void postComment() {
        pd = new ProgressDialog(this);
        pd.setMessage("Adding Comment...");

        //get data from comment edit text
        String comment = commentEt.getText().toString().trim();
        //validate
        if (TextUtils.isEmpty(comment)){
            //no value is entered
            Toast.makeText(this, "Comment is empty...", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        //put into in hasmap
        hashMap.put("cId",timeStamp);
        hashMap.put("comment",comment);
        hashMap.put("timestamp",timeStamp);
        hashMap.put("uid",myUid);
        hashMap.put("uEmail",myEmail);
        hashMap.put("uDp",myDp);
        hashMap.put("uName",myName);

        //put data in db
        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added
                        pd.dismiss();
                        Toast.makeText(PostDetailAct.this, "Comment Added...", Toast.LENGTH_SHORT).show();
                        commentEt.setText("");
                        updateCommentCount();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed, not added
                pd.dismiss();
                Toast.makeText(PostDetailAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCommentCount() {
        //whenver user adds comment increase the comment count as we did for like count
        mProcessComment = true;
        final DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessComment){
                    String comments = ""+ snapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    mProcessComment = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserInfo() {
        //get current user info
        Query myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.orderByChild("uid").equalTo(myUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            myName = ""+ds.child("name").getValue();
                            myDp = ""+ds.child("image").getValue();

                            //set data
                            try {
                                //if image is received then set
                                Picasso.get().load(myDp).placeholder(R.drawable.no_avatar).into(cAvatarIv);
                            }catch (Exception e){
                                Picasso.get().load(R.drawable.no_avatar).into(cAvatarIv);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });;
    }

    private void loadPostInfo() {
        //get post using the id of the post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        Query query = ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //keep checking the posts until get the required posts
                for (DataSnapshot ds : snapshot.getChildren()){
                    //get data
                    //  String pTitle = ""+ds.child("pTitle").getValue();
                    String pDescr = ""+ds.child("pDescr").getValue();
                    //pLikes  = ""+ds.child("pLikes").getValue();
                    String pTimeStamp = ""+ds.child("pTime").getValue();
                    pImage = ""+ds.child("pImage").getValue();
                    hisDp = ""+ds.child("uDp").getValue();
                    hisUid = ""+ds.child("uid").getValue();
                    String uEmail = ""+ds.child("uEmail").getValue();
                    hisName = ""+ds.child("uName").getValue();
                    commentCount = ""+ds.child("pComments").getValue();

                    //convert timestamp dd/mm/yyyy hh:mm am/pm
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

                    //SET data
                    // pTitleTv.setText(pTitle);
                    pDescriptionTv.setText(pDescr);
                    //pLikesTv.setText(pLikes+"Likes");
                    pTimeTv.setText(pTime);
                    pCommentsTv.setText(commentCount+" Comments");

                    uNameTv.setText(hisName);
                    //set image of user who posted
                    //if there is no image i.e pImage.equals("noImage") then hide imageview
                    if (pImage.equals("noImage")){
                        //hide imageview
                        pImageIv.setVisibility(View.GONE);
                    }else {
                        //show imageview
                        pImageIv.setVisibility(View.VISIBLE);
                        try {
                            Picasso.get().load(pImage).into(pImageIv);
                        }catch (Exception e){

                        }
                    }

                    //set user image in comment
                    try {
                        Picasso.get().load(hisDp).into(uPictureIV);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.no_avatar).into(uPictureIV);

                    }

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
