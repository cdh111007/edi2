<%-- 
    Document   : user_add
    Created on : 2018-11-28, 9:07:30
    Author     : nm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 新建FTP</title>
        <script>
            function  checkinput() {
                var cstCode = $.trim($("#cstCode").val());

                if (cstCode === '') {
                    alert("客户代码不能为空");
                    return false;
                }
                var ediCate = $.trim($("#ediCate").val());

                if (ediCate === '') {
                    alert("EDI类型不能为空");
                    return false;
                }

                var ftpIP = $.trim($("#ftpIP").val());

                if (ftpIP === '') {
                    alert("FTP地址不能为空");
                    return false;
                }

                var ftpUser = $.trim($("#ftpUser").val());

                if (ftpUser === '') {
                    alert("FTP用户名不能为空");
                    return false;
                }

                var ftpPwd = $.trim($("#ftpPwd").val());

                if (ftpPwd === '') {
                    alert("FTP密码不能为空");
                    return false;
                }

                return true;
            }
        </script>
    </head>
    <body>
        <form  action="${pageContext.request.contextPath}/ftp/add" method="post"  onsubmit="return checkinput();">
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 30%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>新建FTP</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：</th>
                            <th><select name="cstCode" id="cstCode" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.code}">${user.code } - ${user.name}</option>
                                    </c:forEach>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <th>EDI类型：</th>
                            <th>
                                <select name="ediCate" id="ediCate" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="COARRI,CODECO,COEDOR,COSECR,VESDEP" var="item">
                                        <option value="${item}"> 
                                            <c:choose>
                                                <c:when test="${'COARRI' eq item}">装卸船报文</c:when>
                                                <c:when test="${'COEDOR' eq item}">场存报文</c:when>
                                                <c:when test="${'COSECR' eq item}">完船报文</c:when>
                                                <c:when test="${'VESDEP' eq item}">离港报文</c:when>
                                                <c:otherwise>进出门报文</c:otherwise>
                                            </c:choose>
                                        </option>
                                    </c:forEach>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <th>FTP地址：</th>
                            <th><input name="ftpIP" id="ftpIP" type="text" style="width: 200px;" /></th>
                        </tr>
                        <tr>
                            <th>FTP用户名：</th>
                            <th><input name="ftpUser" id="ftpUser" type="text" style="width: 200px;" /></th>
                        </tr>
                        <tr>
                            <th>FTP密码： </th>
                            <th> <input name="ftpPwd"  id="ftpPwd" type="text" style="width: 200px;"  /></th>
                        </tr>
                        <tr>
                            <th>FTP端口号：</th>
                            <th><input name="ftpPort" id="ftpPort" type="text" style="width: 200px;" value="21"/></th>
                        </tr>
                        <tr>
                            <th>FTP路径： </th>
                            <th> <input name="ftpPath"  id="ftpPath" type="text" style="width: 200px;" value="/" /></th>
                        </tr>
                        <tr>
                            <th colspan="2"><button type="submit" id="btn" >保 存</button></th>
                        </tr>
                    </thead>
                </table>
            </div>
        </form>
    </body>
</html>
