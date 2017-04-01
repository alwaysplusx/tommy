package com.harmony.tommy.jpa.dao;

import java.util.List;

import com.harmony.tommy.jpa.persistence.User;

public interface UserRepository {

    public void saveUser(User user);

    public void deleteUser(User user);

    public void updateUser(User user);

    public long count();

    public User findUserById(Long id);

    public List<User> findUserByUsername(String username);
}
