package com.myversion.myversion.domain;

import jakarta.persistence.PrePersist;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
public class Song {
    @jakarta.persistence.Id
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 기본 ID는 자동 생성
    private String title;   // 노래 제목
    private String artist;  // 가수
    private String subtitle;
    private String coverImg;
    private LocalDateTime createdAt; // 생성 시각 저장


    public String getTitle() {
        return title;
    }

    public void setTitle(String tiele) {
        this.title = tiele;
    }


    public String getArtist() {
        return artist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }


    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();   // 엔티티가 처음 저장될 때 생성 시각 저장
    }



}
