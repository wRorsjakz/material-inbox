package com.example.bottomappbar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bottomappbar.Entity.Email;

import java.util.List;

@Dao
public interface EmailDAO {

    @Insert
    void insert(Email email);

    @Query("SELECT * FROM email_table ORDER BY id ASC")
    LiveData<List<Email>> getAllEmails();

}
