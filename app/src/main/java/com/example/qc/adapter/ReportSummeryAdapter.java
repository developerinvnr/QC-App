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
import com.example.qc.got.ReportSummeryActivity;
import com.example.qc.pojo.ReportSummeryPojo;

import java.util.List;

public class ReportSummeryAdapter extends RecyclerView.Adapter<ReportSummeryAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<ReportSummeryPojo> summeryArray;
    private static List<ReportSummeryPojo> summeryArrayFiltered;
    private static ReportSummeryAdapter.SummeryAdapterListener listener;

    public ReportSummeryAdapter(Context context, List<ReportSummeryPojo> summeryArray, ReportSummeryAdapter.SummeryAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.summeryArray = summeryArray;
        this.summeryArrayFiltered = summeryArray;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtsrno, txtcropname, txtvariety,txtlots;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtsrno = itemView.findViewById(R.id.tv_srno);
            txtcropname = itemView.findViewById(R.id.tv_cropname);
            txtvariety = itemView.findViewById(R.id.tv_variety);
            txtlots = itemView.findViewById(R.id.tv_lots);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected record in callback
                    listener.onContactSelected(summeryArrayFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_summery_row, parent, false);
        return new ReportSummeryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportSummeryAdapter.MyViewHolder holder, int position) {
        final ReportSummeryPojo reportSummeryPojo = summeryArrayFiltered.get(position);
        holder.txtcropname.setText(reportSummeryPojo.getCrop());
        holder.txtvariety.setText(reportSummeryPojo.getVariety());
        holder.txtlots.setText(reportSummeryPojo.getNoofLots());
        holder.txtsrno.setText(String.valueOf(reportSummeryPojo.getSrno()));
    }

    @Override
    public int getItemCount() {
        return summeryArrayFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public interface SummeryAdapterListener {
        void onContactSelected(ReportSummeryPojo reportSummeryPojo);

    }

}
