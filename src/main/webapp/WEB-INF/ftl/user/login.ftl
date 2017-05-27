<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login</title>
</head>
<#assign ctx = springMacroRequestContext.contextPath/>
<body>
<#if Session.name?exists>
${Session.name}
<#else>
<form method="post" action="${ctx}/user/login">
    <input name="name" type="text">
    <input name="password" type="password">
    <input type="submit" value="提交">
</form>
</#if>
<p>192.168.99.100:8080</p>
</body>
</html>