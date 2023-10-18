package com.example.qc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;
import com.example.qc.pojo.OfflineDataListPojo;
import com.example.qc.pojo.OfflineDataSyncToServer;

import java.util.List;

public class SyncFGTDataToServerAdapter extends RecyclerView.Adapter<SyncFGTDataToServerAdapter.MyViewHolder>{

    private Context context;
    private final List<OfflineDataSyncToServer> pendingList;
    public SyncFGTDataToServerAdapter(Context context, List<OfflineDataSyncToServer> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout ll_main;
        private final TextView tv_srno,tv_crop,tv_variety,tv_lotno,tv_sample,tv_doe;
        private final TextView tv_normal1,tv_normal2,tv_normal3,tv_normal4,tv_normal5,tv_normal6,tv_normal7,tv_normal8;
        private final TextView tv_abnormal1,tv_abnormal2,tv_abnormal3,tv_abnormal4,tv_abnormal5,tv_abnormal6,tv_abnormal7,tv_abnormal8;
        private final TextView tv_dead1,tv_dead2,tv_dead3,tv_dead4,tv_dead5,tv_dead6,tv_dead7,tv_dead8;
        private final TextView tv_rep1,tv_rep2,tv_rep3,tv_rep4,tv_rep5,tv_rep6,tv_rep7,tv_rep8;
        private final TextView tv_normalAvg,tv_abnormalAvg,tv_deadAvg,tv_repAvg,tv_techRemark,tv_vigRemark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            tv_srno = itemView.findViewById(R.id.tv_srno);
            tv_crop = itemView.findViewById(R.id.tv_crop);
            tv_variety = itemView.findViewById(R.id.tv_variety);
            tv_lotno = itemView.findViewById(R.id.tv_lotno);
            tv_sample = itemView.findViewById(R.id.tv_sample);
            tv_doe = itemView.findViewById(R.id.tv_doe);
            tv_normal1 = itemView.findViewById(R.id.tv_normal1);
            tv_normal2 = itemView.findViewById(R.id.tv_normal2);
            tv_normal3 = itemView.findViewById(R.id.tv_normal3);
            tv_normal4 = itemView.findViewById(R.id.tv_normal4);
            tv_normal5 = itemView.findViewById(R.id.tv_normal5);
            tv_normal6 = itemView.findViewById(R.id.tv_normal6);
            tv_normal7 = itemView.findViewById(R.id.tv_normal7);
            tv_normal8 = itemView.findViewById(R.id.tv_normal8);
            tv_normalAvg = itemView.findViewById(R.id.tv_normalAvg);
            tv_abnormal1 = itemView.findViewById(R.id.tv_abnormal1);
            tv_abnormal2 = itemView.findViewById(R.id.tv_abnormal2);
            tv_abnormal3 = itemView.findViewById(R.id.tv_abnormal3);
            tv_abnormal4 = itemView.findViewById(R.id.tv_abnormal4);
            tv_abnormal5 = itemView.findViewById(R.id.tv_abnormal5);
            tv_abnormal6 = itemView.findViewById(R.id.tv_abnormal6);
            tv_abnormal7 = itemView.findViewById(R.id.tv_abnormal7);
            tv_abnormal8 = itemView.findViewById(R.id.tv_abnormal8);
            tv_abnormalAvg = itemView.findViewById(R.id.tv_abnormalAvg);
            tv_dead1 = itemView.findViewById(R.id.tv_dead1);
            tv_dead2 = itemView.findViewById(R.id.tv_dead2);
            tv_dead3 = itemView.findViewById(R.id.tv_dead3);
            tv_dead4 = itemView.findViewById(R.id.tv_dead4);
            tv_dead5 = itemView.findViewById(R.id.tv_dead5);
            tv_dead6 = itemView.findViewById(R.id.tv_dead6);
            tv_dead7 = itemView.findViewById(R.id.tv_dead7);
            tv_dead8 = itemView.findViewById(R.id.tv_dead8);
            tv_deadAvg = itemView.findViewById(R.id.tv_deadAvg);
            tv_rep1 = itemView.findViewById(R.id.tv_rep1);
            tv_rep2 = itemView.findViewById(R.id.tv_rep2);
            tv_rep3 = itemView.findViewById(R.id.tv_rep3);
            tv_rep4 = itemView.findViewById(R.id.tv_rep4);
            tv_rep5 = itemView.findViewById(R.id.tv_rep5);
            tv_rep6 = itemView.findViewById(R.id.tv_rep6);
            tv_rep7 = itemView.findViewById(R.id.tv_rep7);
            tv_rep8 = itemView.findViewById(R.id.tv_rep8);
            tv_repAvg = itemView.findViewById(R.id.tv_repAvg);
            tv_techRemark = itemView.findViewById(R.id.tv_techRemark);
            tv_vigRemark = itemView.findViewById(R.id.tv_vigRemark);

            ll_main.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @NonNull
    @Override
    public SyncFGTDataToServerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_fgt_sync_heading, parent, false);
        return new SyncFGTDataToServerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SyncFGTDataToServerAdapter.MyViewHolder holder, int position) {
        OfflineDataSyncToServer getData = pendingList.get(position);
        holder.tv_srno.setText(String.valueOf(position+1));
        holder.tv_crop.setText(getData.getCrop());
        holder.tv_variety.setText(getData.getVariety());
        holder.tv_lotno.setText(getData.getLotno());
        holder.tv_sample.setText(getData.getSampleno());
        holder.tv_doe.setText(getData.getQcg_fgtdoe());
        holder.tv_normal1.setText(getData.getQcg_vignormal1());
        holder.tv_normal2.setText(getData.getQcg_vignormal2());
        holder.tv_normal3.setText(getData.getQcg_vignormal3());
        holder.tv_normal4.setText(getData.getQcg_vignormal4());
        holder.tv_normal5.setText(getData.getQcg_vignormal5());
        holder.tv_normal6.setText(getData.getQcg_vignormal6());
        holder.tv_normal7.setText(getData.getQcg_vignormal7());
        holder.tv_normal8.setText(getData.getQcg_vignormal8());
        holder.tv_normalAvg.setText(getData.getQcg_vignormalavg());
        holder.tv_abnormal1.setText(getData.getQcg_vigabnormal1());
        holder.tv_abnormal2.setText(getData.getQcg_vigabnormal2());
        holder.tv_abnormal3.setText(getData.getQcg_vigabnormal3());
        holder.tv_abnormal4.setText(getData.getQcg_vigabnormal4());
        holder.tv_abnormal5.setText(getData.getQcg_vigabnormal5());
        holder.tv_abnormal6.setText(getData.getQcg_vigabnormal6());
        holder.tv_abnormal7.setText(getData.getQcg_vigabnormal7());
        holder.tv_abnormal8.setText(getData.getQcg_vigabnormal8());
        holder.tv_abnormalAvg.setText(getData.getQcg_vigabnormalavg());
        holder.tv_dead1.setText(getData.getQcg_vigdead1());
        holder.tv_dead2.setText(getData.getQcg_vigdead2());
        holder.tv_dead3.setText(getData.getQcg_vigdead3());
        holder.tv_dead4.setText(getData.getQcg_vigdead4());
        holder.tv_dead5.setText(getData.getQcg_vigdead5());
        holder.tv_dead6.setText(getData.getQcg_vigdead6());
        holder.tv_dead7.setText(getData.getQcg_vigdead7());
        holder.tv_dead8.setText(getData.getQcg_vigdead8());
        holder.tv_deadAvg.setText(getData.getQcg_vigdeadavg());
        holder.tv_rep1.setText(getData.getQcg_vigoobnormal1());
        holder.tv_rep2.setText(getData.getQcg_vigoobnormal2());
        holder.tv_rep3.setText(getData.getQcg_vigoobnormal3());
        holder.tv_rep4.setText(getData.getQcg_vigoobnormal4());
        holder.tv_rep5.setText(getData.getQcg_vigoobnormal5());
        holder.tv_rep6.setText(getData.getQcg_vigoobnormal6());
        holder.tv_rep7.setText(getData.getQcg_vigoobnormal7());
        holder.tv_rep8.setText(getData.getQcg_vigoobnormal8());
        holder.tv_repAvg.setText(getData.getQcg_vigoobnormalavg());
        holder.tv_techRemark.setText(getData.getQcg_oprremark());
        holder.tv_vigRemark.setText(getData.getQcg_vigvremark());
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

}
