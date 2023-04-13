public class StringCalculator {

    public int splitStr(String s) {
        String[] nums = {};
        if (isValidStr(s))
            return 0;

        nums = s.split("\\n");
        if (nums[0].contains("//")){
            String delimiter = nums[0].substring(2);
            nums = nums[1].split(delimiter);
        }
        else {
            nums = s.split(",|:");
        }

        int result = 0;

        for (String num : nums) {
            int n = Integer.parseInt(num);
            if (n < 0)
                throw new RuntimeException();
            result += n;
        }
        return result;
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
