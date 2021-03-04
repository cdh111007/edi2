<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 客户管理</title>
        <script  type="text/javascript">
            function pre() {
                var p = $("#p").val();
                p--;
                if (p <= 0) {
                    p = 1;
                }
                ("#p").value = p;
                var url = "${pageContext.request.contextPath}/user/list/" + p;
                window.location.href = url;
            }

            function next() {
                var p = $("#p").val();
                var t = $("#t").val();
                p++;
                if (p > t) {
                    p = t;
                }
                var url = "${pageContext.request.contextPath}/user/list/" + p;
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
                        <td colspan="4"><a href="${pageContext.request.contextPath}/user_add">添加客户</a></td>
                        <td colspan="9">
                            <a href="#" onclick="pre();">上一页</a>
                            <a href="#" onclick="next();">下一页</a>
                        </td>
                    </tr>
                    <tr>
                        <th>序号</th>
                        <th>客户代码</th>
                        <th>名称</th>
                        <th>邮箱地址</th>
                        <th>抄送邮箱地址</th>
                        <th>报文格式</th>
                        <th>进出门报文</th>
                        <th>装卸船报文</th>
                        <th>场存报文</th>
                        <th>完船报文</th>
                        <th>离港报文</th>
                        <th>是否实时</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach items="${page.result}"  var="user" varStatus="s">
                        <tr>
                            <td>${s.index+1}</td>
                            <td>${user.code}</td>
                            <td>${user.name}</td>
                            <td>${user.mail}</td>
                            <td>${user.ccmail}</td>
                            <td>${user.ediType}</td>
                            <td>
                                <c:choose >
                                    <c:when test="${user.codecoFlag eq '1'}">开</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose >
                                    <c:when test="${user.coarriFlag eq '1'}">开</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose >
                                    <c:when test="${user.coedorFlag eq '1'}">开</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose >
                                    <c:when test="${user.cosecrFlag eq '1'}">开</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose >
                                    <c:when test="${user.vesdepFlag eq '1'}">开</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose >
                                    <c:when test="${user.actFlag eq '1'}">是</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/user/edit/${user.code}">编辑</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>
