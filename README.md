# SoftEngineer
======================画图=========================

安装echarts  ：管理员身份打开cmd，进入vue文件夹所在路径，输入下列命令即可

```
npm install echarts --save
```

======================new=========================

一、删除了userrole类

新增动态路由->动态菜单栏

安装插件：管理员身份打开cmd，进入vue文件夹所在路径，输入下列命令即可

```
1、npm i vuex@3.0.0   //用于动态管理

2、npm i vuex-persistedstate    //用于避免刷新导致数据丢失
```

二、修改了UsersController: login、register方法、UsersServiceImpl: userRegister方法、删除了NodeConreoller(好像叫这个名字)



三、修改了vue文件

将Main.vue修改为：userManage.vue，并移到Admin目录下

router/index.js也作了修改：将原本的导航路径删除，通过store/index.js进行动态添加

Register.vue 、Login.vue: doRegister、doLogin进行了修改



四、新增数据表menu，以及对应的实体类、service等

```
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menuCode` varchar(8) DEFAULT NULL COMMENT '菜单编码',
  `menuName` varchar(16) DEFAULT NULL COMMENT '菜单名字',
  `menuClick` varchar(16) DEFAULT NULL COMMENT '点击触发的函数',
  `menuRight` varchar(8) DEFAULT NULL COMMENT '权限 1管理员 2用户',
  `menuComponent` varchar(200) DEFAULT NULL COMMENT '菜单路径',
  `menuIcon` varchar(100) DEFAULT NULL COMMENT '图标名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '001', '用户管理', 'userManage', '1', 'Admin/userManage', 'el-icon-user-solid');
INSERT INTO `menu` VALUES ('2', '002', '导入数据', 'ImportFiles', '2','User/ImportFiles', 'el-icon-upload');
INSERT INTO `menu` VALUES ('3', '003', '指标优化', '3-1', '2', 'Home', 'el-icon-s-promotion');
```



==================================================

pom.xml

项目拉下来之后可能需要更改 JDK，我的是 Oracle 17.0.2，在该文件中表现为

16 ~ 18 行

        <properties>
            <java.version>17</java.version>
        </properties>



注意 MySQL 连接库版本，我的是 5.1.47

lombok

这个插件是用来简化实体类的，下面会说明

            <!--lombok-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.8</version>
            </dependency>



👇这个是抄的，可能用不到，先放着了

            <!--spring2.X集成redis所需common-pool12-->
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-pool2</artifactId>
                <version>2.6.0</version>
            </dependency>

application.properties

可能需要改的地方如下：

    # 应用服务 WEB 访问端口
    server.port=8877



数据库用户名密码，数据库名，driver-class-name（如果你的是 8.0 以上的 MySQL，那么该项的值是com.mysql.cj.jdbc.Driver

    # 应用连接数据库
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.url=jdbc:mysql://localhost:3306/se?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver

如果有上传文件的地址，改成自己的

    # 文件上传地址
    file-save-path=D:\\WorkSpace\\SoftEngineer\\upload

Config 包

不用动里面的东西。

简单理解：Cors 解决前后端分离后数据的传输问题；MybatisPlusConfig 是 mybatis 插件的配置类（里面配的数据库是 MySQL，咱们都一样就不用改），WebConfig 解决文件上传的问题。

其他代码我只保留了一个方法，其他方法的使用大差不差，可以看被注释掉的其他增删改查的方法

---

mb 用法请参阅：

:link:简介 | MyBatis-Plus (baomidou.com)https://www.baomidou.com/pages/24112f/#%E7%89%B9%E6%80%A7

Mapper 包

该写法是 mybatis-plus 的写法。@Mapper 注解相当于配置自动扫描，extends BaseMapper<Users>是配置该 Mapper 对应的实体类，配好后，该 Mapper 不用再写代码（一般来说，如果需求很特别还是得写）

    @Mapper
    public interface UsersMapper extends BaseMapper<Users> {
    
    }



程序的启动

启动后，从SoftEngineerApplication.java 进入（没什么用的知识增加了.jpg）
