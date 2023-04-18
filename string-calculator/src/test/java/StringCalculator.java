import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int addString(String s) {
        String[] nums = {};
        if (isValidStr(s))
            return 0;

        nums = split(s);

        return addNumbers(nums);
    }

    private String[] split(String s) {
        Matcher matcher = Pattern.compile("//(.)\\n(.*)").matcher(s);
        if (matcher.find()) {
            String delimiter = matcher.group(1);
            return matcher.group(2).split(delimiter);
        }

        return s.split(",|:");
    }

    private void checkNegativeNum(int num) {
        if (num < 0)
            throw new RuntimeException();
    }

    private int addNumbers(String[] nums) {
        int sum = 0;

        for (String num : nums) {
            int n = Integer.parseInt(num);
            checkNegativeNum(n);
            sum += n;
        }

        return sum;
    }

    private boolean isValidStr(String s) {
        return checkNull(s) || checkEmpty(s);
    }

    private boolean checkNull(String s) {
        return s == null;
    }

    private boolean checkEmpty(String s) {
        return s.length() == 0;
    }
}
