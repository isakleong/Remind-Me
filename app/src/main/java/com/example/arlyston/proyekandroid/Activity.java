package com.example.arlyston.proyekandroid;

import java.util.Date;

/**
 * Created by Arlyston on 03/12/2018.
 */


public class Activity {
    int id;
    String name, priority,idUser;
    Date convdate;
    boolean reminded, stopped;

    public boolean isReminded() {
        return reminded;
    }

    public void setReminded(boolean reminded) {
        this.reminded = reminded;
    }

    public Activity(){}

    public Activity(int no, String nama, Date convtanggal, String prioritas,String idsanguser){
        id = no;
        name = nama;
        convdate = convtanggal;
        priority = prioritas;
        reminded = false;
        stopped = false;
        idUser = idsanguser;

    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Date getConvdate() {
        return convdate;
    }

    public void setConvdate(Date convdate) {
        this.convdate = convdate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
