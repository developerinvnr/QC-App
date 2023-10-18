package com.example.qc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.pojo.OfflineDataListPojo;

import java.util.List;

public class OfflineFGTDataListAdapter extends RecyclerView.Adapter<OfflineFGTDataListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private final List<OfflineDataListPojo> pendingList;
    public OfflineFGTDataListAdapter(Context context, List<OfflineDataListPojo> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout ll_main;
        private final TextView tv_crop,tv_variety,tv_lotno,tv_sample,tv_setupdt,tv_stage,tv_srno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            tv_srno = itemView.findViewById(R.id.tv_srno);
            tv_crop = itemView.findViewById(R.id.tv_crop);
            tv_variety = itemView.findViewById(R.id.tv_variety);
            tv_lotno = itemView.findViewById(R.id.tv_lotno);
            tv_sample = itemView.findViewById(R.id.tv_sample);
            tv_setupdt = itemView.findViewById(R.id.tv_setupdt);
            tv_stage = itemView.findViewById(R.id.tv_stage);

            ll_main.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_fgt_sample_heading, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OfflineDataListPojo getData = pendingList.get(position);
        holder.tv_srno.setText(String.valueOf(position+1));
        holder.tv_crop.setText(getData.getCrop());
        holder.tv_variety.setText(getData.getVariety());
        holder.tv_lotno.setText(getData.getLotno());
        holder.tv_sample.setText(getData.getSampleno());
        holder.tv_setupdt.setText(getData.getQcgSetupdt());
        holder.tv_stage.setText(getData.getTrstage());
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


}
