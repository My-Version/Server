package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Song;

import java.util.List;
import java.util.Optional;

public interface SongRepository {
    //Song save(Song song);
    Optional<Song> findById(long id);
    List<Song> findAll();
}
