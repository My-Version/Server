package com.myversion.myversion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Compare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String userId;
    private String recordLocation;
    private String coverLocation;

    private String createTime;
    private String similarity;
    private String worst_time;
    private String best_time;
    private String time_length;
    private String imgLocation;

    public Compare() {
    }

    public String getUserId() {
        return userId;
    }

    public String getCoverLocation() {
        return coverLocation;
    }

    public void setCoverLocation(String coverLocation) {
        this.coverLocation = coverLocation;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getWorst_time() {
        return worst_time;
    }

    public void setWorst_time(String worst_time) {
        this.worst_time = worst_time;
    }

    public String getBest_time() {
        return best_time;
    }

    public void setBest_time(String best_time) {
        this.best_time = best_time;
    }

    public String getTime_length() {
        return time_length;
    }

    public void setTime_length(String time_length) {
        this.time_length = time_length;
    }


    public String getRecordLocation() {
        return recordLocation;
    }

    public void setRecordLocation(String recordLocation) {
        this.recordLocation = recordLocation;
    }



    public String getImgLocation() {
        return imgLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
    }

    public Long getId() {
        return id;
    }
}
