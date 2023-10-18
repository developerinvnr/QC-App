package com.example.qc.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.qc.R;
import com.example.qc.broadcast.CheckInternetConnectionBroadcast;
import com.example.qc.sharedpreference.SessionManager;

import java.util.HashMap;
import java.util.List;

public class Utils {
    private Context _context;
    private static Utils sSharedPrefs;


    public Utils(Context context) {
        this._context = context;
    }


    public static Utils getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new Utils(context);
        }
        return sSharedPrefs;
    }

    public static Utils getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        //Option 1:
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");

        //Option 2:
        // Alternatively, you can create a new instance here
        // with something like this:
        // getInstance(MyCustomApplication.getAppContext());
    }


    private IntentFilter intentFilter;
    private CheckInternetConnectionBroadcast receiver;
    private Dialog dialog;
    private final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public void initConnectionDetector() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new CheckInternetConnectionBroadcast();
    }

    public void showAlert(Context context, String message) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custompopup_showmessage);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);*/

        TextView text = dialog.findViewById(R.id.tv_message);
        text.setText(message);
        ImageButton okButton = dialog.findViewById(R.id.okbtn);
        Button cancelbtn = dialog.findViewById(R.id.cancelbtn);
        cancelbtn.setVisibility(View.GONE);
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public ArrayAdapter<String> spinnerAdapter(final Context context, List<String> dataArray) {
        HashMap<String, String> map = new SessionManager(context).getMap(SessionManager.KEY_COLORMAP);
        final String colorPrimary = map.get("1");

        return new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, dataArray) {

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextSize(15);
                text.setTypeface(text.getTypeface(), Typeface.BOLD);
                text.setTextColor(context.getResources().getColor(R.color.primary_text));
                return view;

            }

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView text = view.findViewById(android.R.id.text1);
                text.setTextSize(15);
                text.setTypeface(text.getTypeface(), Typeface.BOLD);
                text.setTextColor(context.getResources().getColor(R.color.primary_text));

                return view;

            }
        };
    }
}
