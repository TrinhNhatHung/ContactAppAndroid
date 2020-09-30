package com.example.contactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contactapp.databinding.ActivityDetailContactBinding;

public class DetailContact extends AppCompatActivity implements DialogFragmentEdit.Listener {
    private ActivityDetailContactBinding binding;
    private static final  int REQUEST_CALL =1;
    private DialogFragmentEdit dialogFragmentEdit;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailContactBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        Intent intent = getIntent();
        contact = (Contact) intent.getSerializableExtra("contact");
        binding.tvName.setText(contact.getName());
        binding.tvPhone.setText(contact.getMobile());
        binding.tvSms.setText(contact.getEmail());
        binding.tvInfo.setText(contact.getInfo());

        binding.btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });

        binding.btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{binding.tvSms.getText().toString()});
                startActivity(emailIntent);
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragmentDialog(contact);
            }
        });
    }

    private void makePhoneCall() {
        String number = binding.tvPhone.getText().toString();
        if (number.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(DetailContact.this,
                    Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(DetailContact.this, new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
            }
        } else {
            Toast.makeText(this, "Not a phone number",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            } else {
                Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFragmentDialog (Contact contact){
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragmentEdit = new DialogFragmentEdit(contact,DetailContact.this);
        dialogFragmentEdit.show(fragmentManager,"abc");
    }

    @Override
    public void onSaveDialog(Contact contact) {
        binding.tvName.setText(contact.getName());
        binding.tvPhone.setText(contact.getMobile());
        binding.tvSms.setText(contact.getEmail());
        binding.tvInfo.setText(contact.getInfo());
    }

}