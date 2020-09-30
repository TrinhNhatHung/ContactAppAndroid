package com.example.contactapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM Contact")
    public List<Contact> getAllContact ();

    @Insert
    public void insertContact (Contact contact);

    @Delete
    public void deleteContact (Contact contact);

    @Update
    public void updateContact (Contact contact);
}
