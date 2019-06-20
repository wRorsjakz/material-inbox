package com.example.bottomappbar.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bottomappbar.Entity.Email;
import com.example.bottomappbar.Repository.EmailRepository;

import java.util.List;

public class EmailViewModel extends AndroidViewModel {

    private EmailRepository emailRepository;

    public EmailViewModel(@NonNull Application application) {
        super(application);
        emailRepository = new EmailRepository(application);
    }

    public LiveData<List<Email>> getAllEmails() {
        return emailRepository.getAllEmails();
    }

    public void insert(Email email) {
        emailRepository.insert(email);
    }

}
