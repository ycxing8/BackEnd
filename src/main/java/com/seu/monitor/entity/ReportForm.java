package com.seu.monitor.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ReportForm {
    @Id
    @GeneratedValue
    private Integer id;
    private String machineIdentifier;
    private String date;
    private String content;
    private String data;
    private String unit;

    public String getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getUnit() {
        return unit;
    }

    public String getData() {
        return data;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
