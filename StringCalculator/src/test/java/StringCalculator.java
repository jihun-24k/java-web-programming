public class StringCalculator {

    public int addString(String s) {
        String[] nums = {};
        if (isValidStr(s))
            return 0;

        nums = split(s);

        return addNumbers(nums);
    }

    private String[] split(String s) {
        String[] nums = s.split("\\n");
        if (nums[0].contains("//")){
            String delimiter = nums[0].substring(2);
            return nums[1].split(delimiter);
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
