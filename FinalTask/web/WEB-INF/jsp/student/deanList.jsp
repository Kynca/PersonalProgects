<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<tag:html title="Student list">
    <tag:menu/>
    <h2><fmt:message key="students"/></h2>
    <div class="inner">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th><fmt:message key="id"/></th>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="secondName"/></th>
            <th><fmt:message key="Patronymic"/></th>
            <th><fmt:message key="birthDate"/></th>
            <th><fmt:message key="mail"/></th>
        </tr>
        </thead>
        <c:forEach items="${students}" var="item">
        <tr>
            <th>${item.id}</th>
            <th>${item.name}</th>
            <th>${item.lastname}</th>
            <th>${item.patronymic}</th>
            <th>${item.date}</th>
            <th>${item.mail}</th>
        </tr>
        </c:forEach>
    </table>
    </div>
    <c:url value="/student/process.html" var="process"/>

    <form action="${process}" method="post">
        <input required="required" type="text" name="id"><br>
        <input type="radio" id="edit" name="action" value="false" required="required">
        <label for="edit"><fmt:message key="edit"/></label><br>
        <input type="radio" id="delete" name="action" value="true">
        <label for="delete"><fmt:message key="delete"/></label><br>
        <button class="btn" type="submit"><fmt:message key="send"/></button>
    </form>

    <c:url value="/student/create/form.html" var="create"/>
    <a href="${create}" class="btn"><fmt:message key="create"/></a>

    <tag:warning/>

</tag:html>
