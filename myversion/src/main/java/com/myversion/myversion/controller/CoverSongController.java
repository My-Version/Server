package com.myversion.myversion.controller;

import com.myversion.myversion.domain.CoverSong;
import com.myversion.myversion.service.CoverSongService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coversong")
public class CoverSongController {
    private final CoverSongService coverSongService;
    public CoverSongController(CoverSongService coverSongService) {
        this.coverSongService = coverSongService;
    }

    // 커버곡 생성
    // userId, artist, songTitle, s3FileLocation, createdDate
    @PostMapping("/save")
    public CoverSong saveCoverSong(@RequestBody CoverSong coverSong) {
        return coverSongService.save(coverSong);
    }
    // findAllByUserId
    @GetMapping("/{userId}")
    public List<CoverSong> findAllByUserId(@PathVariable String userId) {
        return coverSongService.findAllByUserId(userId);
    }

    @PatchMapping("/{id}")
    public CoverSong updateCoverSong(@PathVariable Long id, @RequestBody CoverSong updatedFields) {
        return coverSongService.updateCoverSong(id, updatedFields);
    }



}
