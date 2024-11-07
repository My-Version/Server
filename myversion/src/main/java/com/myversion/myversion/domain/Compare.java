package com.myversion.myversion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Compare {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String jsonLocation;
    private String imgLocation;

    public Compare() {}

    public void setJsonLocation(String jsonLocation) {
        this.jsonLocation = jsonLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
    }

    public Long getId() {
        return id;
    }
}
