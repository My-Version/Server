package com.myversion.myversion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.myversion.myversion.domain.CoverSong;
import com.myversion.myversion.repository.CoverSongSpringDataJpaRepository;

@Service
public class CoverSongService {

    private final CoverSongSpringDataJpaRepository coverSongSpringDataJpaRepository;

    @Autowired
    public CoverSongService(CoverSongSpringDataJpaRepository coverSongSpringDataJpaRepository){
        this.coverSongSpringDataJpaRepository = coverSongSpringDataJpaRepository;
    }



    public List<CoverSong> findAllByUserId(String userId){
        return coverSongSpringDataJpaRepository.findAllByUserid(userId);
    }

    public Optional<String> findS3FileLocationById(Long id) {
        Optional<CoverSong> coverSong = coverSongSpringDataJpaRepository.findById(id);
        if (coverSong.isEmpty()) {
            System.out.println("CoverSong not found for ID: " + id);
        }
        return coverSong.map(CoverSong::getS3FileLocation);
    }

    public Optional<String> findArtistById(Long id){
        return coverSongSpringDataJpaRepository.findById(id).map(CoverSong::getArtist);

    }

    public CoverSong save(CoverSong coverSong){
        return coverSongSpringDataJpaRepository.save(coverSong);
    }

    public CoverSong updateCoverSong(Long id, CoverSong coverSong){
        CoverSong updatedCoverSong;
        updatedCoverSong = coverSongSpringDataJpaRepository.findById(id).get();
        if (updatedCoverSong == null) {
            throw new IllegalStateException("찾을 수 없는 커버곡입니다.");
        }
        if (coverSong.getS3FileLocation() == null) {
            throw new IllegalArgumentException("파일 위치가 없습니다.");
        }
        updatedCoverSong.setS3FileLocation(coverSong.getS3FileLocation());
        if (coverSong.getCreatedDate() == null) {
            throw new IllegalArgumentException("파일 생성 시각이 없습니다.");
        }
        updatedCoverSong.setCreatedDate(coverSong.getCreatedDate());
        return coverSongSpringDataJpaRepository.save(updatedCoverSong);
    }
}
