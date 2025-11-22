package dao;

import model.User;
import java.util.List;

public interface UserDAO {

    boolean createUser(User user) throws Exception;

    User authenticate(String email, String password) throws Exception;

    List<User> getAllUsers() throws Exception;

    boolean updateUser(User user) throws Exception;

    boolean deleteUser(int userId) throws Exception;

    User getUserById(int id) throws Exception;
}
