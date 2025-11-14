package user;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SignInService {

    private Map<String, String> users = new HashMap<>();

    public SignInService() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String pw = parts[1].trim();
                    users.put(id, pw);
                }
            }
        } catch (IOException e) {
            System.out.println("users.txt 파일을 읽을 수 없습니다: " + e.getMessage());
        }
    }

    public boolean userExists(String id) {
        return users.containsKey(id);
    }

    public boolean validatePassword(String id, String password) {
        return users.get(id).equals(password);
    }
}