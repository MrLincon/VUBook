package com.example.vubook.Adapters;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class CR {

    private String thumbImageUrl,name,email,department,semester,section,cr_id,contact;
    private @ServerTimestamp Date timestamp;

    public CR() {
    }

    public CR(String thumbImageUrl, String name, String email, String department, String semester, String section, String cr_id, String contact, Date timestamp) {
        this.thumbImageUrl = thumbImageUrl;
        this.name = name;
        this.email = email;
        this.department = department;
        this.semester = semester;
        this.section = section;
        this.cr_id = cr_id;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCr_id() {
        return cr_id;
    }

    public void setCr_id(String cr_id) {
        this.cr_id = cr_id;
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
