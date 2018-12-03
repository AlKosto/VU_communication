package com.example.imran.vucommunication;

public class Notes {

    String ac_phone_number,currentDate,currentTime
            ,dept_name,ftype,id
            ,nid,paid_or_free,payment_method
            ,price,prog_name,sub_description
            ,sub_name,topic,file;


    public Notes() {
    }

    public Notes(String ac_phone_number, String currentDate, String currentTime, String dept_name, String ftype, String id, String nid, String paid_or_free, String payment_method, String price, String prog_name, String sub_description, String sub_name, String topic, String file) {
        this.ac_phone_number = ac_phone_number;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.dept_name = dept_name;
        this.ftype = ftype;
        this.id = id;
        this.nid = nid;
        this.paid_or_free = paid_or_free;
        this.payment_method = payment_method;
        this.price = price;
        this.prog_name = prog_name;
        this.sub_description = sub_description;
        this.sub_name = sub_name;
        this.topic = topic;
        this.file = file;
    }


    public String getAc_phone_number() {
        return ac_phone_number;
    }

    public void setAc_phone_number(String ac_phone_number) {
        this.ac_phone_number = ac_phone_number;
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

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPaid_or_free() {
        return paid_or_free;
    }

    public void setPaid_or_free(String paid_or_free) {
        this.paid_or_free = paid_or_free;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProg_name() {
        return prog_name;
    }

    public void setProg_name(String prog_name) {
        this.prog_name = prog_name;
    }

    public String getSub_description() {
        return sub_description;
    }

    public void setSub_description(String sub_description) {
        this.sub_description = sub_description;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
