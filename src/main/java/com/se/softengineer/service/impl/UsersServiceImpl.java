package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Users;
import com.se.softengineer.mapper.UsersDataMapper;
import com.se.softengineer.mapper.UsersMapper;
import com.se.softengineer.service.UsersService;
import com.se.softengineer.utils.AesTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 **/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper,Users> implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersDataMapper usersDataMapper;

    AesTypeHandler handler = new AesTypeHandler();


    /**
     * @author lmy
     */

    @Override
    public Users userLogin(String username, String password) throws Exception {
        List list = lambdaQuery()
                .eq(Users::getUserName,username)
                .eq(Users::getUserPassword,handler.encrypt(password)).list();
        Users user = null;
        if(list.size()>0) {
            user = (Users) list.get(0);
        }
//        System.out.println(user);
        return user;
    }

    /**
     * @author lmy
     */

    private List<Users> getUserListByName(String name) throws Exception {
        return lambdaQuery().eq(Users::getUserName,name).list();
    }

    /**
     * @author lmy
     */

    @Override
    public Users userRegister(String username, String password, String email) throws Exception {
        List<Users> list = getUserListByName(username);
        if(list.size()>0)
            return null;
        Users user = new Users();
        user.setUserName(username);
        user.setUserPassword(password);
        user.setUserEmail(email);
        user.setRole(2);

        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        user.setLoginTime(format);

        int i = usersMapper.insert(user);
        return i==1?user:null;
    }

    /**
     * @author lmy
     */

    @Override
    public Users updateUser(Users user) throws Exception {
        List<Users> list = getUserListByName(user.getUserName());
        //用户名存在(即id不一致)
        if(list.size()>0 && list.get(0).getUserID()!=user.getUserID())
            return null;

        /* 修改用户信息时有可能对用户名做了更改，因此需要修改存储用户名下指标体系的数据表username_data. by wxy*/
        /* 最后的版本的是用户名无法修改，所以这段不用了 */
//        Users origin_user = usersMapper.selectById(user.getUserID());
//        try {
//            String origin_name = origin_user.getUserName();
//            if(!user.getUserName().equals(origin_name)) {  // 修改了名字才需要改
//                int role = origin_user.getRole();
//                if (role == 2)   // 只有普通用户才有这个表
//                    usersDataMapper.renameTable(origin_name + "_data", user.getUserName() + "_data");
//            }
//        }catch (Exception e) {
//            return null;
//        }
        int i = usersMapper.updateById(user);
        if(i==1)
            return user;
        return null;
    }

    @Override
    public Boolean updateUserLoginTime(Users user){
        Date date = new Date(System.currentTimeMillis());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String format = dateFormat.format(date);
        user.setLoginTime(format);
        System.out.println(user);
        return usersMapper.updateById(user)==1;
    }
}
