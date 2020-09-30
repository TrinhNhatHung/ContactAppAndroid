package com.example.contactapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
public class DialogFragmentEdit extends DialogFragment {
    private Button btnCancel;
    private Button btnSave;
    private EditText edPhone;
    private EditText edName;
    private EditText edEmail;
    private EditText edInfo;
    private Contact contact;
    private Listener listener;


    public DialogFragmentEdit(Contact contact, Listener listener) {
        this.contact = contact;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_contact, container);
        btnSave = v.findViewById(R.id.btn_save);
        btnCancel =v.findViewById(R.id.btn_cancel);
        edEmail = v.findViewById(R.id.ed_email);
        edInfo = v.findViewById(R.id.ed_info);
        edName = v.findViewById(R.id.ed_name);
        edPhone = v.findViewById(R.id.ed_phone);

        edEmail.setText(contact.getEmail());
        edName.setText(contact.getName());
        edPhone.setText(contact.getMobile());
        edInfo.setText(contact.getInfo());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncDBAccess().execute();
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setRetainInstance(true);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(1000,1600);
    }

    public class AsyncDBAccess extends AsyncTask<Void,Listener,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ContactDatabase contactDatabase = ContactDatabase.getInstance(getContext());
            ContactDao contactDao = contactDatabase.contactDao();
            contact.setName(edName.getText().toString());
            contact.setMobile(edPhone.getText().toString());
            contact.setEmail(edEmail.getText().toString());
            contact.setInfo(edInfo.getText().toString());
            contactDao.updateContact(contact);
            publishProgress(listener);
            return null;
        }

        @Override
        protected void onProgressUpdate(Listener... values) {
            super.onProgressUpdate(values);
            values[0].onSaveDialog(contact);
        }
    }

    public interface Listener {
        void onSaveDialog (Contact contact);
    }
}
