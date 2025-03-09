package service;

import dao.UserDAO;
import model.User;
import exception.UserAlreadyExistsException;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void registerUser(User user) throws UserAlreadyExistsException {
        if (userDAO.isEmailExist(user.getEmail())) {
            throw new UserAlreadyExistsException("email", "This email is already registered!");
        }
        if (userDAO.isPhoneExist(user.getPhone())) {
            throw new UserAlreadyExistsException("phone", "This phone number is already registered!");
        }

        userDAO.registerUser(user);
    }
}
