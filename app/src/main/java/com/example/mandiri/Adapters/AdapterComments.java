package com.example.mandiri.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandiri.Models.ModelComment;
import com.example.mandiri.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder> {

    Context context;
    List<ModelComment> commentList;
    String myUid, postId;

    public AdapterComments(Context context, List<ModelComment> commentList, String myUid, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //bind the row comment
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments,viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        //get the data
        final String uid = commentList.get(i).getUid();
        String name = commentList.get(i).getuName();
        String email = commentList.get(i).getuEmail();
        String image = commentList.get(i).getuDp();
        final String cid = commentList.get(i).getcId();
        String comment = commentList.get(i).getComment();
        String timestamp = commentList.get(i).getTimestamp();

        //convert waktu
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String cTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set the data
        holder.nameCv.setText(name);
        holder.commentCv.setText(comment);
        holder.timeCv.setText(cTime);
        //set user dp
        try {
            Picasso.get().load(image).placeholder(R.drawable.no_avatar).into(holder.avatarCv);
        }catch (Exception e){
            //comment click listener
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check if this comment is by currently signet is user or not
                    if (myUid.equals(uid)){
                        //my comment
                        //show dialog delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Delete");
                        builder.setMessage("Are you delete this comment ?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete comment
                                deleteComment(cid);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //dismiss dialog
                                dialogInterface.dismiss();
                            }
                        });
                        //show dialog
                        builder.create().show();
                    }else{
                        //no my comment
                        Toast.makeText(context, "Can't delete this comment...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void deleteComment(String cid) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("Comments").child(cid).removeValue(); // it will delete comment

        //now update comment count
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String comments = ""+ snapshot.child("pComments").getValue();
                int newCommentVal = Integer.parseInt(comments) - 1;
                ref.child("pComments").setValue(""+newCommentVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //declare views from xml
        ImageView avatarCv;
        TextView nameCv, commentCv, timeCv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            avatarCv = itemView.findViewById(R.id.avatarCv);
            nameCv = itemView.findViewById(R.id.nameCv);
            commentCv = itemView.findViewById(R.id.commentCv);
            timeCv = itemView.findViewById(R.id.timeCv);


        }
    }
}
