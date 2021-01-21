package com.example.mandiri.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandiri.Models.ModelUpdateMaintenance;
import com.example.mandiri.R;

import java.util.Calendar;
import java.util.List;

public class AdapterUpdated extends RecyclerView.Adapter<AdapterUpdated.MyHolder> {
    Context context;
    List<ModelUpdateMaintenance> modelUpdateMaintenances;
    String myUid, postId;

    public AdapterUpdated(Context context, List<ModelUpdateMaintenance> modelUpdateMaintenances, String myUid, String postId) {
        this.context = context;
        this.modelUpdateMaintenances = modelUpdateMaintenances;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public AdapterUpdated.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_table, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUpdated.MyHolder holder, int i) {
        //get the data
        final String uid = modelUpdateMaintenances.get(i).getUid();
        String pid = modelUpdateMaintenances.get(i).getpId();
        String noText = modelUpdateMaintenances.get(i).getNoText();
        String tanggal = modelUpdateMaintenances.get(i).getTanggalText();
        String keterangan = modelUpdateMaintenances.get(i).getKetText();
        String tindakan = modelUpdateMaintenances.get(i).getTindakanText();

        //set data
        holder.nomorTextView.setText(noText);
        holder.keteranganTextView.setText(keterangan);
        holder.tindakanTextView.setText(tindakan);
        holder.tanggalTextView.setText(tanggal);
    }

    @Override
    public int getItemCount() {
        return modelUpdateMaintenances.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView nomorTextView, tanggalTextView, keteranganTextView, tindakanTextView;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            nomorTextView = itemView.findViewById(R.id.nomorTextView);
            tanggalTextView = itemView.findViewById(R.id.tanggalTextView);
            keteranganTextView = itemView.findViewById(R.id.keteranganTextView);
            tindakanTextView = itemView.findViewById(R.id.tindakanTextView);
        }


    }
}
