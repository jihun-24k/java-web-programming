public class StringCalculator {

    public int splitStr(String s) {
        if (s.length() == 0)
            return 0;

        String[] nums = s.split(",|:");
        int result = 0;

        for (String num : nums) {
            result += Integer.parseInt(num);
        }
        return result;
    }
}
