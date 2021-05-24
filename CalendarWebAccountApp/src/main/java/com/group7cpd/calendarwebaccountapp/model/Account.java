package com.group7cpd.calendarwebaccountapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Account {
    @Id
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String events;

    public Account(String nm, String pass, String evnt){
        name = nm;
        password = pass;
        events = evnt;
    }

    public Account() {

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEvents() { return events; }

    public int getId() { return id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEvents(String events) { this.events = events; }

    public void setId(int id) { this.id = id; }

}
