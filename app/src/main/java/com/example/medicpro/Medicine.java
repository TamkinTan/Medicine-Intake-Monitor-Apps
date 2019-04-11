package com.example.medicpro;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Medicine implements Serializable{
    private String name;
    private List<Calendar> alarmTimeList;
    private List<Integer> alarmRequestCodeList;
    private boolean repeat;
    private int quantity;
    private String details;

    public Medicine(String name, List<Calendar> alarmTimeList,List<Integer> alarmRequestCodeList,  boolean repeat, int quantity, String details) {
        this.name = name;
        this.alarmTimeList = alarmTimeList;
        this.alarmRequestCodeList = alarmRequestCodeList;
        this.repeat = repeat;
        this.quantity = quantity;
        this.details = details;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Calendar> getAlarmTimeList() {
        return alarmTimeList;
    }

    public void setAlarmTimeList(List<Calendar> alarmTimeList) {
        this.alarmTimeList = alarmTimeList;
    }

    public List<Integer> getAlarmRequestCodeList() {
        return alarmRequestCodeList;
    }

    public void setAlarmRequestCodeList(List<Integer> alarmRequestCodeList) {
        this.alarmRequestCodeList = alarmRequestCodeList;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setDetails(String details){
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public String toString() {
        return name;
    }
}
