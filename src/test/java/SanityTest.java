package test.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SanityTest {
    @Test
    void shouldFail() {
        assertEquals(1, 2); // 실패 테스트
    }
}
