package com.example.qc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.got.GOTObservationPendingReport;
import com.example.qc.pojo.GOTObservationPendingReportPojo;

import java.util.List;

public class GOTObservationPendingReportAdapter extends RecyclerView.Adapter<GOTObservationPendingReportAdapter.MyViewHolder> {

    private Context context;
    private final List<GOTObservationPendingReportPojo> gotObservationPendingReportPojos;

    public GOTObservationPendingReportAdapter(Context context, List<GOTObservationPendingReportPojo> gotObservationPendingReportPojos) {
        this.context = context;
        this.gotObservationPendingReportPojos = gotObservationPendingReportPojos;
    }

    @NonNull
    @Override
    public GOTObservationPendingReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.got_observation_pending_report_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GOTObservationPendingReportAdapter.MyViewHolder holder, int position) {

        GOTObservationPendingReportPojo ob_pojo = gotObservationPendingReportPojos.get(position);
        holder.tv_srno_ob.setText(ob_pojo.getSrno());
        holder.tv_crop_ob.setText(ob_pojo.getCrop());
        holder.tv_spCode_ob.setText(ob_pojo.getSpCode());
        holder.tv_variety_ob.setText(ob_pojo.getVariety());
        holder.tv_lotno_ob.setText(ob_pojo.getLotNo());
        holder.tv_sampleNo_ob.setText(ob_pojo.getSampleNo());
        holder.tv_lotno_ob.setText(ob_pojo.getLotNo());
        holder.tv_tl_ob.setText(ob_pojo.getTL());
        holder.tv_dot_ob.setText(ob_pojo.getDOT());
        holder.tv_bedNo_ob.setText(ob_pojo.getBedNo());
        holder.tv_direction_ob.setText(ob_pojo.getDirection());

    }

    @Override
    public int getItemCount() {
        return gotObservationPendingReportPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_srno_ob, tv_crop_ob, tv_spCode_ob, tv_variety_ob, tv_lotno_ob, tv_sampleNo_ob, tv_tl_ob, tv_dot_ob, tv_bedNo_ob, tv_direction_ob;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_srno_ob = itemView.findViewById(R.id.tv_srno_ob);
            tv_crop_ob = itemView.findViewById(R.id.tv_crop_ob);
            tv_spCode_ob = itemView.findViewById(R.id.tv_spCode_ob);
            tv_variety_ob = itemView.findViewById(R.id.tv_variety_ob);
            tv_lotno_ob =itemView.findViewById(R.id.tv_lotno_ob);
            tv_sampleNo_ob = itemView.findViewById(R.id.tv_sampleNo_ob);
            tv_tl_ob = itemView.findViewById(R.id.tv_tl_ob);
            tv_dot_ob = itemView.findViewById(R.id.tv_dot_ob);
            tv_bedNo_ob = itemView.findViewById(R.id.tv_bedNo_ob);
            tv_direction_ob = itemView.findViewById(R.id.tv_direction_ob);
        }
    }
}
