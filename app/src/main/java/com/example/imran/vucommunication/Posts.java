package com.example.imran.vucommunication;

public class Posts {

    public String date,image,post,time, id;

    public Posts() {
    }



    public Posts(String date, String image, String post, String time) {
        this.date = date;
        this.image = image;
        this.post = post;
        this.time = time;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
