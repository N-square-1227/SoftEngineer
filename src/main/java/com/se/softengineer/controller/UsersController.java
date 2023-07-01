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
import com.se.softengineer.utils.TimeUtil;
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
    private UsersDataService usersDeletedService;   // 用于对user_deleted删除操作,防止混淆

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

            // 少遍历几遍那个表，万一吃不消
            if(TimeUtil.timeDifference(user.getLoginTime()) >= 1)
                usersDataService.delDeletedIndex(user);

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
            /**
             * 现在一想这部分设计的一点也不好，一个用户两个表，要是用户太多那表也太多了，完全可以在表名那里加一列是用户名
             * 查的时候按这个字段查，现在改也来不及了，唉就这样吧
             **/
            /* 创建username_Data表，存储用户所属的指标体系和数据文件 by wxy*/
            usersDataService.deleteTable(user.getUserName() + "_data");
            usersDataService.createTable(user.getUserName() + "_data");
            /* 创建已删除的表 */
            usersDataService.deleteTable(user.getUserName() + "_deleted");
            /**
             * 有很多方法是公用的，只需要传入数据表名或字段名就可以，字段名是一样的
             * 其实完全可以在user_data里加个字段，表示是否是已删除的指标体系
             * 如果是已删除的指标体系那对应的时间字段是删除时间
             * 没关系，已经写到这了；也不知道哪个耦合低一点。
             **/
            usersDeletedService.createTable(user.getUserName() + "_deleted");

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
        if(StringUtils.isNotBlank(name) && !"null".equals(name))
            lambdaQueryWrapper.like(Users::getUserName,name);
        IPage result = usersService.page(page,lambdaQueryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }

    /**
     * 删除指标体系
     * @author xly
     *************************
     * @author wxy
     * 修改删除逻辑：点击确定后先问要不要保留优化结果
     * 1. 不保留：一起删除
     * 2. 保留：把优化结果的表名写到用户所属的指标体系表里
     * 加了一个表示是否保存的参数，参数太多改成Post请求了。
     */
//    @GetMapping("/delTable")
//    public Result delTable(@RequestParam String tableName,@RequestParam String user){
    @PostMapping("/delTable")
    public Result delTable(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String tableName = (String) param.get("tableName");     // 指标体系表的数据表的名字
        String user = (String) param.get("user");

        String userTable = user + "_data";
        String userDelTable = user + "_deleted";
        try {
            /**1.0
             * 根据用户是否保存优化结果：
             * needSave==1: 保存优化记录->检查哪些表存在，加入到{user}_data表里，初始的指标体系放在已删除
             * needSave==0: 不保存，只用把初始的指标体系放在已删除
             **/
            /**2.0
             * @author wxy
             * 但是优化结果的数据是不保存的，如果要保存优化结果
             * 可以实现，但是好麻烦，不想做
             * 所以改成单纯的假删除了，就是删除的表放在“已删除"里可以还原。
             */
            usersDataService.delIndex(userTable,tableName);     // 先在user_data表里删除
            /* 加入到已删除表中 */
            usersDeletedService.addIndex(userDelTable, tableName);
//            sampleService.dropExistTable(tableName+"_data");    // 删除数据表
//            indexSymService.dropExistTable(tableName);  // 删除指标体系表

        }catch (Exception e){
            return Result.fail();
        }
        return Result.success();
    }

    /**
     * 还原,和delTable函数相反,从user_deleted表里删除,加入到user_data表里
     */
    @PostMapping("/reduction")
    public Result reduction(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String tableName = (String) param.get("tableName");     // 指标体系表的数据表的名字
        String user = (String) param.get("user");

        String userTable = user + "_data";
        String userDelTable = user + "_deleted";
        try {
            usersDeletedService.delIndex(userDelTable,tableName);     // 先在user_deleted表里删除
            /* 加入到user_data表中 */
            usersDataService.addIndex(userTable, tableName);

        }catch (Exception e){
            return Result.fail();
        }
        return Result.success();
    }

    @PostMapping("/delForever")
    public Result delForever(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String tableName = (String) param.get("tableName");     // 指标体系表的数据表的名字
        String user = (String) param.get("user");

        String userDelTable = user + "_deleted";
        try {
            usersDeletedService.delIndex(userDelTable, tableName);
            sampleService.dropExistTable(tableName+"_data");    // 删除数据表
            indexSymService.dropExistTable(tableName);  // 删除指标体系表

            // 删除优化结果的表
            /* 应该优化结果可以保存的,嗐 */
            indexSymService.dropExistTable(tableName + "_new_pca");
            indexSymService.dropExistTable(tableName + "_new_entropy");
            indexSymService.dropExistTable(tableName + "_new_kmeans");

        }catch (Exception e){
            return Result.fail();
        }
        return Result.success();
    }
}
