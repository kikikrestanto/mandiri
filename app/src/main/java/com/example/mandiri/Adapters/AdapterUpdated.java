package com.example.mandiri.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandiri.Models.ModelUpdateMaintenance;
import com.example.mandiri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;

public class AdapterUpdated extends RecyclerView.Adapter<AdapterUpdated.MyHolder> {
    Context context;
    List<ModelUpdateMaintenance> updateList;


    public AdapterUpdated(Context context, List<ModelUpdateMaintenance> updateList) {
        this.context = context;
        this.updateList = updateList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_table, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int i) {

        //get the data
        final String uid = updateList.get(i).getUid();
        String uEmail = updateList.get(i).getuEmail();
        final String noText = updateList.get(i).getNoView();
        Long tanggal = updateList.get(i).getTanggalView();
        final String keterangan = updateList.get(i).getTindakanView();
        final String tindakan = updateList.get(i).getKeteranganView();

        String tgl = String.valueOf(tanggal);
        String no = String.valueOf(noText);
        String tin = String.valueOf(tindakan);
        String ket = String.valueOf(keterangan);

        //set data
        holder.nomorTextView.setText(no);
        holder.tanggalTextView.setText(tgl);
        holder.tindakanTextView.setText(tin);
        holder.keteranganTextView.setText(ket);
    }

    @Override
    public int getItemCount() {
        return updateList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView nomorTextView, tanggalTextView, tindakanTextView, keteranganTextView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            nomorTextView = itemView.findViewById(R.id.nomorTextView);
            tanggalTextView = itemView.findViewById(R.id.tanggalTextView);
            tindakanTextView = itemView.findViewById(R.id.tindakanTextView);
            keteranganTextView = itemView.findViewById(R.id.keteranganTextView);
        }


    }
}
