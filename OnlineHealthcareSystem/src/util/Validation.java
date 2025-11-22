package util;

import java.util.regex.Pattern;

public class Validation {

    // Email validation regex pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Phone number must be 10 digits
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    // Name must contain letters only
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z ]+$");

    // Check empty string
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Validate email
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Validate name
    public static boolean isValidName(String name) {
        if (isEmpty(name)) return false;
        return NAME_PATTERN.matcher(name).matches();
    }

    // Validate age (only numbers allowed)
    public static boolean isValidAge(String age) {
        if (isEmpty(age)) return false;
        try {
            int n = Integer.parseInt(age);
            return n > 0 && n < 150;
        } catch (Exception e) {
            return false;
        }
    }

    // Validate phone
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
