package com.myversion.myversion.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.python.util.PythonInterpreter;

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

    private static PythonInterpreter intPre;
    // 생성된 본인의 커버파일과 본인이 부른 연습본에 대한 유사도 분석(python파일 실행). 결과 파일 위치 반환
    // ([2]: jaon파일 위치, [3] png 파일 위치)
    public List<String> CompareSong(String User_Practice_Dir, String Cover_Practice_Dir){
        List<String> result = new ArrayList<String>();
        try{
            // Python 3.x 스크립트 호출
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    "/home/ec2-user/tone_compare.py",  //Python 스크립트경로
                    User_Practice_Dir,  //첫번째 인자
                    Cover_Practice_Dir  // 두번째 인자
            );
            // 프로세스 실행
            Process process = processBuilder.start();
            // Python 스크립트 출력값을 읽기 위한 BufferReader 사용
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 에러 읽기
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));


            String line;
            while ((line = stdInput.readLine()) != null) {
                System.out.println(line);   // 출력 확인
                result.add(line);           // 결과 리스트에 추가
            }

            while ((line = stdError.readLine()) != null) {
                System.out.println("Error: "+line); // 에러로그 출력
            }


            //프로세스 종료 코드 확인 (0이면 정상종료)
            int exitCode = process.waitFor();
            if(exitCode != 0){
                System.out.println("Python script error : "+exitCode);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //반환할 결과 리스트 (json파일위치, png 파일위치)
        if(result.isEmpty()){
            result = Arrays.asList("no_file_path", "no_file_path");
        }
        return result;
        // ([2]: jaon파일 위치, [3] png 파일 위치)
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