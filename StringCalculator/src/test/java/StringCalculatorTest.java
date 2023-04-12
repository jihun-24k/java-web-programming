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
        assertSplitStr(6, "1,2,3");
    }

    @DisplayName("콜론으로 문자열을 나눔")
    @Test
    void splitByColon() {
        assertSplitStr(3, "1:2");
    }

    @DisplayName("쉼표 또는 콜론으로 문자열을 나눔")
    @Test
    void splitByComma_Or_Colon() {
        assertSplitStr(6, "1:2,3");
    }

    @DisplayName("빈 문자열이면 0을 반환")
    @Test
    void whenEmptyStr_Then_Return_0 () {
        assertSplitStr(0, "");
    }

    @DisplayName("커스텀 구분자로 문자열을 나눔")
    @Test
    void splitByCustomDelimiter() {
        assertSplitStr(6, "//;\n1;2;3");
    }

    void assertSplitStr(int expectedNum, String s){
        int realNum = sc.splitStr(s);
        assertEquals(expectedNum, realNum);
    }
}