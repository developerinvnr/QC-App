package com.example.qc.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.qc.R;
import com.example.qc.bts_seedling.BTSSeedlingDispatchHomeActivity;
import com.example.qc.bts_seedling.HomeBTSSeedlingActivity;
import com.example.qc.dencity.DencityHomeActivity;
import com.example.qc.germination.GerminationHomeActivity;
import com.example.qc.germination.ImageUploadActivity;
import com.example.qc.got.ReportHomeActivity;
import com.example.qc.got.ReviewAndFinalSubmitActivity;
import com.example.qc.got.SampleGOTResultHomeActivity;
import com.example.qc.got.SampleReceiptHomeActivity;
import com.example.qc.moisture.MoistureHomeActivity;
import com.example.qc.pojo.LoginResponse;
import com.example.qc.pp.PPHomeActivity;
import com.example.qc.samplecollection.SampleCollectionHomeActivity;
import com.example.qc.sharedpreference.SharedPreferences;
import com.example.qc.utils.ConnectionDetector;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton btn_samplecollection = root.findViewById(R.id.btn_samplecollection);
        ImageButton btn_moisture = root.findViewById(R.id.btn_moisture);
        ImageButton btn_pp = root.findViewById(R.id.btn_pp);
        ImageButton btn_germination = root.findViewById(R.id.btn_germination);
        ImageButton btn_sampleack = root.findViewById(R.id.btn_sampleack);
        ImageButton btn_gotresultupdate = root.findViewById(R.id.btn_gotresultupdate);
        ImageButton btn_btsseedling = root.findViewById(R.id.btn_btsseedling);
        ImageButton btn_btsseedlingdispatch = root.findViewById(R.id.btn_btsseedlingdispatch);
        ImageButton btn_dencity = root.findViewById(R.id.btn_dencity);
        ImageButton btn_got_report = root.findViewById(R.id.btn_got_report);
        ImageButton btn_photoUpload = root.findViewById(R.id.btn_photoUpload);
        ImageButton btn_reviewSubmit = root.findViewById(R.id.btn_reviewSubmit);
        ImageButton btn_offlineFGT = root.findViewById(R.id.btn_offlineFGT);
        CardView cv_samplecollection = root.findViewById(R.id.cv_samplecollection);
        CardView cv_moisture = root.findViewById(R.id.cv_moisture);
        CardView cv_pp = root.findViewById(R.id.cv_pp);
        CardView cv_germination = root.findViewById(R.id.cv_germination);
        CardView cv_photoUpload = root.findViewById(R.id.cv_photoUpload);
        LinearLayout ll_samplecollection = root.findViewById(R.id.ll_samplecollection);
        LinearLayout ll_pp_germ = root.findViewById(R.id.ll_pp_germ);
        LinearLayout ll_recplant = root.findViewById(R.id.ll_recplant);
        LinearLayout ll_btsseedling = root.findViewById(R.id.ll_btsseedling);
        LinearLayout ll_dencity = root.findViewById(R.id.ll_dencity);
        LinearLayout ll_got_report = root.findViewById(R.id.ll_got_report);
        LinearLayout ll_photoUpload = root.findViewById(R.id.ll_photoUpload);
        LinearLayout ll_review = root.findViewById(R.id.ll_review);
        LinearLayout ll_offlineFGT = root.findViewById(R.id.ll_offlineFGT);
        SharedPreferences.getInstance(getContext());
        LoginResponse loginRes = (LoginResponse) SharedPreferences.getInstance().getObject(SharedPreferences.KEY_LOGIN_OBJ, LoginResponse.class);
        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            if (loginRes.getRole().equalsIgnoreCase("qcappmp")) {
                cv_samplecollection.setVisibility(View.VISIBLE);
                cv_moisture.setVisibility(View.VISIBLE);
                cv_pp.setVisibility(View.VISIBLE);
                cv_germination.setVisibility(View.GONE);
                ll_recplant.setVisibility(View.GONE);
                ll_btsseedling.setVisibility(View.GONE);
                ll_dencity.setVisibility(View.VISIBLE);
                ll_photoUpload.setVisibility(View.VISIBLE);
            } else if (loginRes.getRole().equalsIgnoreCase("qcappgermp")) {
                cv_samplecollection.setVisibility(View.GONE);
                cv_moisture.setVisibility(View.GONE);
                cv_pp.setVisibility(View.GONE);
                ll_recplant.setVisibility(View.GONE);
                ll_btsseedling.setVisibility(View.GONE);
                ll_dencity.setVisibility(View.GONE);
                ll_review.setVisibility(View.GONE);
                cv_germination.setVisibility(View.VISIBLE);
                ll_photoUpload.setVisibility(View.VISIBLE);
            } else if (loginRes.getRole().equalsIgnoreCase("qcappgot")) {
                cv_samplecollection.setVisibility(View.GONE);
                cv_moisture.setVisibility(View.GONE);
                cv_pp.setVisibility(View.GONE);
                ll_recplant.setVisibility(View.VISIBLE);
                ll_review.setVisibility(View.VISIBLE);
                ll_btsseedling.setVisibility(View.GONE);
                cv_germination.setVisibility(View.GONE);
                ll_dencity.setVisibility(View.GONE);
                ll_photoUpload.setVisibility(View.GONE);
                ll_got_report.setVisibility(View.VISIBLE);
            } else if (loginRes.getRole().equalsIgnoreCase("qcbts")) {
                cv_samplecollection.setVisibility(View.GONE);
                cv_moisture.setVisibility(View.GONE);
                cv_pp.setVisibility(View.GONE);
                ll_recplant.setVisibility(View.GONE);
                cv_germination.setVisibility(View.GONE);
                ll_dencity.setVisibility(View.GONE);
                ll_photoUpload.setVisibility(View.GONE);
                ll_review.setVisibility(View.GONE);
                ll_btsseedling.setVisibility(View.VISIBLE);
            } else if (loginRes.getRole().equalsIgnoreCase("qcdencity")) {
                ll_samplecollection.setVisibility(View.GONE);
                ll_pp_germ.setVisibility(View.GONE);
                ll_recplant.setVisibility(View.GONE);
                ll_btsseedling.setVisibility(View.GONE);
                ll_photoUpload.setVisibility(View.GONE);
                ll_review.setVisibility(View.GONE);
            } else if (loginRes.getRole().equalsIgnoreCase("qcgotview")) {
                cv_samplecollection.setVisibility(View.GONE);
                cv_moisture.setVisibility(View.GONE);
                cv_pp.setVisibility(View.GONE);
                ll_recplant.setVisibility(View.GONE);
                ll_btsseedling.setVisibility(View.GONE);
                cv_germination.setVisibility(View.GONE);
                ll_dencity.setVisibility(View.GONE);
                ll_photoUpload.setVisibility(View.GONE);
                ll_got_report.setVisibility(View.VISIBLE);
                ll_review.setVisibility(View.VISIBLE);
            } else {

            }
        }else {
            cv_samplecollection.setVisibility(View.GONE);
            cv_moisture.setVisibility(View.GONE);
            cv_pp.setVisibility(View.GONE);
            ll_recplant.setVisibility(View.GONE);
            ll_btsseedling.setVisibility(View.GONE);
            cv_germination.setVisibility(View.GONE);
            ll_dencity.setVisibility(View.GONE);
            ll_photoUpload.setVisibility(View.GONE);
            ll_got_report.setVisibility(View.GONE);
            ll_review.setVisibility(View.GONE);
            ll_offlineFGT.setVisibility(View.VISIBLE);
        }

        btn_offlineFGT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GerminationHomeActivity.class);
                startActivity(intent);
            }
        });

        btn_samplecollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SampleCollectionHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_photoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageUploadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_reviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewAndFinalSubmitActivity.class);
                startActivity(intent);
            }
        });

        btn_moisture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoistureHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PPHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_germination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GerminationHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_sampleack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SampleReceiptHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_gotresultupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SampleGOTResultHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_btsseedling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeBTSSeedlingActivity.class);
                startActivity(intent);
            }
        });
        btn_btsseedlingdispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BTSSeedlingDispatchHomeActivity.class);
                startActivity(intent);
            }
        });
        btn_dencity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DencityHomeActivity.class);
                startActivity(intent);
            }
        });
        btn_got_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportHomeActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}