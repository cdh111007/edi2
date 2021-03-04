<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 进出门报文查询</title>
        <script  type="text/javascript">
            function first() {
                var url = "${pageContext.request.contextPath}/codeco/list/1";
                var form = document.getElementsByTagName("form")[0];
                form.action = url;
                form.submit();
            }
            function last() {
                var t = $("#t").val();
                var url = "${pageContext.request.contextPath}/codeco/list/" + t;
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
                var url = "${pageContext.request.contextPath}/codeco/list/" + p;
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
                var url = "${pageContext.request.contextPath}/codeco/list/" + p;
                var form = document.getElementsByTagName("form")[0];
                form.action = url;
                form.submit();
            }
        </script>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/codeco/search/1" method="post" >
            <input type="hidden" name="p" id="p" value="${page.pageNo}"/>
            <input type="hidden" name="t" id="t" value="${page.pages}"/>
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 40%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>进出门报文查询</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：<select name="cstcode" id="cstcode" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.code}" <c:if test="${cstcode eq user.code}">selected</c:if>>${user.code } - ${user.name}</option>
                                    </c:forEach>
                                </select></th>
                        </tr>
                        <tr>
                            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱号：<input name="cntrno"
                                                                                    id="cntrno" type="text" style="width: 200px;"
                                                                                    value="${ctn_no}"/></th>
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
                    <tr >
                        <td colspan="10">
                            <a href="#" onclick="first();">首页</a>
                            <a href="#" onclick="pre();">上一页</a>
                            <a href="#" onclick="next();">下一页</a>
                            <a href="#" onclick="last();">尾页</a>
                        </td>
                        <td>${page.pageNo}/${page.pages}</td>
                    </tr>
                    <tr>
                        <th>序号</th>
                        <th>客户代码</th>
                        <th>进出类型</th>
                        <th>箱号</th>
                        <th>日志编号</th>
                        <th>进场时间</th>
                        <th>出场时间</th>
                        <th>创建时间</th>
                        <th>报文名</th>
                        <th>发送标记</th>
                        <th>查看报文</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${page.result}"  var="codeco" varStatus="s">
                        <tr>
                            <td>${s.index+1}</td>
                            <td>${codeco.cstCode}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(codeco.msgType, 'DV')}">拆装箱</c:when>
                                    <c:when test="${fn:startsWith(codeco.inoutfg, 'GATE-OUT')}">出</c:when>
                                    <c:when test="${fn:startsWith(codeco.inoutfg, 'GATE-IN')}">进</c:when>
                                    <c:when test="${fn:startsWith(codeco.inoutfg, 'LOAD')}">装</c:when>
                                    <c:when test="${fn:startsWith(codeco.inoutfg, 'DISC')}">卸</c:when>
                                    
                                    <c:otherwise>/</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${codeco.ctnNo}</td>
                            <td>${codeco.logId}</td>
                            <td>
                                ${codeco.inYardTime}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(codeco.outYardTime, '1970')}"></c:when>
                                    <c:otherwise>${codeco.outYardTime}</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${codeco.createAt}</td>
                            <td style="text-align: left">${codeco.filename}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(codeco.sendFlag, '1')}">已发</c:when>
                                    <c:otherwise>/</c:otherwise>
                                </c:choose>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/codeco/view/${codeco.logId}" target="_blank">查看</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>
