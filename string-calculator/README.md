# 요구사항
전달하는 문자를 구분자로 분리한 후 각 숫자의 합을 구해 반환

- 쉼표(,) 또는 콜론(:)을 구분자로 가지는 문자열을 전달하는 경우 구분자를 기준으로 분리한 각 숫자의 합을 반환한다.
  - 예) " " => 0, "1,2" => 3, "1,2:3" => 6
- 앞의 기본 구분자 외에 커스텀 구분자를 지정할 수 있다. 커스텀 문자는 문자열 앞부분의 "//"와 "\n" 사이에 위치하는 문자를 커스텀 구분자로 사용한다. 예를 들어 "//;\n1;2;3"과 같이 값을 입력할 경우 커스텀 구분자는 세미콜론(;)이며, 결과 값은 6이 반환되어야 한다.
- 문자열 계산기에 음수를 전달하는 경우 RuntimeException으로 예외 처리해야한다.

## 요구사항 분석

- [x] 구분자가 쉼표로만 이루어진 문자열 처리
- [x] 구분자가 콜론으로만 이루어진 문자열 처리
- [x] 쉼표 또는 콜론으로만 이루어진 문자열 처리
- [x] 음수를 전달하면 예외처리
- [x] 빈문자열이면 0을 반환
- [x] 커스텀 구분자 문자열 처리