<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 装卸船报文查询（按照箱号）</title>
        <script  type="text/javascript">
            function check() {

                var cstcode = $("#cstcode").val();
                var ctnNo = $("#ctnNo").val();
                var day = $("#day").val();

                if (cstcode === '') {
                    alert('请选择客户代码');
                    return false;
                }

                if (ctnNo === ''||ctnNo.trim() === '') {
                    alert('请输入箱号');
                    return false;
                }

                if (day === '') {
                    alert('请输入日期');
                    return false;
                }

                return true;

            }
        </script>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/coarri/get/" method="post" id="coarri" onsubmit="return check();">
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 40%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>装卸船报文查询<br/>（按照箱号）</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：<select name="cstcode" id="cstcode" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.code}" <c:if test="${cstcode eq user.code}">selected</c:if>>${user.code } - ${user.name}</option>
                                    </c:forEach>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <th>船名代码：<input name="vslname"
                                            id="vnamecd" type="text" style="width: 200px;"
                                            value="${vslname}"/>
                            </th>
                        </tr>
                        <tr>
                            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;航次：<input name="voyage"
                                                                                    id="voyage" type="text" style="width: 200px;"
                                                                                    value="${voyage}"/>
                            </th>
                        </tr>
                        <tr>
                            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱号：<input name="ctnNo"
                                                                                    id="ctnNo" type="text" style="width: 200px;"
                                                                                    value="${ctnNo}"/>
                            </th>
                        </tr>
                        <tr>
                            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期：
                                <input name="day"
                                       id="day" type="text" style="width: 200px;"
                                       value="${day}" onclick="WdatePicker({skin: 'blue', dateFmt: 'yyyy-MM-dd', maxDate: '%y-%M-{%d+1}'});"/>

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
                        <th>序号</th>
                        <th>客户代码</th>
                        <th>装卸类型</th>
                        <th>船名</th>
                        <th>航次</th>
                        <th>日志编号</th>
                        <th>创建时间</th>
                        <th>报文文件名</th>
                        <th>发送标记</th>
                        <th>查看报文</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${page.result}"  var="coarri"  varStatus="s">
                        <tr>
                            <td>${s.index+1}</td>
                            <td>${coarri.cstCode}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(coarri.ldfg, 'LOAD')}">装</c:when>
                                    <c:when test="${fn:startsWith(coarri.ldfg, 'DISC')}">卸</c:when>
                                    <c:otherwise>/</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${coarri.vslName}</td>
                            <td>${coarri.voyage}</td>
                            <td>${coarri.logId}</td>
                            <td>${coarri.createdAt}</td>
                            <td style="text-align: left">${coarri.filename}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(coarri.sendFlag, '1')}">已发</c:when>
                                    <c:otherwise>/</c:otherwise>
                                </c:choose>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/coarri/view/${coarri.logId}" target="_blank">查看</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>
