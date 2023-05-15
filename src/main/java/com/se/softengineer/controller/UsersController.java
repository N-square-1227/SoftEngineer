package com.se.softengineer.controller;

import com.se.softengineer.entity.Users;
import com.se.softengineer.service.UsersService;
import com.se.softengineer.utils.Code;
import com.se.softengineer.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@RestController // RestController 相当于 Controller 和 RestBody
@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    /**
     * 时间格式化
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");

    /**
     * 图片保存路径
     */
    @Value("${file-save-path}")
    private String fileSavePath;

    /**
     * 新增一个用户
     * @param users
     * @return
     */
    @PostMapping("/add-one-user")
    public R addBookInfo(@RequestBody Users users){
        int flag = usersService.addUsersInfo(users);
        if (flag != 1){
            return new R(Code.WORK_ERR,"新增用户信息失败！");
        }else {
            return new R(Code.WORK_OK,"新增用户信息成功！");
        }
    }

    /**
     * @Author 南希诺
     * @create 2023.5.10
     * 熵权法
     * @return
     */
    @GetMapping("/en")
    public boolean entropy() throws Exception {
        return usersService.Entropy();
    }

    /**
     * @author xly
     * @return
     * @throws Exception
     */
    @GetMapping("/kmeans")
    public boolean kmeans() throws Exception {
        return usersService.runkmeans();
    }
}
