package com.myversion.myversion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id
    @Column(unique = true, nullable = false)
    private String id;   // 사용자 입력 id

    @Column(nullable = false)
    private String password;

    protected Member() {
    }

    public Member(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId(){
        return this.id;
    }

    public String getPassWord(){
        return this.password;
    }
}