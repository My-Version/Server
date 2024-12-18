package com.myversion.myversion.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myversion.myversion.domain.Compare;
import com.myversion.myversion.repository.CompareSpringDataJpaRepository;
import com.myversion.myversion.util.Path;

@Service
public class CompareService {

    private CompareSpringDataJpaRepository compareSpringDataJpaRepository;

    @Autowired
    public CompareService(CompareSpringDataJpaRepository compareSpringDataJpaRepository) {
        this.compareSpringDataJpaRepository = compareSpringDataJpaRepository;
    }


    public List<Compare> getCompareListByUserId(String userId) {
        return compareSpringDataJpaRepository.findAllByUserid(userId);
    }

    public Long addCompare(String userId, String recordLocation, String coverSongLocation){
        Compare compare = new Compare();
        compare.setUserId(userId);
        compare.setRecordLocation(recordLocation);
        compare.setCoverLocation(coverSongLocation);
        return compareSpringDataJpaRepository.save(compare).getId();
    }

    public void updateResult(Long compareId, String createTime, String similarity, String worst_time, String best_time, String time_length, String imgLocation) {
        Compare compare = compareSpringDataJpaRepository.findById(compareId).get();
        compare.setCreateTime(createTime);
        compare.setSimilarity(similarity);
        compare.setWorst_time(worst_time);
        compare.setBest_time(best_time);
        compare.setTime_length(time_length);
        compare.setImgLocation(imgLocation);
        compareSpringDataJpaRepository.save(compare);
    }

    public static CompletableFuture<List<String>> compareSongAsync(String userPracticeDir, String coverDir) {
        return CompletableFuture.supplyAsync(() -> compareSong(userPracticeDir, coverDir));
    }

    // 생성된 본인의 커버파일과 본인이 부른 연습본에 대한 유사도 분석(python파일 실행). 결과 파일 위치 반환
    // (result[2]: jaon파일 위치, result[3] png 파일 위치)
    private static List<String> compareSong(String User_Practice_Dir, String Cover_Dir) {
        List<String> result = new ArrayList<String>();
        try {
            // Python 3.x 스크립트 호출
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    Path.compareFilePath_EC2,  //Python 스크립트 경로
                    User_Practice_Dir,  // 첫번째 인자
                    Cover_Dir  // 두 번째 인자
            );
            // 프로세스 실행
            Process process = processBuilder.start();

            // Python 스크립트 출력값을 읽기 위한 BufferReader 사용
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 1초마다 프로세스 상태 확인
            while (process.isAlive()) {
                System.out.println("Python script is still running...");
                Thread.sleep(10000);  // 10초 대기 후 다시 확인
            }

            // 정상적으로 종료되었을 때 출력 확인
            String line;
            while ((line = stdInput.readLine()) != null) {
                System.out.println(line);
                result.add(line);  // 결과 리스트에 추가
            }

            while ((line = stdError.readLine()) != null) {
                System.out.println("Error: " + line); // 에러 로그 출력
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Python script error: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 반환할 결과 리스트 (json 파일 위치, png 파일 위치)
        if (result.isEmpty()) {
            result = Arrays.asList("no_file_path", "no_file_path");
        }
        return result;
    }
}
