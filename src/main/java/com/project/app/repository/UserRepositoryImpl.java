package com.project.app.repository;

import com.project.app.model.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserRepository 구현체 (임시 인메모리 버전)
 *
 * - 현재는 애플리케이션 메모리에만 사용자 정보를 저장
 * - 추후 파일(users.txt) 또는 DB 연동으로 교체해야 함.
 *
 * TODO:
 * - SignUpService 리팩토링 시, users.txt 대신 이 Repository를 사용하도록 변경
 * - 파일/DB 연동 구현 후, 인메모리 store 제거 또는 테스트용으로만 사용
 */
public class UserRepositoryImpl implements UserRepository {

    /**
     * 인메모리 저장소
     * key: userId, value: User
     */
    private final Map<String, User> store = new ConcurrentHashMap<>();
    private static final String USER_FILE_PATH = "src/main/data/userData.txt";

    public UserRepositoryImpl() {
        System.out.println("UserRepository 초기화 시작...");
        loadUsersFromFile();
        System.out.println("총 " + store.size() + "명의 사용자가 로드되었습니다.");

    }

    private void loadUsersFromFile() {
        String[] possiblePaths = {
            "src/main/data/userData.txt",
            "data/userData.txt",
            "../iltagangsa/src/main/data/userData.txt"
        };
        
        for (String path : possiblePaths) {
            try {
                // UTF-8 인코딩으로 파일 읽기
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), "UTF-8")
                );
                System.out.println("파일 발견: " + path);
                processFile(br);
                br.close();
                return;
            } catch (IOException e) {
                // 다음 경로 시도
            }
        }
        
        System.out.println("userData.txt 파일을 찾을 수 없습니다.");
    }
    /**
     * users.txt 파일에서 사용자 데이터 로드
     */
    private void processFile(BufferedReader br) throws IOException {
            String line;
            int count = 0;

            // 첫 줄은 헤더이므로 건너뛰기
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("/");
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String password = parts[1].trim();
                    String name = parts[2].trim();
                    String birth = parts[3].trim();
                    String gradeStr = parts[4].trim();

                    // 학년 변환: "고1" -> 1, "고2" -> 2, "고3" -> 3, "N수" -> 4
                    int grade = switch (gradeStr) {
                        case "고1" -> 1;
                        case "고2" -> 2;
                        case "고3" -> 3;
                        case "N수" -> 4;
                        default -> 1;
                    };

                    User user = new User(id, password, name, grade, birth);
                    store.put(id, user);
                    count++;
                }
            }

            System.out.println(" users.txt에서 " + count + "명의 사용자 로드 완료");
            System.out.println(" 테스트 계정 예시: ID=user001, PW=pw001");

        } 

    @Override
    public User findById(String id) {
        if (id == null) {
            return null;
        }
        return store.get(id);
    }

    @Override
    public void save(User user) {
        if (user == null || user.getId() == null) {
            return;
        }
        store.put(user.getId(), user);
    }
}
