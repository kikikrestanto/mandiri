package com.example.mandiri.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandiri.Activites.ImageViewAct;
import com.example.mandiri.Activites.PostDetailAct;
import com.example.mandiri.Models.ModelPost;
import com.example.mandiri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    Context context;
    List<ModelPost> postList;

    String myUid, pimage;
    String postImage;

    private DatabaseReference likeRef; //for likes database node
    private DatabaseReference postRef; //reference of posts

    private Activity activity;

    public AdapterPosts(Context context, List<ModelPost> postList){
        this.context= context;
        this.postList = postList;

        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts,parent,false);

        return new MyHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        //get data
        final String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName = postList.get(i).getuName();
        String uDp = postList.get(i).getuDp();
        final String pId = postList.get(i).getpId();
        final String pDescr = postList.get(i).getpDescr();
        final String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();
        String pComments = postList.get(i).getpComments(); //contains total number of comments for a post

        //convert timestamp dd/mm/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set data
        myHolder.uNameTv.setText(uName);
        myHolder.pTimeTv.setText(pTime);
        //myHolder.pTitleTv.setText(pTitle);
        myHolder.pDescriptionTv.setText(pDescr);
        myHolder.pCommentsTv.setText(pComments+" Comments"); // example 10 comments

        //Set user dp

        try {
            Picasso.get().load(uDp).placeholder(R.drawable.no_avatar).into(myHolder.uPictureIv);
        }catch (Exception e){

        }

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //set image
        //if there is no image i.e pImage.equals("noImage") then hide imageview
        if (pImage.equals("noImage")){
            //hide imageview
            myHolder.pImageIv.setVisibility(View.GONE);
        }else {
            //show imageview
            myHolder.pImageIv.setVisibility(View.VISIBLE);
            try {
                Picasso.get().load(pImage).into(myHolder.pImageIv);
            }catch (Exception e){

            }
        }

        //handle button
        myHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailAct.class);
                intent.putExtra("postId",pId); //wil get detail of post using id
                context.startActivity(intent);
            }
        });

        //handle button
        myHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(myHolder.moreBtn, uid, myUid, pId, pImage);
            }
        });

        myHolder.pImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewAct.class);
                intent.putExtra("pImage", pImage);
                context.startActivity(intent);
            }
        });


    }

    private void showMoreOptions(ImageButton moreBtn, String uid, String myUid, final String pId, final String pImage) {

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //creating menu pop up currently having option Delete, will add more options later
        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);


        //show delete options
        if (uid.equals(myUid)){
            //add item in menus
            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");

        }

        popupMenu.getMenu().add(Menu.NONE,1 ,0,"View Detail");

        // item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    //delete is clicked
                    beginDelete(pId, pImage);
                } else if(id==1){
                    Intent intent = new Intent(context, PostDetailAct.class);
                    intent.putExtra("postId",pId); //wil get detail of post using id
                    context.startActivity(intent);
                }
                return false;
            }
        });
        //show menu
        popupMenu.show();
    }

    private void beginDelete(String pId, String pImage) {

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //post can be with or without image
        if (pImage.equals("noImage")){
            //post is without image
            deleteWithoutImage(pId);
        }
        else{
            //post is with image
            deleteWithImage(pId,pImage);
        }
    }

    private void deleteWithImage(final String pId, String pImage) {

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //progress bar

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");


        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image delete, now deleted database

                        Query fQuery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    ds.getRef().removeValue(); // remove values from database where pid matches
                                }
                                //deleted
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteWithoutImage(String pId) {

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //progress bar

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");

        Query fQuery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue(); // remove values from database where pid matches
                }
                //deleted
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {return postList.size(); }

    class MyHolder extends RecyclerView.ViewHolder{
        //views from row_post.xml
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pDescriptionTv,pCommentsTv;
        ImageButton moreBtn;
        LinearLayout profileLayout;
        Button commentBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            pImageIv = itemView.findViewById(R.id.pImageIv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            profileLayout = itemView.findViewById(R.id.profileLayout);
            pCommentsTv = itemView.findViewById(R.id.pCommentsTv);
            commentBtn = itemView.findViewById(R.id.commentBtn);

        }
    }
}
