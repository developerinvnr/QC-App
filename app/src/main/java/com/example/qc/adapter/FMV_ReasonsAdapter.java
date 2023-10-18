package com.example.qc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qc.R;

import java.util.ArrayList;
import java.util.List;

public class FMV_ReasonsAdapter extends RecyclerView.Adapter<FMV_ReasonsAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<FMV_Reasons> contactList;
    private List<FMV_Reasons> contactListFiltered;
    private FMV_ReasonsAdapter.ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkbox;
        public TextView txtsrno, txtreasons;


        public MyViewHolder(View view) {
            super(view);
            txtsrno = view.findViewById(R.id.txtsrno);
            txtreasons = view.findViewById(R.id.txtreasons);
            checkbox = view.findViewById(R.id.checkbox);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public FMV_ReasonsAdapter(Context context, List<FMV_Reasons> contactList, FMV_ReasonsAdapter.ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public FMV_ReasonsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_fmvreasonslist, parent, false);

        return new FMV_ReasonsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FMV_ReasonsAdapter.MyViewHolder holder, final int position) {
        final FMV_Reasons contact = contactListFiltered.get(position);
        String srno = String.valueOf(contact.getI());
        if (srno.equalsIgnoreCase("0")) {
            holder.txtsrno.setText("");
        } else {
            holder.txtsrno.setText(srno);
        }

        holder.txtreasons.setText(contact.getReason_name());

        holder.checkbox.setChecked(contact.isCheck);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FMV_Reasons contact = contactListFiltered.get(holder.getAdapterPosition());
                contact.setCheck(holder.checkbox.isChecked());
                listener.onCheckBoxCheck(contact, holder.checkbox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<FMV_Reasons> filteredList = new ArrayList<>();
                    for (FMV_Reasons row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getReason_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<FMV_Reasons>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(FMV_Reasons contact);
        void onCheckBoxCheck(FMV_Reasons contact, boolean ischeck);
    }
}
