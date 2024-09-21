package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaSongRepository extends JpaRepository<Song, Long>, SongRepository {
    @Override
    Optional<Song> findByName(String name);
}
