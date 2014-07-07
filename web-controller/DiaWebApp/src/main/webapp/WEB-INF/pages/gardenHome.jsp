<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>DIA Garden: <c:out value="${sessionScope.gardenName}"/></title>

    <!-- Bootstrap -->
    <link href="resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="resources/js/html5shiv.js"></script>
    <script src="resources/js/respond.min-1.4.2.js"></script>
    <![endif]-->
</head>

<body style="margin-top:60px">

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!-- Fixed navbar -->
<div id="dic_bubble" class="selection_bubble fontSize13 noSelect"
     style="z-index: 9999; border: 1px solid rgb(74, 174, 222); visibility: hidden;"></div>
<div class="navbar navbar-default  navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">YIT DIA</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#"><c:out value="${sessionScope.gardenName}"/> home</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Welcome to <c:out
                            value="${sessionScope.gardenName}"/> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="${contextPath}/j_spring_security_logout">Sign Out</a></li>
                        <li class="divider"></li>
                        <li><a href="#">About DIA</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<!-- /Fixed navbar -->


<div class="container">
    <div class="row">
        <div class="col-sm-2 col-sm-offset-10">
            <form action="goToAddDevice" method="get">
                <!-- <input type="hidden" name="gardenId" value="<%request.getParameter("gardenId");%>"/>-->
                <button style="margin:5%" type="submit" class="btn btn-lg btn-success col-sm-12">Add My Device
                </button>
            </form>
        </div>
    </div>
    <!-- Main component for a primary marketing message or call to action -->
    <div class="jumbotron">
        <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading">Devices</div>
            <!-- Table -->
            <table class="table" data-link="row">
                <thead>
                <tr>
                    <th>Device Name</th>
                    <th> CurrentStatus</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${devices}" var="device">
                    <tr>
                        <th><a href="${contextPath}/deviceHome?deviceName=<c:out value='${device.deviceName}'/>"><c:out
                                value="${device.deviceName}"/></a></th>
                        <th><c:out value="${device.currentStatus}"/></th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- /container -->
<a href="index.html"></a>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="resources/js/jquery-1.11.0.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="resources/js/bootstrap/bootstrap.min.js"></script>
</body>
</html>

