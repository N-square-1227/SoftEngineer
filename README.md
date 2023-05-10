# SoftEngineer
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
