<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Profile</title>
    <style>
        .message-success {
            color: green;
            font-weight: bold;
        }
        .message-error {
            color: red;
            font-weight: bold;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: inline-block;
            width: 120px;
        }
        input[type="text"], input[type="email"], input[type="date"] {
            width: 250px;
            padding: 5px;
        }
        input[type="submit"] {
            margin-top: 10px;
            padding: 8px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <h1>Student Profile</h1>
    
    <c:if test="${not empty messSuccess}">
        <p class="message-success">${messSuccess}</p>
    </c:if>
    <c:if test="${not empty messFail}">
        <p class="message-error">${messFail}</p>
    </c:if>
    
    <c:choose>
        <c:when test="${not empty studentProfile}">
            <form method="post" action="students">
                <input type="hidden" name="userId" value="${studentProfile.userId}">
                
                <div class="form-group">
                    <label>Name:</label>
                    <input type="text" name="name" value="${studentProfile.name}" required>
                </div>
                
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value="${studentProfile.email}" required>
                </div>
                
                <div class="form-group">
                    <label>Student Code:</label>
                    <input type="text" name="studentCode" value="${studentProfile.studentCode}" required>
                </div>
                
                <div class="form-group">
                    <label>Date of Birth:</label>
                    <fmt:formatDate value="${studentProfile.dateOfBirth}" pattern="yyyy-MM-dd" var="formattedDate" />
                    <input type="date" name="dateOfBirth" value="${formattedDate}" required>
                </div>
                
                <input type="submit" value="Update Profile">
            </form>
        </c:when>
        <c:otherwise>
            <p>No profile information available.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>