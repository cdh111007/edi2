<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 客户FTP管理</title>
        <script  type="text/javascript">
            function pre() {
                var p = $("#p").val();
                p--;
                if (p <= 0) {
                    p = 1;
                }
                ("#p").value = p;
                var url = "${pageContext.request.contextPath}/ftp/list/" + p;
                window.location.href = url;
            }

            function next() {
                var p = $("#p").val();
                var t = $("#t").val();
                p++;
                if (p > t) {
                    p = t;
                }
                var url = "${pageContext.request.contextPath}/ftp/list/" + p;
                window.location.href = url;
            }
        </script>
    </head>
    <body>

        <div class="table-wrapper">
            <input type="hidden" name="p" id="p" value="${page.pageNo}"/>
            <input type="hidden" name="t" id="t" value="${page.pages}"/>
            <table class="fl-table">
                <thead>
                    <tr>
                        <td colspan="3"><a href="${pageContext.request.contextPath}/ftp/topage">添加FTP</a></td>
                        <td colspan="1">${result}</td>
                        <td colspan="5">
                            <a href="#" onclick="pre();">上一页</a>
                            <a href="#" onclick="next();">下一页</a>
                        </td>
                    </tr>
                    <tr>
                        <th>序号</th>
                        <th>客户代码</th>
                        <th>FTP地址</th>
                        <th>FTP用户名</th>
                        <th>FTP密码</th>
                        <th>FTP端口</th>
                        <th>FTP路径</th>
                        <th>EDI类型</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach items="${page.result}"  var="ftp" varStatus="s">
                        <tr>
                            <td>${s.index+1}</td>
                            <td>${ftp.cstCode}</td>
                            <td>${ftp.ftpIP}</td>
                            <td>${ftp.ftpUser}</td>
                            <td>${ftp.ftpPwd}</td>
                            <td>${ftp.ftpPort}</td>
                            <td>${ftp.ftpPath}</td>
                            <td>${ftp.ediCate}</td>
                            <td><a href="${pageContext.request.contextPath}/ftp/edit/${ftp.cstCode}/${ftp.ediCate}">编辑</a>
                                <!--<a href="${pageContext.request.contextPath}/ftp/delete/${ftp.cstCode}/${ftp.ediCate}">删除</a>-->
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>
