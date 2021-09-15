package service;

import Bean.User;
import DAO.userDao;

import java.util.List;

public class userService {
    userDao userDao = new userDao();
    public List<User> queryAllUsers(){
        return userDao.queryUsers();
    }
    public boolean addUser(User user){
        return userDao.addUser(user);
    }
    public String login(User user){
        return userDao.login(user);
    }
    public boolean deleteUser(int id){
        return userDao.deleteUser(id);
    }
}
