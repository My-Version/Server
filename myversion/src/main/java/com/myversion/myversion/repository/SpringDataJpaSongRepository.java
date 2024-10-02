package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SpringDataJpaSongRepository extends JpaRepository<Song, Long>, Repository {
    // artist와 title이 둘 다 일치하는 노래를 찾는 커스텀 메서드
    @Query("SELECT s FROM Song s WHERE s.artist = :artist AND s.title = :title")
    Optional<Song> findByArtistAndTitle(@Param("artist") String artist, @Param("title") String title);
}
