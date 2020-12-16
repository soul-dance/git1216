<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <title>Insert title here</title>
</head>
<body>
<form action="user/login" class="form-horizontal" role="form">
    <div class="form-group form-group-lg">
        <div style="width: 350px;">
            <input class="form-control" type="text" placeholder="用户名">
        </div>
        <div style="width: 350px; position: relative;top: 20px;">
            <input class="form-control" type="password" placeholder="密码">
        </div>
        <div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

            <span id="msg"></span>

        </div>
        <button type="submit" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
    </div>
</form>
</body>
</html>