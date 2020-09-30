package com.example.contactapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class MyViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Contact>> liveData;
    public LiveData<ArrayList<Contact>> getArrayListLiveData (){
        if (liveData == null){
            liveData = new MutableLiveData<ArrayList<Contact>>();
        }
        return liveData;
    }

    public void setValue (ArrayList<Contact> list){

        liveData.setValue(list);
    }

}
