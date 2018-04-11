package com.seu.monitor.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Integer id;//主键

    private String name;//用户名，主唯一

    private String password;

    private String power;
	
	private String machineIdentifiers;

    public User(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

	public void setMachineIdentifiers(String machineIdentifiers) {
		this.machineIdentifiers = machineIdentifiers;
	}

	public String getMachineIdentifiers() {
		return machineIdentifiers;
	}
	
}
