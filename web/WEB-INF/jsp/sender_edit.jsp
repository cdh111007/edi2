<%-- 
    Document   : setting
    Created on : 2018-11-27, 9:18:45
    Author     : nm
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 发送方接收方代码设置</title>
        <script>
            function  checkinput() {
                var cstCode = $.trim($("#cstCode").val());

                if (cstCode === '') {
                    alert("客户代码不能为空");
                    return false;
                }
                var cstSender = $.trim($("#cstSender").val());
                if (cstSender === '') {
                    alert("发送方代码不能为空");
                    return false;
                }

                var cstReceiver = $.trim($("#cstReceiver").val());
                if (cstReceiver === '') {
                    alert("接收方代码不能为空");
                    return false;
                }

                var ediCate = $.trim($("#ediCate").val());
                if (ediCate === '') {
                    alert("报文分类不能为空");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/sender/update" method="post" onsubmit="return checkinput();" >
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 30%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>发送方代码 - 接收方代码设置</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：</th>
                            <th>
                                <select name="cstCode" id="cstCode" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.code}" <c:if test="${sender.cstCode eq user.code}">selected</c:if>>${user.code } - ${user.name}</option>
                                    </c:forEach>
                                </select>
                            </th> 
                        </tr>
                        <tr>
                            <th>发送方代码：</th>
                            <th>
                                <input type="text" id="cstSender" name="cstSender" value="${sender.cstSender}"/>
                            </th> 
                        </tr>
                        <tr>
                            <th>接收方代码：</th>
                            <th>
                                <input type="text" id="cstReceiver" name="cstReceiver" value="${sender.cstReceiver}"/>
                            </th> 
                        </tr>
                        <tr>
                            <th>报文分类：</th>
                            <th>
                                <select name="ediCate" id="ediCate" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="COARRI,CODECO,CODEOR,VESDEP,COSECR" var="item">
                                        <option value="${item}"> 
                                            <c:choose>
                                                <c:when test="${'COARRI' eq item}">装卸船报文</c:when>
                                                <c:when test="${'CODEOR' eq item}">盘存报文</c:when>
                                                <c:when test="${'VESDEP' eq item}">抵离港报文</c:when>
                                                <c:when test="${'COSECR' eq item}">完船报文</c:when>
                                                <c:otherwise>进出门报文</c:otherwise>
                                            </c:choose>
                                        </option>
                                    </c:forEach>
                                </select>
                            </th> 
                        </tr>
                        <tr>
                            <th colspan="2"><button type="submit" id="btn">保 存</button></th>
                        </tr>
                    </thead>
                </table>
            </div>

        </form>
    </body>
</html>
