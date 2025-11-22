package app;

import dao.impl.UserDAOImpl;
import model.User;

public class GenerateAdmin {
    public static void main(String[] args) throws Exception {
        User u = new User("Super Admin", "admin@health.com", "admin123", "ADMIN");
        new UserDAOImpl().createUser(u);
        System.out.println("Admin created with ID = " + u.getId());
    }
}
