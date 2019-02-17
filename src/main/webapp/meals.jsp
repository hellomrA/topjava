<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
    <c:forEach var="meal" items="${meals}" >
        <tr style="color: <c:if test="${meal.excess}">red</c:if><c:if test="${!meal.excess}">green</c:if>">
            <td>
                <td>${f:formatLocalDateTime(meal.dateTime, 'hh-mm-ss dd.MM.yyyy')}</td>
            </td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
