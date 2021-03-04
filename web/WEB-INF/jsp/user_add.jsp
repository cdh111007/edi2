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
        <title>EDI - 新增客户</title>
        <script>
            function  checkinput() {
                var code = $.trim($("#code").val());
                var name = $.trim($("#name").val());
                var ediType = $.trim($("#ediType").val());
                if (code === '') {
                    alert("客户代码不能为空");
                    return false;
                }
                if (name === '') {
                    alert("客户名称不能为空");
                    return false;
                }
                if (ediType === '') {
                    alert("报文类型不能为空");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form  action="${pageContext.request.contextPath}/user/add" method="post" id="user_add_form" onsubmit="return checkinput();">
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 30%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>新增客户</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>*客户代码：</th>
                            <th><input name="code"  id="code" type="text" style="width: 200px;" value="${user.code}"/></th>
                        </tr>
                        <tr>
                            <th>*名称：</th>
                            <th><input name="name" id="name" type="text" style="width: 200px;" value="${user.name}"/></th>
                        </tr>
                        <tr>
                            <th>邮件地址：</th>
                            <th><input name="mail" id="mail" type="text" style="width: 200px;" value="${user.mail}"/></th>
                        </tr>
                        <tr>
                            <th>抄送地址： </th>
                            <th> <input name="ccmail"  id="ccmail" type="text" style="width: 200px;" value="${user.ccmail}"  /></th>
                        </tr>
                        <tr>
                            <th>报文格式： </th>
                            <th> <select name="ediType" id="ediType"><option value="">请选择...</option><option value="UN">联合国</option><option value="JT">交通部</option></select></th>
                        </tr>
                        <tr>
                            <th>可接收CODECO：</th>
                            <th><input name="codecoFlag" id="codecoFlag" type="checkbox"   value="1"  <c:if test="${user.codecoFlag eq '1'}">checked</c:if>/></th>
                            </tr>
                            <tr>
                                <th>可接收COARRI：</th>
                                <th><input name="coarriFlag" id="coarriFlag" type="checkbox"   value="1" <c:if test="${user.coarriFlag eq '1'}">checked</c:if> /></th>
                            </tr>
                            <tr>
                                <th>可接收COEDOR：</th>
                                <th><input name="coedorFlag" id="coedorFlag" type="checkbox"   value="1" <c:if test="${user.coedorFlag eq '1'}">checked</c:if> /></th>
                            </tr>
                            <tr>
                                <th>可接收COSECR：</th>
                                <th><input name="cosecrFlag" id="cosecrFlag" type="checkbox"   value="1" <c:if test="${user.cosecrFlag eq '1'}">checked</c:if> /></th>
                            </tr>
                            <tr>
                                <th>可接收VESDEP：</th>
                                <th><input name="vesdepFlag" id="vesdepFlag" type="checkbox"   value="1" <c:if test="${user.vesdepFlag eq '1'}">checked</c:if> /></th>
                            </tr>
                            <tr>
                                <th>实时发送：</th>
                                <th><input name="actFlag" id="coarriFlag" type="checkbox"   value="1" <c:if test="${user.actFlag eq '1'}">checked</c:if> /></th>
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
