package murach.data;

import murach.business.User;

public interface UserDAO {
    int insert(User user);
    int update(User user);
    int delete(User user);
    boolean emailExists(String email);
    User selectUser(String email);
}
