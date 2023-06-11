package com.se.softengineer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.se.softengineer.entity.Menu;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.*;
import com.se.softengineer.utils.AesTypeHandler;
import com.se.softengineer.utils.QueryPageParam;
import com.se.softengineer.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private UsersDataService usersDataService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private IndexSymService indexSymService;

    private AesTypeHandler handler = new AesTypeHandler();

    /**
     * 时间格式化
     */
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");

    /**
     * 文件保存路径
     */
    @Value("${file-save-path}")
    private String fileSavePath;

    /**
     * 登录
     * @author lmy
     */
    @PostMapping("/login")
    public Result login(@RequestBody Users user) throws Exception {
        Users curUser = usersService.userLogin(user.getUserName(),user.getUserPassword());
        if(curUser!=null){
            List menuList = menuService.lambdaQuery().like(Menu::getMenuRight,curUser.getRole()).list();
            HashMap res = new HashMap();
            res.put("user",curUser);
            res.put("menu",menuList);
            return Result.success(res);
        }
        return Result.fail();
    }

    /**
     * 注册：新增一个用户
     * @author lmy
     */
    @PostMapping("/register")
    public Result register(@RequestBody Users user) throws Exception {
        Users curUser = usersService.userRegister(user.getUserName(),user.getUserPassword(),user.getUserEmail());
        if(curUser!=null){
            /* 创建username_Data表，存储用户所属的指标体系和数据文件 by wxy*/
            usersDataService.deleteTable(user.getUserName() + "_data");
            usersDataService.createTable(user.getUserName() + "_data");

            List menuList = menuService.lambdaQuery().like(Menu::getMenuRight,curUser.getRole()).list();
            HashMap res = new HashMap();
            res.put("user",curUser);
            res.put("menu",menuList);
            return Result.success(res);
        }
        return Result.fail();
    }

    /**
     * 管理员修改用户信息
     * @author lmy
     */
    @PostMapping("/update")
    public Result update(@RequestBody Users user) throws Exception {
        /* 更新用户信息，如果修改了用户名，同时要更新user_data表 */
        return usersService.updateUser(user)!=null ? Result.success():Result.fail();
    }

    /**
     * 修改个人信息
     * @author lmy
     */
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody QueryPageParam query) throws Exception {
        HashMap param = query.getParam();
        String name = (String)param.get("userName");
        String email = (String) param.get("userEmail");
        Integer id = (Integer) param.get("userID");
        Users newUser = usersService.getById(id);
        newUser.setUserName(name);
        newUser.setUserEmail(email);
        return usersService.updateUser(newUser)!=null ? Result.success() : Result.fail();
    }

    /**
     * 修改用户密码
     * @author lmy
     */
    @PostMapping("/updatePwd")
    public Result updatePwd(@RequestBody QueryPageParam query) throws Exception {
        HashMap param = query.getParam();
        String newPwd = (String)param.get("newPwd");
        Integer id = (Integer) param.get("userID");

        return usersService.lambdaUpdate().set(Users::getUserPassword, handler.encrypt(newPwd))
                .eq(Users::getUserID,id).update(new Users()) ? Result.success() : Result.fail();
    }

    /**
     * 删除用户(用户表以及用户角色表都要删除)
     * @author lmy
     */
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer userID){

        Users user = usersService.getById(userID);
        //管理员用户不可删除
        if(user.getRole()==1)
            return Result.fail();
        try {   /* 同时删除用户所属的data表和相关的指标体系及数据表 */
            usersDataService.deleteTable(user.getUserName() + "_data");
        }catch(Exception e){
            return Result.fail();
        }
        return usersService.removeById(userID) ? Result.success():Result.fail();
    }

    /**
     * 查询一个用户的信息
     * @author lmy
     */
    @GetMapping("/userDetail")
    public Result userDetail(@RequestParam Integer userID){
        Users user = usersService.getById(userID);
        return user!=null ?  Result.success(user):Result.fail();
    }

    /**
     * 返回用户列表
     * @author lmy
     */
    @PostMapping("/userList")
    public Result list(){
        List list = usersService.list();
        return list.size()>0 ? Result.success(list):Result.fail();
    }

    /**
     * 分页返回用户列表
     * @author lmy
     */
    @PostMapping("/userListPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<Users> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)) {
            try {
                lambdaQueryWrapper.like(Users::getUserName,handler.encrypt(name));
            } catch (Exception e) {
                return Result.fail();
            }
        }
        IPage result = usersService.page(page,lambdaQueryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }

    /*
    删除指标体系
    @author xly
     */
    @GetMapping("/delTable")
    public Result delTable(@RequestParam String tableName,@RequestParam String user){
        String userTable = user + "_data";
        try {
            usersDataService.delIndex(userTable,tableName);
            sampleService.dropExistTable(tableName+"_data");
            indexSymService.dropExistTable(tableName);
            indexSymService.dropExistTable(tableName+"_new_kmeans");
            indexSymService.dropExistTable(tableName+"_new_pca");
            indexSymService.dropExistTable(tableName+"_new_entropy");
        }catch (Exception e){
            return Result.fail();
        }
        return Result.success();
    }
}
