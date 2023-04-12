import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringCalculatorTest {

    StringCalculator sc;

    @BeforeEach
    void init() {
        sc = new StringCalculator();
    }

    @DisplayName("쉼표로 문자열을 나눔")
    @Test
    void splitByComma() {
        int res = sc.splitStr("1,2,3");
        assertEquals(6, res);
    }

    @DisplayName("콜론으로 문자열을 나눔")
    @Test
    void splitByColon() {
        int res = sc.splitStr("1:2");
        assertEquals(3, res);
    }

    @DisplayName("쉼표 또는 콜론으로 문자열을 나눔")
    @Test
    void splitByComma_Or_Colon() {
        int res = sc.splitStr("1:2,3");
        assertEquals(6, res);
    }
}