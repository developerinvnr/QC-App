package com.example.qc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.pojo.GotReportDetailedPojo;
import com.example.qc.pojo.TransplantingPendingReportPojo;

import java.util.ArrayList;
import java.util.List;

public class TransplantingPendingReportAdapter extends RecyclerView.Adapter<TransplantingPendingReportAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<GotReportDetailedPojo> gotReportDetailedPojos =  new ArrayList<>();

    public TransplantingPendingReportAdapter(Context context, List<GotReportDetailedPojo> gotReportDetailedPojos) {
        this.context = context;
        this.gotReportDetailedPojos = gotReportDetailedPojos;
    }

    @NonNull
    @Override
    public TransplantingPendingReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.transplanting_pending_report_row, parent, false);
        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull TransplantingPendingReportAdapter.MyViewHolder holder, int position) {

        final GotReportDetailedPojo grdp_pojo = gotReportDetailedPojos.get(position);
        holder.tv_srno_tp.setText(String.valueOf(grdp_pojo.getSrno()));
        holder.tv_doa_tp.setText(grdp_pojo.getArrivaldate());
        holder.tv_crop_tp.setText(grdp_pojo.getCrop());
        holder.tv_variety_tp.setText(grdp_pojo.getVariety());
        holder.tv_spCode_tp.setText(grdp_pojo.getSpcodes());
        holder.tv_lotno_tp.setText(grdp_pojo.getLotno());
        holder.tv_sample_tp.setText(grdp_pojo.getSampleno());
        holder.tv_dos_tp.setText(grdp_pojo.getDateofsowing());
        holder.tv_nl_tp.setText(grdp_pojo.getSownurseryloc());
        holder.tv_bedno_tp.setText(grdp_pojo.getTpbedno());
        holder.tv_treyno_tp.setText(grdp_pojo.getSowtrayno());
        holder.tv_nooftrey_tp.setText(grdp_pojo.getSownooftray());

    }

    @Override
    public int getItemCount() {
            return gotReportDetailedPojos.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_srno_tp, tv_doa_tp, tv_crop_tp, tv_spCode_tp, tv_variety_tp, tv_lotno_tp, tv_sample_tp, tv_dos_tp, tv_nl_tp, tv_bedno_tp, tv_treyno_tp, tv_nooftrey_tp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_srno_tp = itemView.findViewById(R.id.tv_srno_tp);
            tv_doa_tp = itemView.findViewById(R.id.tv_doa_tp);
            tv_crop_tp = itemView.findViewById(R.id.tv_crop_tp);
            tv_spCode_tp = itemView.findViewById(R.id.tv_spCrop_tp);
            tv_variety_tp = itemView.findViewById(R.id.tv_variety_tp);
            tv_lotno_tp = itemView.findViewById(R.id.tv_lotno_tp);
            tv_sample_tp = itemView.findViewById(R.id.tv_sample_tp);
            tv_dos_tp = itemView.findViewById(R.id.tv_dos_tp);
            tv_nl_tp = itemView.findViewById(R.id.tv_nl_tp);
            tv_bedno_tp = itemView.findViewById(R.id.tv_bedno_tp);
            tv_treyno_tp = itemView.findViewById(R.id.tv_treyno_tp);
            tv_nooftrey_tp = itemView.findViewById(R.id.tv_nooftrey_tp);
        }
    }
}
