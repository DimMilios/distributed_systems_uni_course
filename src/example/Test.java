package example;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String regex = "^(?:[^,]+,)([^,]+)";

        String inp = "EditPatient.js, Test.js, Test.js, Test.js";

        System.out.println(Pattern.matches(regex, inp));
    }
}
