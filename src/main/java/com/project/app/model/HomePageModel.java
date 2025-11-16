package main.java.com.project.app.model;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

// ======== Model ========

/**
 * 이미지 경로 목록과 현재 인덱스를 관리하는 Model
 */
public class HomePageModel {

    private final List<String> imagePaths; // 이미지 파일 경로 리스트
    private int currentIndex = 0;          // 현재 인덱스 (0부터 시작)

    public HomePageModel(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public boolean isEmpty() {
        return imagePaths == null || imagePaths.isEmpty();
    }

    public String getCurrentImagePath() {
        return imagePaths.get(currentIndex);
    }

    public void next() {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        if (currentIndex < imagePaths.size() - 1) {
            currentIndex++;
        } else if (currentIndex == imagePaths.size() - 1) {
            currentIndex = 0; // 마지막에서 다시 처음으로
        }
    }

    public void prev() {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        if (currentIndex > 0) {
            currentIndex--;
        } else if (currentIndex == 0) {
            currentIndex = imagePaths.size() - 1; // 처음에서 마지막으로
        }
    }
}