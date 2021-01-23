package com.example.mandiri.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandiri.Activites.PostDetailMaintenanceAct;
import com.example.mandiri.Models.ModelAddMaintenance;
import com.example.mandiri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterAddPostMaintenance extends RecyclerView.Adapter<AdapterAddPostMaintenance.MyHolder> {

    Context context;
    List<ModelAddMaintenance> addPost;

    String myUid;

    public AdapterAddPostMaintenance(Context context, List<ModelAddMaintenance> addPost){
        this.context = context;
        this.addPost = addPost;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_maintenance, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int i) {
        //get data
        final String uid = addPost.get(i).getUid();
        String uEmail = addPost.get(i).getuEmail();
        final String pId = addPost.get(i).getpId();
        String uName = addPost.get(i).getuName();
        final String jenisEdit = addPost.get(i).getJenisEdit();
        final String merkEdit = addPost.get(i).getMerkEdit();
        final String lokasiEdit = addPost.get(i).getLokasiEdit();
        final String inventarisEdit = addPost.get(i).getInventarisEdit();

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //set data
        holder.nameUser.setText(uName);
        holder.jenisTextView.setText(jenisEdit);
        holder.merkView.setText(merkEdit);
        holder.lokasiView.setText(lokasiEdit);
        holder.inventarisView.setText(inventarisEdit);

        holder.moreBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.moreBtnMain, uid, myUid,pId);
            }
        });

    }

    private void showMoreOptions(ImageButton moreBtnMain, String uid, String myUid, final String pId) {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //creating menu pop up
        PopupMenu popupMenu = new PopupMenu(context, moreBtnMain, Gravity.END);

        //show delete options
        if (uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE,0,0, "Delete");
        }

        popupMenu.getMenu().add(Menu.NONE,1,0,"View Detail");

        //item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0) {
                    //delete is clicked
                    beginDelete(pId);
                }else if (id == 1){
                    Intent intent = new Intent(context, PostDetailMaintenanceAct.class);
                    intent.putExtra("postId",pId);
                    context.startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void beginDelete(String pId) {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deletePost(pId);
    }

    private void deletePost(String pId) {
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting...");

        Query query = FirebaseDatabase.getInstance().getReference("PostMaintenance").orderByChild("pId").equalTo(pId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().removeValue();
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
    public int getItemCount() {
        return addPost.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        //view from row_maintenance
        LinearLayout nameLinear, merkLinear, lokasiLinear, inventarisLinear;;
        ImageButton moreBtnMain;
        TextView nameUser,nameTextView, jenisTextView,
                 merkText, merkView,
                lokasiText, lokasiView,
                inventarisText, inventarisView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            nameUser = itemView.findViewById(R.id.nameUser);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            jenisTextView = itemView.findViewById(R.id.jenisTextView);
            merkText = itemView.findViewById(R.id.merkText);
            merkView = itemView.findViewById(R.id.merkView);
            lokasiText = itemView.findViewById(R.id.lokasiText);
            lokasiView = itemView.findViewById(R.id.lokasiView);
            inventarisText = itemView.findViewById(R.id.inventarisText);
            inventarisView = itemView.findViewById(R.id.inventarisView);

            nameLinear = itemView.findViewById(R.id.nameLinear);
            merkLinear = itemView.findViewById(R.id.merkLinear);
            lokasiLinear = itemView.findViewById(R.id.lokasiLinear);
            inventarisLinear = itemView.findViewById(R.id.inventarisLinear);

            moreBtnMain = itemView.findViewById(R.id.moreBtnMain);
        }
    }
}
