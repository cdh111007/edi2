<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include  file="index.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EDI - 装卸船重发</title>
        <script>

            function send() {

                var begin = $("#begin").val();
                var end = $("#end").val();
                var cstcode = $("#cstcode").val();
                var cntrno = $("#cntrno").val();
                var typecheck = $('input[type^=radio]:checked').val();
                var vslName = $("#vslName").val();
                var voyage = $("#voyage").val();

                if (cstcode === '') {
                    alert('请选择客户代码');
                    return;
                }

                if (typecheck === '' || typecheck === undefined) {
                    alert('请选择发送方式');
                    return;
                }

                if (typecheck === 't') {
                    if (begin === '') {
                        alert('请输入起始时间');
                        return;
                    }
                    if (end === '') {
                        alert('请输入结束时间');
                        return;
                    }
                } else {
                    if (vslName === '') {
                        alert('请输入中文船名');
                        return;
                    }

                    if (voyage === '') {
                        alert('请输入航次');
                        return;
                    }
                }

                var url = '${pageContext.request.contextPath}/coarri_resend/send?cstcode='
                        + cstcode + '&begin=' + begin + '&end=' + end
                        + '&voyage=' + voyage + '&typecheck=' + typecheck + '&vslName=' + vslName + '&cntrno=' + cntrno;

                $("#tips").html("正在发送数据，请稍等...");
                    $.ajax({
                    url: url,
                    type: "post",
                    contentType: 'application/json;charset=utf-8',
                    async: true,
                    success: function (result) {
                            $("#tips").html("发送成功！");
                    },
                    error: function (result) {
                        $("#tips").html("发送失败！");
                    }
                    });
            }
        </script>
    </head>
    <body>
        <form action="#" method="post" >
            <div  class="table-wrapper">
                <table style="height: 80%;
                       width: 80%;         /*  必须设置一个宽度， margin: 0 auto才能使之居于父组件中央*/
                       margin: 10px auto;  /*  通过设置外边距（margin）中的左右外边距属性为auto使之居于父组件（body）中间*/">
                    <caption>
                        <h2>装卸船重发</h2>
                    </caption>
                    <thead>
                        <tr>
                            <th>客户代码：
                                <select name="cstcode" id="cstcode" style="width: 200px;">
                                    <option value=""></option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.code }" <c:if test="${cstcode eq user.code}">selected</c:if>>${user.code } - ${user.name}</option>
                                    </c:forEach>
                                </select>
                            </th>
                        </tr>
                        <tr>
                            <th>箱号（每个箱号用空格分隔）
                            </th>
                        </tr>
                        <tr>
                            <th><textarea name="cntrno"
                                          id="cntrno"    rows="10"  cols="105" wrap="true"
                                          value="${cntrno}"></textarea>
                            </th>
                        </tr>
                        <tr>
                            <th> 
                                <input name="typecheck"  
                                       id="typecheck1" type="radio" 
                                       value="v"  /><label for="typecheck1">按船名航次发</label>


                                <input name="typecheck"  
                                       id="typecheck2" type="radio" 
                                       value="t"   /><label for="typecheck2">按时间段发</label>
                            </th>
                        </tr>
                        <tr>
                            <th>中文船名
                                <input name="vslName"
                                       id="vslName" type="text" style="width: 200px;"
                                       value="${vslName}" />

                                航次
                                <input name="voyage"
                                       id="voyage" type="text" style="width: 200px;"
                                       value="${voyage}" />
                            </th>
                        </tr>
                        <tr>
                            <th>
                            </th>
                        </tr>
                        <tr>
                            <th>开始时间（格式：20180101010000）
                                <input name="begin"
                                       id="begin" type="text" style="width: 200px;"
                                       value="${begin}" onclick="WdatePicker({skin: 'blue', dateFmt: 'yyyyMMddHHmmss', maxDate: '%y-%M-{%d+1}'});"/>
                                结束时间
                                <input name="end"
                                       id="end" type="text" style="width: 200px;"
                                       value="${end}" onclick="WdatePicker({skin: 'blue', dateFmt: 'yyyyMMddHHmmss', maxDate: '%y-%M-{%d+1}'});"/>
                            </th>
                        </tr>
                        <tr>
                            <th >
                                <button type="button" id="btn" onclick="send();">发送</button>
                            </th>
                        </tr>
                        <tr>
                            <th>
                                <div id="tips"></div>
                            </th>
                        </tr>
                    </thead>
                </table>
            </div>
        </form>


    </body>
</html>
