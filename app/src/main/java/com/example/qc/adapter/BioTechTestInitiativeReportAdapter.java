package com.example.qc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.pojo.BioTechTestInitiativeReportPojo;

import java.util.List;

public class BioTechTestInitiativeReportAdapter extends RecyclerView.Adapter<BioTechTestInitiativeReportAdapter.MyViewHolder> {

    private Context context;
    private List<BioTechTestInitiativeReportPojo> bioTechTestInitiativeReportPojos;

    public BioTechTestInitiativeReportAdapter(Context context, List<BioTechTestInitiativeReportPojo> bioTechTestInitiativeReportPojo) {
        this.context = context;
        this.bioTechTestInitiativeReportPojos = bioTechTestInitiativeReportPojos;
    }

    @NonNull
    @Override
    public BioTechTestInitiativeReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bio_tech_test_report_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BioTechTestInitiativeReportAdapter.MyViewHolder holder, int position) {

        BioTechTestInitiativeReportPojo bio_pojo = bioTechTestInitiativeReportPojos.get(position);
        holder.tv_srno_bio.setText(bio_pojo.getSrno());
        holder.tv_crop_bio.setText(bio_pojo.getCrop());
        holder.tv_spCode_bio.setText(bio_pojo.getSpCode());
        holder.tv_variety_bio.setText(bio_pojo.getVariety());
        holder.tv_sampleno_bio.setText(bio_pojo.getSampleNo());
        holder.tv_lotno_bio.setText(bio_pojo.getLotno());
        holder.tv_ad_bio.setText(bio_pojo.getAd());
        holder.tv_dos_bio.setText(bio_pojo.getDos());
        holder.tv_trd_bio.setText(bio_pojo.getTrd());
    }

    @Override
    public int getItemCount() {
        return bioTechTestInitiativeReportPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_srno_bio, tv_crop_bio, tv_spCode_bio, tv_variety_bio, tv_lotno_bio, tv_sampleno_bio, tv_ad_bio, tv_dos_bio, tv_trd_bio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_srno_bio = itemView.findViewById(R.id.tv_srno_bio);
            tv_crop_bio = itemView.findViewById(R.id.tv_crop_bio);
            tv_spCode_bio = itemView.findViewById(R.id.tv_spCode_bio);
            tv_variety_bio = itemView.findViewById(R.id.tv_variety_bio);
            tv_lotno_bio = itemView.findViewById(R.id.tv_lotno_bio);
            tv_sampleno_bio = itemView.findViewById(R.id.tv_sampleNo_bio);
            tv_ad_bio = itemView.findViewById(R.id.tv_ad_bio);
            tv_dos_bio = itemView.findViewById(R.id.tv_dos_bio);
            tv_trd_bio = itemView.findViewById(R.id.tv_trd_bio);

        }
    }
}
