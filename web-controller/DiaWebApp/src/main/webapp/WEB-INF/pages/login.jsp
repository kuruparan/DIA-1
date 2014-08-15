<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>DIA login</title>

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
                <li class="active"><a href="#">Home</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">About DIA</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<!-- /Fixed navbar -->

<div class="container" style="padding: 5%;">
    <div class="thumbnail" style="padding: 5%;">
        <!-- Main component for a primary marketing message or call to action -->
        <div class="row">
            <div class="col-sm-12">
                <div class="col-sm-6">
                    <h2>Welcome to DIA</h2>

                    <p>The Digital Irrigation Automation platform</p>
                </div>
                <div class="col-sm-5">
                    <c:if test="${not empty error}">
                        <div class="error"><p style="color:red">${error}</p></div>
                    </c:if>
                    <c:if test="${not empty msg}">
                        <div class="msg">${msg}</div>
                    </c:if>
                    <form method="post" action="<c:url value='/j_spring_security_check' />">
                        <div class="col-sm-5" style="padding: 0">
                            <input type="text" name="gardenName" class="form-control" placeholder="Garden name">
                        </div>
                        <div class="col-sm-5" style="padding: 0;padding-left: 1%">
                            <input type="password" name="password" class="form-control" placeholder="Password">
                        </div>
                        <div class="col-sm-2">
                            <button type="submit" class="btn btn-success">
                                Sign In
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-sm-5">
                <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                        <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                    </ol>
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner">
                        <div class="item active">
                            <img src="resources/images/img1.jpg">
                        </div>
                        <div class="item">
                            <img src="resources/images/img2.jpg">
                        </div>
                        <div class="item">
                            <img src="resources/images/img3.jpg">
                        </div>
                    </div>
                    <!-- Controls -->
                    <a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left"></span>
                    </a>
                    <a class="right carousel-control" href="#carousel-example-generic" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right"></span>
                    </a>
                </div>
            </div>

            <div class="col-sm-5 col-sm-offset-1">

                <form method="post" action="registerMe" name="signUpForm">
                    <div class="row">
                        <div class="col-sm-12">
                            <input type="text" name="gardenName" class="form-control input-lg col-sm-4"
                                   placeholder="Name your garden">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12 " style="margin-top:3%">
                            <input type="password" name="password" class="form-control input-lg col-sm-4"
                                   placeholder="Give it a password">
                        </div>
                        <div class="col-sm-12 " style="margin-top:3%">
                            <input type="password" name="passwordRe" class="form-control input-lg col-sm-4"
                                   placeholder="Re-enter password">
                        </div>
                        <div class="col-sm-8 col-sm-offset-2">
                            <a href="gardenhome.html">
                                <button style="margin:5%" type="submit" onclick="return validatePass()"
                                        class="btn btn-lg btn-success col-sm-12">
                                    Create New Garden
                                </button>
                                </a>
                            </div>
                        </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- /container -->
<a href="index.html"></a>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="resources/js/jquery-1.11.0.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="resources/js/bootstrap/bootstrap.min.js"></script>
<script src="resources/js/dia.js"></script>
</body>
</html>

