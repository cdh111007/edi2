<%-- 
    Document   : link_add
    Created on : 2018-11-28, 9:08:57
    Author     : nm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 添加客户箱主关联</title>
        <script>
            function  checkinput() {
                var cstCode = $.trim($("#cstCode").val());
                var ctnOperator = $.trim($("#ctnOperator").val());
                if (cstCode === '') {
                    alert("客户代码不能为空");
                    return false;
                }
                if (ctnOperator === '') {
                    alert("持箱人代码不能为空");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form  action="${pageContext.request.contextPath}/link/add" method="post" id="user_add_form" onsubmit="return checkinput();">
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 30%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>添加客户箱主关联</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：</th>
                            <th><select name="cstCode" id="cstCode" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.code}" <c:if test="${cstcode eq user.code}">selected</c:if>>${user.code } - ${user.name}</option>
                                    </c:forEach>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <th>持箱人：</th>
                            <th><input name="ctnOperator"  id="ctnOperator" type="text" style="width: 200px;" /></th>
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
