package com.myversion.myversion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Collections;

@Entity
public class CoverSong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String userId;
    @Column (nullable = false)
    private String artist;
    @Column (nullable = false)
    private String songTitle;

    private String s3FileLocation;
    private String createdDate;

    public CoverSong(String userId, String artist, String songTitle, String s3FileLocation, String createdDate) {
        this.userId = userId;
        this.artist = artist;
        this.songTitle = songTitle;
        this.s3FileLocation = s3FileLocation;
        this.createdDate = createdDate;
    }

    protected CoverSong() {

    }

    public String getCreatedDate() {
        return createdDate;
    }
    
    public Long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getUserId() {
        return userId;
    }

    public String getSongTitle() {
        return songTitle;
    }


    public void setS3FileLocation(String s3FileLocation) {
        this.s3FileLocation = s3FileLocation;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getS3FileLocation() {
        return s3FileLocation;
    }

}
