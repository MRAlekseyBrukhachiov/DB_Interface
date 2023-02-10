package com.example.project_db_interface;

import java.sql.Date;

public class Publishing {
    private int publishing_id;
    private int edition_id;
    private int staff_id;
    private int request_id;
    private String staff_name;
    private String edition_edits;
    private Date date;
    private String results;

    public Publishing(int publishing_id, int edition_id, int staff_id, int request_id, String staff_name,
                      String edition_edits, Date date, String results) {
        this.publishing_id = publishing_id;
        this.edition_id = edition_id;
        this.staff_id = staff_id;
        this.request_id = request_id;
        this.staff_name = staff_name;
        this.edition_edits = edition_edits;
        this.date = date;
        this.results = results;
    }

    public Publishing() {}

    public int getPublishing_id() {
        return publishing_id;
    }

    public void setPublishing_id(int publishing_id) {
        this.publishing_id = publishing_id;
    }

    public int getEdition_id() {
        return edition_id;
    }

    public void setEdition_id(int edition_id) {
        this.edition_id = edition_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getEdition_edits() {
        return edition_edits;
    }

    public void setEdition_edits(String edition_edits) {
        this.edition_edits = edition_edits;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
