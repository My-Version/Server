package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Song;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaSongRepository implements SongRepository{

    private final EntityManager em;

    public JpaSongRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Song save(Song song) {
        em.persist(song);
        return null;
    }

    @Override
    public Optional<Song> findById(long id) {
        Song song = em.find(Song.class, id);
        return Optional.ofNullable(song);
    }



    @Override
    public Optional<Song> findByName(String name) {
        List<Song> result = em.createQuery("select s from Song s where s.name = :name", Song.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Song> findAll() {
        return em.createQuery("select s from Song s", Song.class).getResultList();
    }

}
