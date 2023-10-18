package com.example.qc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.pojo.GotReportDetailedPojo;
import com.example.qc.pojo.SowingPendingReportPojo;

import java.util.List;

public class SowingPendingReportAdapter extends RecyclerView.Adapter<SowingPendingReportAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private final List<GotReportDetailedPojo> gotReportDetailedPojos;

    public SowingPendingReportAdapter(Context context, List<GotReportDetailedPojo> gotReportDetailedPojos) {
        this.context = context;
        this.gotReportDetailedPojos = gotReportDetailedPojos;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public SowingPendingReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sowing_pending_report_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SowingPendingReportAdapter.MyViewHolder holder, int position) {

            holder.tv_srno.setText(String.valueOf(gotReportDetailedPojos.get(position).getSrno()));
            holder.tv_doa.setText(gotReportDetailedPojos.get(position).getArrivaldate());
            holder.tv_crop.setText(gotReportDetailedPojos.get(position).getCrop());
            holder.tv_spCode.setText(gotReportDetailedPojos.get(position).getSpcodes());
            holder.tv_variety.setText(gotReportDetailedPojos.get(position).getVariety());
            holder.tv_lotno.setText(gotReportDetailedPojos.get(position).getLotno());
            holder.tv_sample.setText(gotReportDetailedPojos.get(position).getSampleno());
            holder.tv_nob.setText(gotReportDetailedPojos.get(position).getNob());
            holder.tv_qty.setText(gotReportDetailedPojos.get(position).getQty());
            holder.tv_gotStatus.setText(gotReportDetailedPojos.get(position).getGotstatus());
            holder.tv_pdLoc.setText(gotReportDetailedPojos.get(position).getProdloc());
            holder.tv_organiser.setText(gotReportDetailedPojos.get(position).getOrganizer());
            holder.tv_farmer.setText(gotReportDetailedPojos.get(position).getFarmer());
            holder.tv_pp.setText(gotReportDetailedPojos.get(position).getProdper());
            holder.tv_sloc.setText(gotReportDetailedPojos.get(position).getSloc());
    }

    @Override
    public int getItemCount() {
        return gotReportDetailedPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_srno, tv_doa, tv_crop, tv_spCode, tv_variety, tv_lotno, tv_sample, tv_nob, tv_qty, tv_gotStatus, tv_pdLoc, tv_organiser, tv_farmer, tv_pp, tv_sloc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_srno = itemView.findViewById(R.id.tv_srno);
            tv_doa = itemView.findViewById(R.id.tv_doa);
            tv_crop = itemView.findViewById(R.id.tv_crop);
            tv_spCode = itemView.findViewById(R.id.tv_spcode);
            tv_variety = itemView.findViewById(R.id.tv_variety);
            tv_lotno = itemView.findViewById(R.id.tv_lotno);
            tv_sample = itemView.findViewById(R.id.tv_sample);
            tv_nob = itemView.findViewById(R.id.tv_nob);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_gotStatus = itemView.findViewById(R.id.tv_gotStatus);
            tv_pdLoc = itemView.findViewById(R.id.tv_pdLoc);
            tv_organiser = itemView.findViewById(R.id.tv_organiser);
            tv_farmer = itemView.findViewById(R.id.tv_farmer);
            tv_pp = itemView.findViewById(R.id.tv_pp);
            tv_sloc = itemView.findViewById(R.id.tv_sloc);

        }
    }
}
