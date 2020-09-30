package com.example.contactapp;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    private FloatingActionButton fab;
    private ArrayList<Contact> contacts= new ArrayList<Contact>();
    private RecyclerView rvContacts;
    private MyAdapter myAdapter;
    private  MyViewModel model;
    private SearchView searchView;
    private  ContactDatabase contactDatabase;
    private ContactDao contactDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.sv_search);
        fab = findViewById(R.id.fab);
        rvContacts = findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        model = new ViewModelProvider(this).get(MyViewModel.class);
        model.getArrayListLiveData().observe(this, new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> list) {
                contacts = list;
                myAdapter = new MyAdapter(contacts,MainActivity.this,model);
                rvContacts.setAdapter(myAdapter);
            }
        });
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, AddContact.class);
                startActivityForResult(intent,123);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTaskDBAccess().execute(model);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 123) {
            Contact contact = (Contact) data.getSerializableExtra("contact");
            contacts.add(contact);
            myAdapter.notifyDataSetChanged();
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    contactDao.insertContact(contact);
                    contacts = (ArrayList<Contact>) contactDao.getAllContact();
                }
            });
            model.setValue(contacts);
        }

    }

    private class AsyncTaskDBAccess extends AsyncTask<MyViewModel,MyViewModel,Void> {

        @Override
        protected Void doInBackground(MyViewModel... myViewModels) {
            contactDatabase = ContactDatabase.getInstance(MainActivity.this);
            contactDao =contactDatabase.contactDao();
            contacts = (ArrayList<Contact>) contactDao.getAllContact();
            publishProgress(myViewModels);
            return null;
        }

        @Override
        protected void onProgressUpdate(MyViewModel... values) {
            super.onProgressUpdate(values);
            model.setValue(contacts);
        }

    }
}