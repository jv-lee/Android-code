package com.lee.db.dao;

import com.lee.db.database.BaseDao;
import com.lee.db.entity.User;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-21
 * @description 维护用户的共有数据
 */
public class UserDao extends BaseDao<User> {

    @Override
    public long insert(User entity) {
        //查询该表中所有的用户记录
        List<User> list = query(new User());
        User where = null;
        //将所有用户修改状态为为登陆状态
        for (User user : list) {
            where = new User();
            where.setId(user.getId());
            where.setStatus(0);
            update(user, where);
        }
        //把当前用户修改为登录状态
        entity.setStatus(1);
        return super.insert(entity);
    }

    /**
     * 获取当前登录的User
     *
     * @return 登陆用户实体数据
     */
    public User getCurrentUser() {
        User user = new User();
        user.setStatus(1);
        List<User> userList = query(user);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

}
