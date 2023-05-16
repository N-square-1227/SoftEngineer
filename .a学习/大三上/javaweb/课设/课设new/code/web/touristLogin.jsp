<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>
<head>
    <title>新增书籍</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>游客登录</small>
                </h1>
            </div>
        </div>
    </div>

    <style type="text/css">
        fieldset
        {
            margin:30px auto;
            width:300px;
            height:300px;
            padding:20px auto;
            color:blue;
            text-align:center;
        }

        /*label部分CSS才是重点*/
        label
        {
            display: inline-block;
            width: 60px;
            text-align: justify;
            text-align-last: justify;
            margin-right: 10px;
        }


    </style>

    <form action="/user/Login" method="post">
        <p style=" margin:0 auto; text-align:center;">
        <fieldset>
            <br><br>
            <label>用户名</label>
            <input type="text" name="username" id="username" required="required"><br><br>
            <label>密&nbsp;&nbsp;码</label>
            <input type="password" name="password" id="password" required="required"><br><br><br>
            <input type="submit" value="登录">

    </fieldset>
        </p>
    </form>
    <fieldset>
        <a class="btn btn-primary" style="width: 100px;text-align: center" href="/user/tofind">忘记密码</a><br><br>
    </fieldset>
<%--    <form>--%>
<%--        <table >--%>
<%--            <tr><td>用户名：</td><td><input type="text" ></td></tr><br>--%>
<%--            <tr><td>密码：</td><td><input type="text"></td></tr><br>--%>
<%--            <tr><td>真实姓名：</td><td><input type="text"></td></tr><br>--%>
<%--        </table>--%>
<%--    </form>--%>
</div>