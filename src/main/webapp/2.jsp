<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>

<html>
<body>
<h2>2</h2>
<a href="<%=basePath%>/index.jsp">lalala</a>
</body>
</html>
