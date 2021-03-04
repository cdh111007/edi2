<%--
    Document   : index
    Created on : 2017-3-13, 10:20:13
    Author     : martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("type", request.getParameter("type"));
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pgwmenu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <style>
            h1 { padding: 30px 0; font: 32px "Microsoft Yahei"; text-align: center;}
            h2 { margin-top: 50px; font: 24px "Microsoft Yahei";}
            body { font-size: 14px;}
            .dowebok-explain { margin-top: 20px; font-size: 14px; text-align: center; color: #f50;}
            .pgwMenu a { padding: 0 30px;}
        </style>
    </head>
    <body>

        <ul class="pgwMenu">
            <li><a <c:if test="${type eq 'codeco'}">class="selected"</c:if> href="${pageContext.request.contextPath}/codeco/list/1?type=codeco">进出门报文</a></li>
            <li><a <c:if test="${type eq 'coarri'}">class="selected"</c:if> href="${pageContext.request.contextPath}/coarri/list/1?type=coarri">装卸船报文</a></li>
            <li><a <c:if test="${type eq 'user'}">class="selected"</c:if> href="${pageContext.request.contextPath}/user/list/1?type=user">客户管理</a></li>
            <li><a <c:if test="${type eq 'ftp'}">class="selected"</c:if> href="${pageContext.request.contextPath}/ftp/list/1?type=ftp">客户FTP管理</a></li>
            <li><a <c:if test="${type eq 'sender'}">class="selected"</c:if> href="${pageContext.request.contextPath}/sender/list/1?type=sender">客户发送方管理</a></li>
            <li><a <c:if test="${type eq 'link'}">class="selected"</c:if> href="${pageContext.request.contextPath}/link/list/1?type=link">客户箱主关系管理</a></li>
            <li><a <c:if test="${type eq 'codsend'}">class="selected"</c:if> href="${pageContext.request.contextPath}/codeco_resend/topage?type=codsend">进出门重发</a></li>
            <li><a <c:if test="${type eq 'dvsend'}">class="selected"</c:if> href="${pageContext.request.contextPath}/dv_resend/topage?type=dvsend">拆装箱重发</a></li>
            <li><a <c:if test="${type eq 'coasend'}">class="selected"</c:if> href="${pageContext.request.contextPath}/coarri_resend/topage?type=coasend">装卸船重发</a></li>
            <li><a <c:if test="${type eq 'getcoarri'}">class="selected"</c:if> href="${pageContext.request.contextPath}/get_coarri/?type=getcoarri">装卸船查询（按箱号）</a></li>
            </ul>

            <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/My97DatePicker/WdatePicker.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/pgwmenu.min.js"></script>
        <script type="text/javascript">
            $(function () {
                $('.pgwMenu').pgwMenu({
                    dropDownLabel: '菜单',
                    viewMoreLabel: '更多<span class="icon"></span>'
                });

            });
            //禁用F5刷新
            $(document).ready(function () {
                $(document).bind("keydown", function (e) {
                    e = window.event || e;
                    if (e.keyCode === 116) {
                        e.keyCode = 0;
                        return false;
                    }
                });
            });
            //禁用鼠标右键
            $(document).ready(function () {
                $(document).bind("contextmenu", function (e) {
                    return true;
                });
            });


        </script>

    </body>

</html>
