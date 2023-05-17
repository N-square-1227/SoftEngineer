package com.se.softengineer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.UserroleService;
import com.se.softengineer.service.UsersService;
import com.se.softengineer.utils.Code;
import com.se.softengineer.utils.QueryPageParam;
import com.se.softengineer.utils.Result;
import org.apache.catalina.User;
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
    private UserroleService userroleService;

    /**
     * 时间格式化
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");

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
    public Result login(@RequestBody Users user){
        Users curUser = usersService.userLogin(user.getUserName(),user.getUserPassword());
//        Users curUser = usersService.userLogin(user);
//        String roleName="";
//        if(curUser!=null)
//            roleName = userroleService.getRoleNameByUserID(curUser.getUserID());
        return curUser!=null?Result.success(curUser):Result.fail();
    }

    /**
     * 注册：新增一个用户
     * @author lmy
     */
    @PostMapping("/register")
    public Result register(@RequestBody Users user){
        Users curUser = usersService.userRegister(user.getUserName(),user.getUserPassword(),user.getUserEmail());
        String roleName = userroleService.getRoleNameByUserID(curUser.getUserID());
        return curUser!=null?Result.success(roleName,curUser):Result.fail();
    }

    /**
     * 修改用户信息
     * @author lmy
     */
    @PostMapping("/update")
    public Result update(@RequestBody Users user){
        return usersService.updateUser(user)!=null ? Result.success():Result.fail();
    }

    /**
     * 修改用户信息
     * @author lmy
     */
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody QueryPageParam query){
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
    public Result update(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String newPwd = (String)param.get("newPwd");
        Integer id = (Integer) param.get("userID");

        return usersService.lambdaUpdate().set(Users::getUserPassword, newPwd)
                .eq(Users::getUserID,id).update(new Users()) ? Result.success() : Result.fail();
    }

    /**
     * 删除用户(用户表以及用户角色表都要删除)
     * @author lmy
     */
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer userID){
        if(usersService.removeById(userID))
            return userroleService.removeById(userID) ? Result.success():Result.fail();
        return Result.fail();
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



//    /**
//     * 图片上传
//     *
//     * @param file
//     * @param request
//     * @return
//     */
//    @PostMapping("/upload") // @RequestParam is Spring 中的注解，用于从 HTTP 请求中获取指定名称的参数值。
//    // 在这个例子中，它用于获取名为 "file" 的参数值，通常用于上传文件的场景。
//    public R uploadPicture(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//
//        String directory = simpleDateFormat.format(TimeUtil.getTime());
//
//
//        /**
//         * 文件保存目录 E:/images/2020/03/15/
//         * 如果目录不存在，则创建
//         */
//        File dir = new File(fileSavePath + directory);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        System.out.println("图片上传，保存的位置:" + fileSavePath + directory);
//
//        /**
//         * 给文件重新设置一个名字
//         * 后缀
//         */
//        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
//
//        //4.创建这个新文件
//        File newFile = new File(fileSavePath + directory + newFileName);
//        //5.复制操作
//        try {
//            file.transferTo(newFile);
//            //协议 :// ip地址 ：端口号 / 文件目录(/images/2020/03/15/xxx.jpg)
//            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/" + directory + newFileName;
//            System.out.println("图片上传，访问URL：" + url);
//            return new R(Code.WORK_OK, "上传成功", url);
//        } catch (IOException e) {
//            return new R(Code.WORK_ERR, "IO异常");
//        }
//    }
//
//
//    /**
//     * 书城条件分页查询
//     * @param current
//     * @param pageSize
//     * @param book
//     * @return
//     */
//    @PostMapping("/list/{current}/{pageSize}")
//    // @PathVariable是Spring MVC中的注解，用于从URL中获取变量值。
//    // 它通常用于RESTful Web服务中，以便从URL中获取参数值，然后将其传递给控制器方法进行处理。
//    // 例如，如果我们有一个URL `/users/{id}`，其中`{id}`是变量，我们可以使用@PathVariable注解来获取`id`的值，并将其传递给控制器方法。
//    public R selectPage(@PathVariable("current") long current,@PathVariable("pageSize") long pageSize,
//                        @RequestBody Book book){
//        //mybatis-plus分页
//        Page<Book> page = new Page<>(current, pageSize);
//        QueryWrapper<Book> wrapper = new QueryWrapper<>();
//
//        String name = book.getName();
//        if (!StringUtils.isEmpty(name)){
//            wrapper.like("name",name);
//        }
//        wrapper.eq("is_deleted","0");
//        wrapper.orderByDesc("gmt_modified");
//
//        Page<Book> result = bookService.selectPage(page, wrapper);
//        if (StringUtils.isEmpty(String.valueOf(result.getRecords()))){
//            return new R(Code.WORK_ERR,"查询为空");
//        }
//        return new R(Code.WORK_OK,"操作成功",result);
//    }

//
//    /**
//     * 根据id获取书本信息
//     * @param id
//     * @return
//     */
//    @GetMapping("/get-one-book/{id}")
//    public R getOneBook(@PathVariable("id") Integer id){
//        Book result = bookService.getOneBook(id);
//        if (!Strings.isNotEmpty(result.getName())){
//            return new R(Code.WORK_ERR,"根据id获取书本信息失败！");
//        }
//        return new R(Code.WORK_OK,"获取书本信息成功",result);
//    }
//
//    /**
//     * 修改一本书的信息
//     * @param book
//     * @return
//     */
//    @PostMapping("/upd-one-book")
//    public R updOneBook(@RequestBody Book book){
//        int flag = bookService.updOneBook(book);
//        if (flag != 1){
//            return new R(Code.WORK_ERR,"新增书本信息失败！");
//        }else {
//            return new R(Code.WORK_OK,"新增书本信息成功！");
//        }
//    }
//
//    /**
//     * 删除一本书
//     * @param book
//     * @return
//     */
//    @PostMapping("/delete-one-book")
//    public R deleteOneBook(@RequestBody Book book){
//        int flag = bookService.deleteOneBook(book);
//        if (flag != 1){
//            return new R(Code.WORK_ERR,"新增书本信息失败！");
//        }else {
//            return new R(Code.WORK_OK,"新增书本信息成功！");
//        }
//    }
}
