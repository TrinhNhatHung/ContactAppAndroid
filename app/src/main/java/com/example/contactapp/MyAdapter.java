package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private static ArrayList<Contact> mContacts;
    private ArrayList<Contact> mContactsAll;
    private static Context context;
    private static MyViewModel model;
    public MyAdapter(ArrayList<Contact> mContacts,Context context,MyViewModel model) {
        this.mContacts = mContacts;
        this.context = context;
        this.mContactsAll = new ArrayList<Contact>(mContacts);
        this.model = model;

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(mContacts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Contact> filtered = new ArrayList<Contact>();
            if (charSequence.toString().isEmpty()){
                filtered.addAll(mContactsAll);
            } else {
                for(Contact contact : mContactsAll){
                    if (contact.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filtered.add(contact);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mContacts.clear();
            mContacts.addAll((Collection<? extends Contact>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View view;
        public TextView tvName;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            tvName = this.view.findViewById(R.id.tv_name);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, DetailContact.class);
            intent.putExtra("contact", mContacts.get(getAdapterPosition()));
            intent.putExtra("contacts",mContacts);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            new AsyncTaskDBAccess().execute(model);
            return true;
        }

        public class AsyncTaskDBAccess extends AsyncTask<MyViewModel,MyViewModel,Void> {

            @Override
            protected Void doInBackground(MyViewModel... myViewModels) {
                Contact contact = mContacts.get(getAdapterPosition());
                ContactDatabase contactDatabase = ContactDatabase.getInstance(context);
                ContactDao contactDao = contactDatabase.contactDao();
                contactDao.deleteContact(contact);
                mContacts = (ArrayList<Contact>) contactDao.getAllContact();
                publishProgress(myViewModels);
                return null;
            }

            @Override
            protected void onProgressUpdate(MyViewModel... values) {
                super.onProgressUpdate(values);
                model.setValue(mContacts);
            }
        }
    }

}
