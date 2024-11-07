package com.myversion.myversion.service;

import com.myversion.myversion.domain.Song;

import com.myversion.myversion.repository.SongSpringDataJpaRepository;

import org.springframework.transaction.annotation.Transactional;



@Transactional
public class SongService {

    private final SongSpringDataJpaRepository songRepository;

    public SongService(SongSpringDataJpaRepository songRepository){
        this.songRepository = songRepository;
    }

    //노래 등록
    public Long join(Song song){
        validateDuplicateSong(song);    // 중복곡은 배제
        //songRepository.save(song);
        return song.getId();
    }

    private void validateDuplicateSong(Song song) {
        // title이랑 artist가 둘다 겹치는 경우는 추가 안함.
        songRepository.findByArtistAndTitle(song.getArtist(), song.getTitle()).ifPresent(
                m->{
                    throw new IllegalStateException("이미 존재하는 노래입니다. : 가수, 곡 제목 중복");
                }
        );
    }

}