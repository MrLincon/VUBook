package com.example.vubook.Adapters;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Teacher {

    private String thumbImageUrl,name,email,designation,department,office,counseling_hour,contact;
    private @ServerTimestamp Date timestamp;

    public Teacher() {
    }

    public Teacher(String thumbImageUrl, String name, String email, String designation, String department, String office, String counseling_hour, String contact, Date timestamp) {
        this.thumbImageUrl = thumbImageUrl;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.department = department;
        this.office = office;
        this.counseling_hour = counseling_hour;
        this.contact = contact;
        this.timestamp = timestamp;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCounseling_hour() {
        return counseling_hour;
    }

    public void setCounseling_hour(String counseling_hour) {
        this.counseling_hour = counseling_hour;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
