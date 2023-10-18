package com.example.qc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.got.ReviewAndFinalSubmitActivity;
import com.example.qc.parser.gotpendinglist.Samparray;
import com.example.qc.pojo.ReportSummeryPojo;

import java.util.List;

public class DataReviewAdapter extends RecyclerView.Adapter<DataReviewAdapter.MyViewHolder>{
    private Context context;
    private List<Samparray> pendingList;
    private static List<Samparray> pendingListFiltered;
    private static DataReviewAdapter.DataReviewAdapterListener listener;
    private int index;

    public DataReviewAdapter(Context context, List<Samparray> pendingList, DataReviewAdapter.DataReviewAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.pendingList = pendingList;
        this.pendingListFiltered = pendingList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout ll_main;
        private final TextView txtsrno,tv_resultDate,tv_sampleno,tv_ppn,tv_female,tv_male,tv_oofType,tv_remark,tv_gpper,tv_view;
        private final Button btn_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            txtsrno = itemView.findViewById(R.id.txtsrno);
            tv_resultDate = itemView.findViewById(R.id.tv_resultDate);
            tv_sampleno = itemView.findViewById(R.id.tv_sampleno);
            tv_ppn = itemView.findViewById(R.id.tv_ppn);
            tv_female = itemView.findViewById(R.id.tv_female);
            tv_male = itemView.findViewById(R.id.tv_male);
            tv_oofType = itemView.findViewById(R.id.tv_oofType);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_gpper = itemView.findViewById(R.id.tv_gpper);
            tv_view = itemView.findViewById(R.id.tv_view);
            btn_view = itemView.findViewById(R.id.btn_view);

            ll_main.setBackgroundColor(Color.parseColor("#FFFFFF"));

            tv_view.setVisibility(View.GONE);
            btn_view.setVisibility(View.VISIBLE);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send selected contact in callback
                    listener.onContactSelected(pendingList.get(getAdapterPosition()));
                }
            });*/
        }
    }

    @NonNull
    @Override
    public DataReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.finalsubmit_heading, parent, false);
        return new DataReviewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataReviewAdapter.MyViewHolder holder, int position) {
        Samparray data = pendingList.get(position);
        holder.txtsrno.setText(String.valueOf(position+1));
        holder.tv_resultDate.setText(data.getFnobserObserdate());
        holder.tv_sampleno.setText(data.getSampleno());
        holder.tv_ppn.setText(String.valueOf(data.getFnobserNoofplants()));
        holder.tv_female.setText(String.valueOf(data.getFnobserFemaleplants()));
        holder.tv_male.setText(String.valueOf(data.getFnobserMaleplants()));
        holder.tv_oofType.setText(String.valueOf(data.getFnobserOtherofftype()));
        holder.tv_remark.setText(data.getFnobserRemarks());
        holder.tv_gpper.setText(String.valueOf(data.getGppercentage()));

        holder.btn_view.setOnClickListener(view -> {
            index = position;
            ((ReviewAndFinalSubmitActivity) context).viewSampleDetails(pendingList.get(index));
        });
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public interface DataReviewAdapterListener {
        void onContactSelected(Samparray samparray);
    }

}
