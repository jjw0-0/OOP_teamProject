package com.project.app.controller;

import com.project.app.dto.InstructorCardView;
import com.project.app.dto.InstructorDetailResponse;
import com.project.app.dto.InstructorListResponse;
import com.project.app.model.Instructor;
import com.project.app.repository.InstructorRepository;
import com.project.app.repository.InstructorRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Instructor Controller
 *
 * 역할:
 * - Repository에서 데이터 조회
 * - Model을 DTO로 변환
 * - View에 데이터 제공
 */
public class InstructorController {

    private static InstructorController instance;
    private final InstructorRepository instructorRepository;

    private InstructorController() {
        this.instructorRepository = new InstructorRepositoryImpl();
    }

    public static InstructorController getInstance() {
        if (instance == null) {
            instance = new InstructorController();
        }
        return instance;
    }

    /**
     * 전체 강사 목록 조회 (카드뷰용)
     */
    public InstructorListResponse getAllInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        List<InstructorCardView> cardViews = instructors.stream()
                .map(this::convertToCardView)
                .collect(Collectors.toList());

        return new InstructorListResponse(cardViews, cardViews.size());
    }

    /**
     * 과목별 강사 목록 조회
     */
    public InstructorListResponse getInstructorsBySubject(String subject) {
        List<Instructor> instructors = instructorRepository.findAll();
        List<InstructorCardView> cardViews = instructors.stream()
                .filter(i -> i.getSubject().equals(subject))
                .map(this::convertToCardView)
                .collect(Collectors.toList());

        return new InstructorListResponse(cardViews, cardViews.size());
    }

    /**
     * 강사명으로 검색
     */
    public InstructorListResponse searchInstructors(String keyword) {
        List<Instructor> instructors = instructorRepository.findByName(keyword);
        List<InstructorCardView> cardViews = instructors.stream()
                .map(this::convertToCardView)
                .collect(Collectors.toList());

        return new InstructorListResponse(cardViews, cardViews.size());
    }

    /**
     * 강사 상세 정보 조회
     */
    public InstructorDetailResponse getInstructorDetail(String instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId);
        if (instructor == null) {
            return null;
        }

        return convertToDetailResponse(instructor);
    }

    /**
     * Instructor -> InstructorCardView 변환
     */
    private InstructorCardView convertToCardView(Instructor instructor) {
        return new InstructorCardView(
                instructor.getId(),
                instructor.getName(),
                instructor.getIntroduction(),
                instructor.getProfileImagePath(),
                4.5, // TODO: Review 데이터 연동 필요 (임시 기본값)
                instructor.getSubject()
        );
    }

    /**
     * Instructor -> InstructorDetailResponse 변환
     */
    private InstructorDetailResponse convertToDetailResponse(Instructor instructor) {
        // Lecture 정보 변환 (TODO: LectureRepository 연동 필요)
        List<InstructorDetailResponse.LectureSummary> lectures = instructor.getLectureIds().stream()
                .map(lectureId -> new InstructorDetailResponse.LectureSummary(
                        lectureId,
                        "강의명 " + lectureId, // TODO: 실제 강의명 조회 필요
                        4.5, // TODO: 실제 평점 조회 필요
                        100000 // TODO: 실제 가격 조회 필요
                ))
                .collect(Collectors.toList());

        // Textbook 정보 변환 (TODO: TextbookRepository 연동 필요)
        InstructorDetailResponse.TextbookSummary textbook = null;
        if (instructor.getTextbookId() != null) {
            textbook = new InstructorDetailResponse.TextbookSummary(
                    instructor.getTextbookId(),
                    "교재명 " + instructor.getTextbookId(), // TODO: 실제 교재명 조회 필요
                    50000 // TODO: 실제 가격 조회 필요
            );
        }

        return new InstructorDetailResponse(
                instructor.getId(),
                instructor.getName(),
                instructor.getIntroduction(),
                instructor.getProfileImagePath(),
                instructor.getAcademyId(),
                "학원명 " + instructor.getAcademyId(), // TODO: Academy 이름 조회 필요
                instructor.getSubject(),
                4.5, // TODO: Review 평점 조회 필요
                0, // TODO: Review 개수 조회 필요
                lectures,
                textbook,
                instructor.getStudentIds().size()
        );
    }
}