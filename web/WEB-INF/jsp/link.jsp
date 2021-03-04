<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 客户箱主关系管理</title>
        <script  type="text/javascript">
            function pre() {
                var p = $("#p").val();
                var t = $("#t").val();
                p--;
                if (p <= 0) {
                    p = 1;
                }
                ("#p").value = p;
                var url = "${pageContext.request.contextPath}/link/list/" + p;
                window.location.href = url;
            }

            function next() {
                var p = $("#p").val();
                var t = $("#t").val();
                p++;
                if (p > t) {
                    p = t;
                }
                var url = "${pageContext.request.contextPath}/link/list/" + p;
                window.location.href = url;
            }
        </script>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/link/search/1" method="post" >
            <input type="hidden" name="p" id="p" value="${page.pageNo}"/>
            <input type="hidden" name="t" id="t" value="${page.pages}"/>
            <label>  ${result}</label>
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 80%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>客户箱主关系查询</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：<input name="code"
                                            id="code" type="text" style="width: 200px;"
                                            value="${code}"/>
                            </th>
                        </tr>
                        <tr>
                            <th><button type="submit" id="btn">查 询</button></th>
                                     
                        </tr>
                    </thead>
                </table>
            </div>
        </form>
        <div class="table-wrapper">
            <table class="fl-table">
                <thead>
                    <tr>
                        <td colspan="3"><a href="${pageContext.request.contextPath}/link/topage">添加关联</a></td>
                        <td >
                            <a href="#" onclick="pre();">上一页</a>
                            <a href="#" onclick="next();">下一页</a>
                        </td>
                    </tr>
                    <tr>
                        <th>序号</th>
                        <th>客户代码</th>
                        <th>持箱人代码</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${page.result}"  var="link" varStatus="s">
                        <tr>
                            <td>${s.index+1}</td>
                            <td>${link.cstCode}</td>
                            <td>${link.ctnOperator}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/link/delete/${link.cstCode}/${link.ctnOperator}">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>
