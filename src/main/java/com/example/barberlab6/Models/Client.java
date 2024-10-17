package com.example.barberlab6.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String cliname;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sessionDateTime;
    private String service;
    private String mastername;



    public Client() {
    }

    public Client(String cliname, LocalDateTime sessionDateTime, String service, String mastername) {
        this.cliname = cliname;
        this.sessionDateTime = sessionDateTime;
        this.service = service;
        this.mastername = mastername;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCliname() {
        return cliname;
    }

    public void setCliname(String cliname) {
        this.cliname = this.cliname;
    }


    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMastername() {
        return mastername;
    }

    public void setMastername(String mastername) {
        this.mastername = mastername;
    }
}


