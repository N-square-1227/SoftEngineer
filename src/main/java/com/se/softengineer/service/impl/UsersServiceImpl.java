package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Users;
import com.se.softengineer.mapper.UsersMapper;
import com.se.softengineer.service.UsersService;
import com.se.softengineer.utils.EncryptHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 **/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper,Users> implements UsersService {

    @Autowired
    private UsersMapper usersMapper;
    /* 用于数据加密和解密 */
    EncryptHandler handler = new EncryptHandler();

    /**
     * @author lmy
     */

    @Override
    public Users userLogin(String username, String password) {
        List list = lambdaQuery()
                .eq(Users::getUserName,handler.encrypt(username))
                .eq(Users::getUserPassword,handler.encrypt(password)).list();
        Users user = null;
        if(list.size()>0)
            user = (Users)list.get(0);
        return user;
    }

    /**
     * @author lmy
     */

    private List<Users> getUserListByName(String name){
        return lambdaQuery().eq(Users::getUserName,handler.encrypt(name)).list();
    }

    /**
     * @author lmy
     */

    @Override
    public Users userRegister(String username, String password, String email) {
        List<Users> list = getUserListByName(username);
        if(list.size()>0)
            return null;
        Users user = new Users();
        user.setUserName(username);
        user.setUserPassword(password);
        user.setUserEmail(email);
        user.setRole(2);
        int i = usersMapper.insert(user);
        return i==1?user:null;
    }

    /**
     * @author lmy
     */

    public Users updateUser(Users user) {
        List<Users> list = getUserListByName(user.getUserName());
        //用户名存在(即id不一致)
        if(list.size()>0 && list.get(0).getUserID()!=user.getUserID())
            return null;
        int i = usersMapper.updateById(user);
        if(i==1)
            return user;
        return null;
    }
}
