package com.example.project_db_interface;

import java.sql.Date;

public class Request {
    private int request_id;
    private int author_id;
    private int status_id;
    private String status_name;
    private String author_name;
    private Date date;
    private String recommendLetter;

    public Request(int request_id, int author_id, int status_id,
                   String status_name, String author_name, Date date, String recommendLetter) {
        this.request_id = request_id;
        this.author_id = author_id;
        this.status_id = status_id;
        this.status_name = status_name;
        this.author_name = author_name;
        this.date = date;
        this.recommendLetter = recommendLetter;
    }

    public Request () {}

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecommendLetter() {
        return recommendLetter;
    }

    public void setRecommendLetter(String recommendLetter) {
        this.recommendLetter = recommendLetter;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    @Override
    public String toString() {
        return "Request{" +
                "request_id=" + request_id +
                ", author_id=" + author_id +
                ", status_id=" + status_id +
                ", status_name='" + status_name + '\'' +
                ", author_name='" + author_name + '\'' +
                ", date=" + date +
                ", recommendLetter='" + recommendLetter + '\'' +
                '}';
    }
}
