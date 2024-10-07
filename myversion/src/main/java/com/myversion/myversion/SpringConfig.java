// package com.myversion.myversion;

// import com.myversion.myversion.repository.SongRepository;
// import com.myversion.myversion.service.SongService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import javax.sql.DataSource;

// @Configuration
// public class SpringConfig{
//     private final SongRepository songRepository;

//     @Autowired
//     public SpringConfig(SongRepository songRepository) {
//         this.songRepository = songRepository;
//     }

//     @Bean
//     public SongService songService(){
//         return new SongService(songRepository);
//     }
// }