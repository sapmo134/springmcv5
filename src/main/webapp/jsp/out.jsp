<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Spring 5 MVC データ受け渡しサンプル</title>
</head>
<body>

<form:form modelAttribute="myForm" action="data">
  <form:input path="userName"/><br>
  <form:button>Submit</form:button><br>

  フィールドエラー:<form:errors path="userName"/><br>
  グローバルエラー：<form:errors/><br>

</form:form>

</body>
</html>