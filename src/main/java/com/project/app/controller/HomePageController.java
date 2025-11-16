package com.project.app.controller;

import com.project.app.model.HomePageModel;
import com.project.app.view.HomePageView.HomeImageView;

import javax.swing.*;

// ======== Controller ========

/**
 * 버튼 이벤트를 처리하고,
 * Model의 인덱스를 바꾼 뒤 View의 이미지를 갱신한다.
 */
public class HomePageController {

    private final HomePageModel model;
    private final HomeImageView view;

    public HomePageController(HomePageModel model, HomeImageView view) {
        this.model = model;
        this.view = view;

        initListeners();
        updateImage(); // 시작할 때 첫 이미지 보여주기
    }

    private void initListeners() {
        view.addPrevButtonListener(e -> {
            model.prev();
            updateImage();
        });

        view.addNextButtonListener(e -> {
            model.next();
            updateImage();
        });
    }

    private void updateImage() {
        if (model.isEmpty()) {
            view.setNoImageText("표시할 이미지가 없습니다.");
            return;
        }

        String path = model.getCurrentImagePath();
        ImageIcon icon = new ImageIcon(path);
        view.setImageIcon(icon);
    }
}