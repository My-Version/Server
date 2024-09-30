package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Song;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public class JpaSongRepository implements SongRepository{

    private final EntityManager em;

    public JpaSongRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Song> findById(long id) {
        Song song = em.find(Song.class, id);
        return Optional.ofNullable(song);
    }

    @Override
    public List<Song> findAll() {
        return em.createQuery("select s from Song s", Song.class).getResultList();
    }

}
