package com.example.qc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.qc.R;

import java.util.ArrayList;

public class FMV_DataAdapter extends ArrayAdapter<FMV_Data> {

    private final ArrayList<FMV_Data> fmvDataArray;
    private final Context gotFieldMonitoringVisitActivity;
    private final int custom_fmvdata;

    public FMV_DataAdapter(Context gotFieldMonitoringVisitActivity, int custom_fmvdata, ArrayList<FMV_Data> fmvDataArray) {
        super(gotFieldMonitoringVisitActivity, custom_fmvdata, fmvDataArray);
        this.custom_fmvdata = custom_fmvdata;
        this.gotFieldMonitoringVisitActivity = gotFieldMonitoringVisitActivity;
        this.fmvDataArray = fmvDataArray;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        FMV_Data dailyReportObj = fmvDataArray.get(position);

        Holder holder;
        if (row == null) {

            LayoutInflater inflater = ((Activity) gotFieldMonitoringVisitActivity).getLayoutInflater();
            row = inflater.inflate(custom_fmvdata, parent, false);
            holder = new Holder();

            holder.txtsrno = row.findViewById(R.id.txtsrno);
            holder.txtcrophealth = row.findViewById(R.id.txtcrophealth);
            holder.txtreasons = row.findViewById(R.id.txtreasons);
            holder.txtppn = row.findViewById(R.id.txtppn);

            row.setTag(holder);

        } else {

            holder = (Holder) row.getTag();
        }

        holder.txtsrno.setText(String.valueOf(dailyReportObj.getI()));
        holder.txtcrophealth.setText(dailyReportObj.getFmv_crophealth());
        holder.txtreasons.setText(dailyReportObj.getFmv_reasons());
        holder.txtppn.setText(dailyReportObj.getFmv_noofplants());

        return row;
    }

    public static class Holder {
        TextView txtsrno;
        TextView txtcrophealth;
        TextView txtreasons;
        TextView txtppn;
    }
}
