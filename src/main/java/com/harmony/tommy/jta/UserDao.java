package com.harmony.tommy.jta;

import com.harmony.tommy.jpa.persistence.User;

public interface UserDao {

    public void saveUser(User user);

    public void deleteUser(User user);

    public long count();

    public void saveWithCMTDao(User user1, User user2);

    public void saveWithBMTDao(User user1, User user2);
}
