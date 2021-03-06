package com.seu.monitor.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "machine")
public class Machine {

    @Id
    @GeneratedValue
    private Integer id;//改成id（序号）为主键

    private String identifier;//标志符唯一

    private String name;

    private String place;

    private String description;

    private String imagePath;
   
    private String identityCode;//设备身份码，用语识别PLC,唯一

	private String status;//8

    private String lastOnlineTime;

    public Machine(){}

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace() {
        return place;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    //num:16

}
