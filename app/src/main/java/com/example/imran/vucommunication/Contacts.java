package com.example.imran.vucommunication;

public class Contacts {
    public String name,status,programName,studentBatch,studentid,image;

    public Contacts(String name, String status, String programName, String studentBatch, String studentid, String image) {
        this.name = name;
        this.status = status;
        this.programName = programName;
        this.studentBatch = studentBatch;
        this.studentid = studentid;
        this.image = image;
    }


    public Contacts(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getStudentBatch() {
        return studentBatch;
    }

    public void setStudentBatch(String studentBatch) {
        this.studentBatch = studentBatch;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
