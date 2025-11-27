package com.project.app.repository;

import com.project.app.model.Lecture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LectureRepositoryImpl {
    private static ArrayList<Lecture> lectureList=new ArrayList<>();

    public void loadLecturesFromFile(String filename) {
        lectureList.clear();

        // 파일 존재 여부 확인
        File file = new File(filename);
        System.out.println("[Repository] 파일 경로: " + file.getAbsolutePath());
        System.out.println("[Repository] 파일 존재: " + file.exists());
        System.out.println("[Repository] 파일 크기: " + file.length() + " bytes");

        Scanner filein = openFile(filename);

        boolean isFirstLine = true;
        int lineCount = 0;

        while (filein.hasNextLine()) {
            String line = filein.nextLine();
            lineCount++;

            if (isFirstLine) {
                System.out.println("[Repository] 헤더 줄: " + line);
                isFirstLine = false;
                continue;
            }
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("/");
            System.out.println("[Repository] 줄 " + lineCount + " - 필드 개수: " + parts.length);

            // 필드 개수 체크 (17개 또는 18개 허용)
            if (parts.length < 17) {
                System.out.println("[Repository] 줄 " + lineCount + " 스킵 - 필드 부족 (필드: " + parts.length + "개)");
                continue;
            }

            try {
                // 이미지 경로 처리 (18번째 필드가 있을 때만)
                String thumbnailPath = "";
                if (parts.length >= 18 && !parts[17].trim().isEmpty()) {
                    String thumbnailFileName = parts[17].trim();
                    thumbnailPath = "src/main/resources/LectureImages/" + thumbnailFileName;
                }

                Lecture lecture = new Lecture(
                        parts[0].trim(),   // lectureId
                        parts[1].trim(),   // academy
                        parts[2].trim(),   // subject
                        Integer.parseInt(parts[3].trim()),   // year
                        parts[4].trim(),   // title
                        parts[5].trim(),   // instructor
                        parts[6].trim(),   // textbook
                        Integer.parseInt(parts[7].trim()),   // lecturePrice
                        Integer.parseInt(parts[8].trim()),   // textbookPrice
                        parts[9].trim(),   // location
                        parts[10].trim(),  // description
                        parts[11].trim(),  // targetGrade
                        Double.parseDouble(parts[12].trim()),  // rating
                        Integer.parseInt(parts[13].trim()),    // currentEnrolled
                        Integer.parseInt(parts[14].trim()),    // capacity
                        parts[15].trim(),  // dayOfWeek
                        parts[16].trim(),  // time
                        thumbnailPath      // 빈 문자열 또는 이미지 경로
                );
                lectureList.add(lecture);
                System.out.println("[Repository] 줄 " + lineCount + " 로드 완료: " + parts[0]);
            } catch (NumberFormatException e) {
                System.err.println("[Repository] 줄 " + lineCount + " 파싱 오류: " + e.getMessage());
                System.err.println("[Repository] 문제 줄: " + line);
            }
        }

        System.out.println("[Repository] 총 로드된 강의 수: " + lectureList.size());
        filein.close();
    }

    public static Scanner openFile(String filename) {
        Scanner filein = null;
        try {
            filein = new Scanner(new File(filename));
        } catch (Exception e) {
            System.out.printf("파일 오픈 실패: %s\n", filename);
            throw new RuntimeException(e);
        }
        return filein;
    }

    public void save(Lecture lecture){
        lectureList.add(lecture);
    }

    // id로 강의 찾기
    public Lecture findById(String id){
        for(Lecture lec:lectureList){
            if(id.equals(lec.getLectureId())){
                return lec;
            }
        }
        return null;
    }
    public ArrayList<Lecture> findAll(){
        return lectureList;
    }

}
