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
}