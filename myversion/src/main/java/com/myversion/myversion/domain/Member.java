package com.myversion.myversion.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String name;

    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
    public String getPassWord(){
        return this.password;
    }

}
