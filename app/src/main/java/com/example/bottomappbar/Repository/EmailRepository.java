package com.example.bottomappbar.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.bottomappbar.DAO.EmailDAO;
import com.example.bottomappbar.Entity.Email;
import com.example.bottomappbar.Room.EmailDatabase;

import java.util.List;

public class EmailRepository {

    private EmailDatabase emailDatabase;
    private EmailDAO emailDAO;

    public EmailRepository(Application application) {
        emailDatabase = EmailDatabase.getInstance(application);
        emailDAO = emailDatabase.emailDAO();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void insert(Email email) {
        emailDAO.insert(email);
    }

    private static class InsertAsyncTask extends AsyncTask<Email, Void, Void> {

        private EmailDAO emailDAO;

        public InsertAsyncTask(EmailDAO emailDAO) {
            this.emailDAO = emailDAO;
        }

        @Override
        protected Void doInBackground(Email... emails) {
            emailDAO.insert(emails[0]);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<Email>> getAllEmails() {
        return emailDAO.getAllEmails();
    }

}
