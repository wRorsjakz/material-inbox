package com.example.bottomappbar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "email_table")
public class Email {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String subject;
    private String sender;
    private String content;
    private String recipient;
    private int profilePic;

    public Email(String subject, String sender, String content, String recipient, int profilePic) {
        this.subject = subject;
        this.sender = sender;
        this.content = content;
        this.recipient = recipient;
        this.profilePic = profilePic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getRecipient() {
        return recipient;
    }

    public int getProfilePic() {
        return profilePic;
    }
}
