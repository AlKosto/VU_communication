package com.example.imran.vucommunication;

public class Events {
    private String currentDate, currentTime,description,id,image,title, eid;
    private long day,hour,minute,year, month;

    public Events() {
    }

    public Events(String currentDate, String currentTime, String description, String id, String image, String title, String eid, long day, long hour, long minute, long year, long month) {
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.description = description;
        this.id = id;
        this.image = image;
        this.title = title;
        this.eid = eid;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.year = year;
        this.month = month;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMinute() {
        return minute;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }
}
