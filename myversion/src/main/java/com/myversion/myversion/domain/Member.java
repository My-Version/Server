package com.myversion.myversion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id
    @Column(unique = true, nullable = false)
    private String id;   // 사용자 입력 id

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    protected Member() {
    }

    public Member(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
    public String getPassWord(){
        return this.password;
    }

}