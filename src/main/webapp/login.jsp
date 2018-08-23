<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
<body onload="check()">
    <form action="<%=basePath%>/login.do" method="post">
        <input type="text" name="username">
        <input type="text" name="password">
        <%--<input type="checkbox" name="rememberMe" value="true">--%>
        <input type="text" name="jcaptchaCode">
        <img id="img1" src="<%=basePath%>/jcaptcha.jpg" title="点击更换验证码" onclick="change();">
        <input type="submit" value="dango">
    </form>
</body>
<script>
    function check() {
        var err="<%=session.getAttribute("err")%>";
        if(err!="null")
            alert(err);
        var path=window.location.href;
        var index=path.indexOf("kickout");
        if(index>-1)
            alert("啦啦啦");
    }

    function change() {
        var img = document.getElementById('img1');
        img.src = '<%=basePath%>/jcaptcha.jpg';
    }



</script>
</html>
