package com.myversion.myversion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myversion.myversion.domain.Song;
@Repository
public interface SpringDataJpaRepository extends Repository {
    @Query("SELECT s FROM Song s WHERE s.artist = :artist AND s.title = :title")
    Optional<Song> findByArtistAndTitle(@Param("artist") String artist, @Param("title") String title);
}
