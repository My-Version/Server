package com.myversion.myversion.service;

import com.myversion.myversion.domain.CoverSong;
import com.myversion.myversion.repository.CoverSongSpringDataJpaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoverSongService {

    private final CoverSongSpringDataJpaRepository coverSongSpringDataJpaRepository;

    @Autowired
    public CoverSongService(CoverSongSpringDataJpaRepository coverSongSpringDataJpaRepository){
        this.coverSongSpringDataJpaRepository = coverSongSpringDataJpaRepository;
    }

    public List<CoverSong> findAllByUserId(String userId){
        return coverSongSpringDataJpaRepository.findByUserId(userId);
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
