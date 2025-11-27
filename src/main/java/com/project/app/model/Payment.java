package com.project.app.model;

/**
 * Payment 엔티티
 *
 * - 결제 내역 정보를 담는 도메인 객체
 *
 * amount 규칙:
 *  - 음수 금액을 사용 (-150000원): 지출이라는 의미
 */
public class Payment {

    private final int id;
    private final String userId;   // 결제한 사용자
    private final int lectureId;   // 관련 강의 ID (강의 결제일 경우), 없으면 0
    private final String itemName; // "고등 수학 I", "수학의 정석" 등
    private final int amount;      // -150000 처럼 음수
    private final String type;     // "LECTURE" or "TEXTBOOK"
    private final String paidAt;   // "2025-11-01" 같은 간단한 날짜 문자열

    public Payment(int id, String userId, int lectureId,
                   String itemName, int amount, String type, String paidAt) {
        this.id = id;
        this.userId = userId;
        this.lectureId = lectureId;
        this.itemName = itemName;
        this.amount = amount;
        this.type = type;
        this.paidAt = paidAt;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public int getLectureId() {
        return lectureId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getPaidAt() {
        return paidAt;
    }
}
