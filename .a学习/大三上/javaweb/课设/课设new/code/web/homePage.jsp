<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2022/12/7
  Time: 15:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<div style="text-align: center;">
    <div class="row">
        <div class="col-md-12 column">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="page-header">
                        <h1>
                            <small>欢迎使用文物标本管理平台</small>
                        </h1>
                    </div>
                </div>
            </div>

            <a class="btn btn-primary" style="width: 100px" href="user/regOrLogin">游客</a><br><br>
            <a class="btn btn-primary" style="width: 100px" href="/staff/toLogin">工作人员</a><br><br>
            <a class="btn btn-primary" style="width: 100px" href="/admin/toLogin">管理员</a>
        </div>
    </div>
</div>
</body>
</html>

