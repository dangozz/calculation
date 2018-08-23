<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>

<html>
<body>
<h2>Hello World!</h2>
<script src="<%=basePath%>/js/jquery-3.3.1.min.js"></script>
</body>
<form id="form1">
    1&emsp;<input id="in1" name="in1" type="text" value="0"/><input id="out1" type="text"><br/>
    2&emsp;<input id="in2" name="in2" type="text" value="0"/><input id="out2" type="text"><br/>
    3&emsp;<input id="in3" name="in3" type="text" value="0"/><input id="out3" type="text"><br/>
    4&emsp;<input id="in4" name="in4" type="text" value="0"/><input id="out4" type="text"><br/>
    5&emsp;<input id="in5" name="in5" type="text" value="0"/><input id="out5" type="text"><br/>
    6&emsp;<input id="in6" name="in6" type="text" value="0"/><input id="out6" type="text"><br/>
    7&emsp;<input id="in7" name="in6" type="text" value="0"/><input id="out7" type="text"><br/>
    总价&emsp;<input id="sum" type="text" value="1"/>
</form>
<input type="button" value="计算" onclick="calculation()"/>

<br/><br/>
<a href="<%=basePath%>/1.do">1</a>
<a href="<%=basePath%>/2.do">2</a>
<a href="<%=basePath%>/3.do">3</a>
<a href="<%=basePath%>/4.do">4</a>
<a href="<%=basePath%>/logout.do">logout</a>

<br/>
<a href="<%=basePath%>/websocketTest1.jsp">1号</a>
<a href="<%=basePath%>/websocketTest2.jsp">2号</a>
<a href="<%=basePath%>/websocketTest3.jsp">3号</a>
<br/><br/>
等级<input id="lv" name="in1" type="text" value="1"/>价格<input id="price" name="in1" type="text" value="1"/><input type="button" value="啦啦啦" onclick="perfusion()"/>&emsp;&emsp;+9市场&ensp;<span id="lv9">0</span>&emsp;<span id="lv9_2">0</span>
<table border="1" bordercolor="#000000" style="border-collapse:collapse;">
    <tr style="text-align:center;">
        <td>+1</td>
        <td>+2</td>
        <td>+3</td>
        <td>+4</td>
        <td>+5</td>
        <td>+6</td>
        <td>+7</td>
        <td>+8</td>
        <td>+9</td>
        <td>+10</td>
        <td>+11</td>
        <td>+12</td>
        <td>+13</td>
        <td>+14</td>
        <td>+15</td>
    </tr>
    <tr style="text-align:center;">
        <td id="td1">1 </td>
        <td id="td2">2 </td>
        <td id="td3">3 </td>
        <td id="td4">4 </td>
        <td id="td5">5 </td>
        <td id="td6">6 </td>
        <td id="td7">7 </td>
        <td id="td8">8 </td>
        <td id="td9">9 </td>
        <td id="td10">10</td>
        <td id="td11">11</td>
        <td id="td12">12</td>
        <td id="td13">13</td>
        <td id="td14">14</td>
        <td id="td15">15</td>
    </tr>
</table>
<script>
    function calculation() {
        var str = JSON.stringify({
            "in1": $('#in1').val(),
            "in2": $('#in2').val(),
            "in3": $('#in3').val(),
            "in4": $('#in4').val(),
            "in5": $('#in5').val(),
            "in6": $('#in6').val(),
            "in7": $('#in7').val(),
            "sum": $('#sum').val()
        });
        $.ajax({
            url: "<%=basePath%>/calculation.do",
            type: "POST",
            contentType: "application/json;charset=utf-8",
            async: true,
            data:str,
            timeout: 120000,
            dataType: 'json',
            success: function (data) {
                $('#out1').val(data[0].out1);
                $('#out2').val(data[0].out2);
                $('#out3').val(data[0].out3);
                $('#out4').val(data[0].out4);
                $('#out5').val(data[0].out5);
                $('#out6').val(data[0].out6);
                $('#out7').val(data[0].out7);
            },
            error: function (data) {
                alert("请重新登录");
                window.location.href='<%=basePath%>';
            },
        });
    }

    function perfusion() {
        var str = JSON.stringify({"lv": $('#lv').val(), "price": $('#price').val()});
        $.ajax({
            url: "<%=basePath%>/perfusion.do",
            type: "POST",
            contentType: "application/json;charset=utf-8",
            async: true,
            data: str,
            timeout: 120000,
            dataType: 'json',
            success: function (data) {
                $('#td1').html(data[0].prices[0]);
                $('#td2').html(data[0].prices[1]);
                $('#td3').html(data[0].prices[2]);
                $('#td4').html(data[0].prices[3]);
                $('#td5').html(data[0].prices[4]);
                $('#td6').html(data[0].prices[5]);
                $('#td7').html(data[0].prices[6]);
                $('#td8').html(data[0].prices[7]);
                $('#td9').html(data[0].prices[8]);
                $('#td10').html(data[0].prices[9]);
                $('#td11').html(data[0].prices[10]);
                $('#td12').html(data[0].prices[11]);
                $('#td13').html(data[0].prices[12]);
                $('#td14').html(data[0].prices[13]);
                $('#td15').html(data[0].prices[14]);
                $('#lv9').html(data[0].lv9);
                $('#lv9_2').html((data[0].prices[8]/0.85).toFixed(2));
            },
            error: function (data) {
                alert("请重新登录");
                window.location.href='<%=basePath%>';
            },
        });
    }


</script>

</html>
