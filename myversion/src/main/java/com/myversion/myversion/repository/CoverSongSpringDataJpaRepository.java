package com.myversion.myversion.repository;

import com.myversion.myversion.domain.CoverSong;
import com.myversion.myversion.domain.Song;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoverSongSpringDataJpaRepository extends JpaRepository<CoverSong, Long> {
    List<CoverSong> findByUserId(String userId);    // userId로 CoverSong 찾아내기
}
