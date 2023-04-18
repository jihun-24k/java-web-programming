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
        assertAddStr(6, "1,2,3");
    }

    @DisplayName("콜론으로 문자열을 나눔")
    @Test
    void splitByColon() {
        assertAddStr(3, "1:2");
    }

    @DisplayName("쉼표 또는 콜론으로 문자열을 나눔")
    @Test
    void splitByComma_Or_Colon() {
        assertAddStr(6, "1:2,3");
    }

    @DisplayName("빈 문자열이면 0을 반환")
    @Test
    void whenEmptyStr_Then_Return_0 () {
        assertAddStr(0, "");
    }

    @DisplayName("null이면 0을 반환")
    @Test
    void whenStr_IsNull_Then_Return_0 (){
        assertAddStr(0, null);
    }

    @DisplayName("커스텀 구분자로 문자열을 나눔")
    @Test
    void splitByCustomDelimiter() {
        assertAddStr(6, "//;\n1;2;3");
    }

    @DisplayName("음수를 전달하면 예외 처리")
    @Test
    void whenNum_IsMinus_ThenThrowsException() {
        assertThrows(RuntimeException.class, () -> sc.addString("-1,1,2"));
    }

    void assertAddStr(int expectedNum, String s){
        int realNum = sc.addString(s);
        assertEquals(expectedNum, realNum);
    }
}