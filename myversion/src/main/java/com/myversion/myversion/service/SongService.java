package com.myversion.myversion.service;

import com.myversion.myversion.domain.Song;
import com.myversion.myversion.repository.SongRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class SongService{

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository){
        this.songRepository = songRepository;
    }

    //노래 등록
    public Long join(Song song){
        validateDuplicateSong(song);    // 중복곡은 배제
        songRepository.save(song);
        return song.getId();
    }

    private void validateDuplicateSong(Song song) {
        songRepository.findByName(song.getName()).ifPresent(
                m->{
                    throw new IllegalStateException("이미 존재하는 노래");
                }
        );
    }

    public List<Song> findSong(){
        return songRepository.findAll();
    }

}