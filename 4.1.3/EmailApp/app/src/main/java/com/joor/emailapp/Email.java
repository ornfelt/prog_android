package com.joor.emailapp;

//this class is a model of an Email - you can create an object of this class which will containt the incoming email data
public class Email {

    //variables
    private String date;
    private String[] from;
    private String subject;
    private String message;

    //constructor setting variables for this object
    public Email(String date, String[] from, String subject, String message){
        this.date = date;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

    /* getters and setters */

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getFrom() {
        return from;
    }

    public void setFrom(String[] from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
