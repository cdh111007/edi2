<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 客户发送方管理</title>
        <script  type="text/javascript">
            function first() {
                var url = "${pageContext.request.contextPath}/sender/list/1";
                var form = document.getElementsByTagName("form")[0];
                form.action = url;
                form.submit();
            }
            function last() {
                var t = $("#t").val();
                var url = "${pageContext.request.contextPath}/sender/list/" + t;
                var form = document.getElementsByTagName("form")[0];
                form.action = url;
                form.submit();
            }
            function pre() {
                var p = $("#p").val();
                p--;
                if (p <= 0) {
                    p = 1;
                }
                ("#p").value = p;
                var url = "${pageContext.request.contextPath}/sender/list/" + p;
                var form = document.getElementsByTagName("form")[0];
                form.action = url;
                form.submit();
            }

            function next() {
                var p = $("#p").val();
                var t = $("#t").val();
                p++;
                if (p > t) {
                    p = t;
                }
                var url = "${pageContext.request.contextPath}/sender/list/" + p;
                var form = document.getElementsByTagName("form")[0];
                form.action = url;
                form.submit();
            }
        </script>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/sender/list/1" method="post" id="sender">
            <div class="table-wrapper">
                <input type="hidden" name="p" id="p" value="${page.pageNo}"/>
                <input type="hidden" name="t" id="t" value="${page.pages}"/>
                <table class="fl-table">
                    <thead>
                        <tr>
                            <td colspan="2"><a href="${pageContext.request.contextPath}/sender/topage">添加客户发送方代码</a></td>
                            <td colspan="2">如添加不成功，系统内已存在该记录</td>
                            <td colspan="2">
                                <a href="#" onclick="pre();">上一页</a>
                                <a href="#" onclick="next();">下一页</a>
                            </td>
                        </tr>
                        <tr>
                            <th>序号</th>
                            <th>客户代码</th>
                            <th>发送方代码</th>
                            <th>接收方代码</th>
                            <th>报文分类</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${page.result}"  var="sender" varStatus="s">
                            <tr>
                                <td>${s.index+1}</td>
                                <td>${sender.cstCode}</td>
                                <td>${sender.cstSender}</td>
                                <td>${sender.cstReceiver}</td>
                                <td>${sender.ediCate}</td>
                                <td><a href="${pageContext.request.contextPath}/sender/edit/${sender.cstCode}/${sender.ediCate}">编辑</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </form>
    </body>
</html>
