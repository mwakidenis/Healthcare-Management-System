package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Generate a BCrypt hash
    public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }

    // Verify password using BCrypt
    public static boolean checkPassword(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }
}
