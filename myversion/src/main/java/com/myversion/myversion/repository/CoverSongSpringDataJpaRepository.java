package com.myversion.myversion.repository;
import com.myversion.myversion.domain.CoverSong;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CoverSongSpringDataJpaRepository extends JpaRepository<CoverSong, Long> {
    @Query("SELECT c FROM CoverSong c WHERE c.userId = :userId")
    List<CoverSong> findAllByUserid(@Param("userId") String userId);
}