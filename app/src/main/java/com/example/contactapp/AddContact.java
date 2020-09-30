package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class AddContact extends AppCompatActivity {
    private EditText edtName;
    private EditText edtPhone;
    private EditText edtEmail;
    private EditText edtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        edtEmail = findViewById(R.id.edt_email);
        edtInfo = findViewById(R.id.edt_info);
        edtPhone = findViewById(R.id.edt_phone);
        edtName = findViewById(R.id.edt_name);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_done:
                Contact contact = new Contact(edtName.getText().toString(), edtEmail.getText().toString(),
                                                edtPhone.getText().toString(), edtInfo.getText().toString());
                Intent returnedIntent = new Intent();
                returnedIntent.putExtra("contact", contact);
                setResult(RESULT_OK,returnedIntent);
                finish();
               return true;
            case R.id.mi_back:
                Intent returnedIntent1 = new Intent();
                setResult(RESULT_CANCELED,returnedIntent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}