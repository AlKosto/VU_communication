package com.example.imran.vucommunication;

public class Comments {

    String comment, date,time, id, cid, image;

    public Comments() {
    }

    public Comments(String comment, String date, String time, String id, String cid, String image) {
        this.comment = comment;
        this.cid = cid;
        this.image = image;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
