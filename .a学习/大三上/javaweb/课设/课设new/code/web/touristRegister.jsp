<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2022/12/7
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>游客注册</title>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>游客注册</small>
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
<form action="/user/insertUser" method="post" enctype="multipart/form-data">
    <p style=" margin:0 auto; text-align:center;">
    <fieldset>
        <br><br>
        <label>用户名</label>
        <input type="text" name="username" id="username" required="required"><br><br>
        <label>密&nbsp;&nbsp;码</label>
        <input type="password" name="password" id="password" required="required"><br><br>
        <label>确认密码</label>
        <input type="password" name="password2" id="password2" required="required"><br><br>
        <label>真实姓名</label><input type="text" name="realname" required="required"><br><br>
        <label>联系电话</label><input type="tel" name="telephone" required="required"><br><br>
        <label>电子邮箱</label><input type="email" name="email" required="required"><br><br>
        <label>家庭住址</label><input type="text" name="address" required="required"><br><br>
        <label>用户性别</label><input type="text" name="gender" required="required"><br><br>
        <label>问题设置</label><input type="text" name="question" required="required"><br><br>
        <label>问题答案</label><input type="text" name="answer" required="required"><br><br>
    <label>上传图片</label><input type="file" name="file" ><br><br>

    <input type="submit" value="注册">
    </fieldset>
    </p>
</form>
</body>
</html>
