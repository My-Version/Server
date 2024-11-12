package com.myversion.myversion.service;

import com.myversion.myversion.domain.Compare;
import com.myversion.myversion.repository.CompareSpringDataJpaRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompareService {

    private CompareSpringDataJpaRepository compareSpringDataJpaRepository;

    @Autowired
    public CompareService(CompareSpringDataJpaRepository compareSpringDataJpaRepository) {
        this.compareSpringDataJpaRepository = compareSpringDataJpaRepository;
    }

    public Compare updateLocations(Long id, String jsonLocation, String imgLocation) {
        Optional<Compare> optionalCompare = compareSpringDataJpaRepository.findById(id);

        if (optionalCompare.isPresent()) {
            Compare compare = optionalCompare.get();
            compare.setJsonLocation(jsonLocation);
            compare.setImgLocation(imgLocation);
            return compareSpringDataJpaRepository.save(compare); // 업데이트 후 저장
        } else {
            throw new IllegalStateException("Compare 엔티티를 찾을 수 없습니다: ID = " + id);
        }
    }

    public Long addCompare(){
        Compare compare = new Compare();
        return compareSpringDataJpaRepository.save(compare).getId();
    }

    public static CompletableFuture<List<String>> compareSongAsync(String userPracticeDir, String coverDir) {
        return CompletableFuture.supplyAsync(() -> compareSong(userPracticeDir, coverDir));
    }

    // 생성된 본인의 커버파일과 본인이 부른 연습본에 대한 유사도 분석(python파일 실행). 결과 파일 위치 반환
    // ([2]: jaon파일 위치, [3] png 파일 위치)
    private static List<String> compareSong(String User_Practice_Dir, String Cover_Dir) {
        List<String> result = new ArrayList<String>();
        try {
            // Python 3.x 스크립트 호출
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    "/home/ec2-user/tone_compare.py",  //Python 스크립트 경로
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
