public class StringCalculator {

    public int splitStr(String s) {
        String[] nums = s.split(",|:");
        int result = 0;

        for (String num : nums) {
            result += Integer.parseInt(num);
        }
        return result;
    }
}
