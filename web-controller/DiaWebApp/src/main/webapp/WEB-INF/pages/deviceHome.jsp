<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix= "c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>DIA Garden: <c:out value="${sessionScope.gardenName}"/>: <c:out value="${device.deviceName}"/></title>

    <!-- Bootstrap -->
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/bootstrap/bootstrap-clockpicker.css">
    <link rel="stylesheet" type="text/css" href="resources/css/toggle-switch.css">
    <link rel="stylesheet" type="text/css" href="resources/css/dia.css">


    <script type="text/javascript">
        function addSchedule() {
            var table = document.getElementById("scheduleTable");
            var row = table.insertRow(0);
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            cell2.innerHTML = "";


            var m1 = document.createElement("input");
            m1.setAttribute('type', 'hidden');
            m1.setAttribute('name', 'start');
            m1.setAttribute('value', "start:" + document.getElementById("startTime").value);
            m1.readOnly = true;           
            cell1.innerHTML = document.getElementById("startTime").value;
            cell1.appendChild(m1);

            var m2 = document.createElement("input");
            m2.setAttribute('type', 'hidden');
            m2.setAttribute('name', 'end');
        	m2.setAttribute('value', "end:"+document.getElementById("endTime").value);
        	m2.readOnly=true;  	
            cell3.innerHTML = document.getElementById("endTime").value;
            cell3.appendChild(m2);

        }

      function submitAllForms(){
        var av=document.getElementsByName("day");
        var addon="b";
            for (e = 0; e < av.length; e++)
            {
                if (av[e].checked == true)
                {
                    addon +="1";
                }
                else{
                    addon += "0";
                }
            }
        document.getElementById("deviceId").value="<%=request.getParameter("deviceName")%>";
        document.getElementById("daysId").value=addon;
        document.getElementById("timeSchedule").submit();

      }

      function load(){

      var av=document.getElementsByName("day");

      <c:forEach items="${daySche}" var="dayS" varStatus="counter">
      var avx=${dayS};
        if(avx!="0"){
            av["${counter.index}"].checked=true;
       }
      </c:forEach>

      var sts=${device.currentStatus};
       if(sts=="1"){
          document.getElementById("myonoffswitch").checked=true;
      }else{
    	  document.getElementById("myonoffswitch").checked=false;
      }

          var stss=${device.operationType};
          if(stss=="1"){
              document.getElementById("scheduleonoffswitch").value="Scheduled";
          }else{
              document.getElementById("scheduleonoffswitch").value="Manual";
          }

      
      document.getElementById("ModeSelectId").selectedIndex = ${device.operationMode};
    }

      function clickToggle(){
        var url,xhr;

        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        if(document.getElementById("myonoffswitch").checked){
            url="${contextPath}/changeStatus?deviceName=${device.deviceName}&status=ON";
        }else{
            url="${contextPath}/changeStatus?deviceName=${device.deviceName}&status=OFF";
        }
              
        xhr = new XMLHttpRequest();
        xhr.open('GET',url, true);
        xhr.send();
      }

        function clickToggleSh(){
            var url,xhr;

            <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
            var elem=document.getElementById("scheduleonoffswitch");
            if(elem.value=="Manual"){
                url="${contextPath}/changeOperationType?deviceName=${device.deviceName}&operationType=1";
                elem.value="Scheduled"
            }else{
                url="${contextPath}/changeOperationType?deviceName=${device.deviceName}&operationType=0";
                elem.value="Manual"
            }

            xhr = new XMLHttpRequest();
            xhr.open('GET',url, true);
            xhr.send();
        }
      
      function changeMode(x){
        var url;

        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        url="${contextPath}/changeMode?deviceName=${device.deviceName}&operationMode="+x;
         
        xhr = new XMLHttpRequest();
        xhr.open('GET',url, true);
        xhr.send();
      }
      
      function reloadPage(){
    	  location.reload();
      }

</script>

    <%--Google pie chart for weather meter--%>
    <script type='text/javascript' src='https://www.google.com/jsapi'></script>
    <script type='text/javascript'>
        google.load('visualization', '1', {packages: ['gauge']});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Label', 'Value'],
                ['\'C',<c:out value="${temperature}"/>]

            ]);

            var options = {
                min: 0, max: 50,
                width: 500, height: 165,
                greenFrom: 20, greenTo: 35,
                redFrom: 35, redTo: 50,
                yellowFrom: 0, yellowTo: 20,
                minorTicks: 3
            };

            var chart = new google.visualization.Gauge(document.getElementById('temp_meter'));
            chart.draw(data, options);

            var data1 = google.visualization.arrayToDataTable([
                ['Label', 'Value'],
                ['%',<c:out value="${moisture}"/>]

            ]);

            var options1 = {
                min: 0, max: 100,
                width: 500, height: 165,
                redFrom: 90, redTo: 100,
                yellowFrom: 45, yellowTo: 90,
                greenFrom: 0, greenTo: 45,
                minorTicks: 5
            };

            var chart1 = new google.visualization.Gauge(document.getElementById('moi_meter'));
            chart1.draw(data1, options1);
        }
    </script>


</head>
<body style="margin-top:60px" onload="load()">

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!-- Fixed navbar -->
<div id="dic_bubble" class="selection_bubble fontSize13 noSelect"
     style="z-index: 9999; border: 1px solid rgb(74, 174, 222); visibility: hidden;"></div>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
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
                <li><a href="${contextPath}/gardenHome"><c:out value="${sessionScope.gardenName}"/> home</a></li>
                <li class="active"><a href="#"><c:out value="${device.deviceName}"/></a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Welcome to <c:out
                            value="${sessionScope.gardenName}"/> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="${contextPath}/signOut">Sign Out</a></li>
                        <li class="divider"></li>
                        <li><a href="#">About DIA</a></li>
                    </ul>
                </li>
            </ul>
        </div>

    </div>
</div>
<!-- /Fixed navbar -->

<div class="container">



<!-- /container -->
<a href="index.html"></a>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->


    <div class="row">


        <div class="thumbnail col-sm-6">

            <div class="row">
                <div class="lead col-sm-8 col-sm-offset-3">Current Device Status</div>
                <div class="col-sm-1 btn btn-default"><span class="glyphicon glyphicon-refresh ">  </span></div>
            </div>
            <div class="onoffswitch col-sm-offset-3">
                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox"
                       id="myonoffswitch" onchange="clickToggle()">
                <label class="onoffswitch-label" for="myonoffswitch">
                    <span class="onoffswitch-inner"></span>
                    <span class="onoffswitch-switch"></span>
                </label>
            </div>
            <br/>
            <%--<button class="btn btn-default col-sm-offset-5" id="myToggleButtonON" onclick="clickToggle(1)">ON</button>--%>
            <%--<button class="btn btn-default col-sm-offset-5" id="myToggleButtonOFF"  onclick="clickToggle(0)">OFF</button>--%>


            <div class="thumbnail thumbnail-no-hover lead col-sm-6">
                Water flow Sensor
                <br/>
                <span class="glyphicon glyphicon-ok" style="margin-top: 7%"> Working</span>
            </div>


            <div class="thumbnail thumbnail-no-hover lead col-sm-6">
                Operation Mode
                <br/>
                <select class="selectpicker span2" id="ModeSelectId"
                        onchange="changeMode(this.selectedIndex)">
                    <option>Normal Mode</option>
                    <option>Alert Mode</option>
                    <option>Intelligent Mode</option>
                </select>

            </div>
            <br/>
            <br/>
        </div>

        <div class="thumbnail col-sm-5 col-sm-offset-1">

            <div class="row">
                <div class="lead col-sm-8 col-sm-offset-3">Weather Sensors</div>
                <div class="col-sm-1 btn btn-default"><span class="glyphicon glyphicon-refresh ">  </span></div>
            </div>

            <div class="col-sm-6" id='temp_meter'></div>
            <div class="col-sm-6" id='moi_meter'></div>
            <div class="lead col-sm-6" style="margin-left: 20px;">Temperature</div>
            <div class="lead col-sm-5 "> Moisture Level</div>

        </div>
    </div>


<div class="row">
    <div class="thumbnail col-sm-12">



        <div class="row">
            <div class="lead col-sm-3 col-sm-offset-3">Device Schedule</div>
            <div class="onoffswitch col-sm-2 ">
                <input id="scheduleonoffswitch" onclick="clickToggleSh()"
                       class="btn branding-background" type="button">
                </input>
            </div>


            <div class="col-sm-1 btn btn-default pull-right"><span class="glyphicon glyphicon-refresh ">  </span></div>


        </div>
        <div class="col-sm-7">

            <div class="thumbnail" style="padding-top: 0;">

                <div class="form-group ">
                    <label class="control-label col-md-1" for="startTime">Start</label>

                    <div class="col-sm-3">
                        <div class="input-group clockpicker">
                            <input type="text" id="startTime" class="form-control" value="09:30">
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-time"></span>
                        </span>
                        </div>
                    </div>
                    <label class="control-label col-md-1" for="endTime">Stop</label>

                    <div class="col-sm-3">
                        <div class="input-group clockpicker">
                            <input type="text" id="endTime" class="form-control" value="09:30">
			                <span class="input-group-addon">
				            <span class="glyphicon glyphicon-time"></span>
			            </span>
                        </div>
                    </div>

                    <div class="col-sm-2">
                        <button type="button" onclick="addSchedule()" class="btn btn-default col-sm-offset-2">Add this
                            new
                            interval
                        </button>
                    </div>
                </div>
            </div>


            <form id="timeSchedule" action="updateSchedule" method="post">
                <table class="table table-hover col-sm-7" id="scheduleTable">
                    <tbody>
                    <c:forEach items="${schedules}" var="schedule">
                        <tr>
                            <td>
                                <c:out value="${schedule.from}"/>
                                <input type="hidden" name="start"
                                       value='start:<c:out value="${schedule.from}"/>' readonly></td>
                            <td>

                            </td>
                            <td>
                                <c:out value="${schedule.to}"/>
                                <input type="hidden" name="end"
                                       value='end:<c:out value="${schedule.to}"/>' readonly>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                 	<input type="hidden" name="days" id="daysId"/>
                    <input type="hidden" name="device" id="deviceId"/>
            </form>

        </div>

        <div class="thumbnail col-sm-3 col-sm-offset-2">Schedule Active Days
            <form id="daySchedule">
                <input type="checkbox" name="day" value="mo">Monday <br/>
                <input type="checkbox" name="day" value="tu">Tuesday <br/>
                <input type="checkbox" name="day" value="tu">Wednesday<br/>
                <input type="checkbox" name="day" value="tu">Thursday<br/>
                <input type="checkbox" name="day" value="tu">Friday<br/>
                <input type="checkbox" name="day" value="tu">Saturday<br/>
                <input type="checkbox" name="day" value="tu">Sunday<br/>
            </form>
        </div>

        <button type="clear" onclick="reloadPage()" class="btn btn-lg btn-default  col-sm-offset-3">Cancel and Reset Default</button>
        <%--<button type="button" onclick="submitAllForm()" class="btn btn-lg btn-success">Update and Activate this--%>
            <%--Schedule--%>
        <%--</button>--%>













        <!-- Button trigger modal -->
        <button class="btn btn-lg btn-success" data-toggle="modal" data-target="#myModal">
            Update and Activate this Schedule
        </button>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">Schedule Update</h4>
                    </div>
                    <div class="modal-body">
                        Please wait while your schedule is being updated...
                        <br/>
                        Your scheudle is successfully updated. Do you want to send it to your device now?
                        <br/>
                        <br/>
                        <br/>
                        <button type="button" class="btn btn-default col-sm-offset-7" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-success" data-dismiss="modal" onclick="submitAllForms()">Send</button>
                    </div>




                </div>
            </div>
        </div>












    </div>
</div>
</div>


</div>



<!-- Include all compiled plugins (below), or include individual files as needed -->
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script type="text/javascript" src="resources/js/bootstrap/jquery-1.11.js"></script>
<script type="text/javascript" src="resources/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/js/bootstrap/bootstrap-clockpicker.min.js"></script>

<script type="text/javascript">
$('.clockpicker').clockpicker()
	.find('input').change(function(){
		console.log(this.value);
	});
var input = $('#single-input').clockpicker({
	placement: 'bottom',
	align: 'left',
	autoclose: true,
	'default': 'now'
});

// Manually toggle to the minutes view
$('#check-minutes').click(function(e){
	// Have to stop propagation here
	e.stopPropagation();
	input.clockpicker('show')
			.clockpicker('toggleView', 'minutes');
});
if (/mobile/i.test(navigator.userAgent)) {
	$('input').prop('readOnly', true);
}
</script>
<script type="text/javascript">
var button = $('#myToggleButton');
button.on('click', function () {
  $(this).toggleClass('active');
});
</script>
</body>
</html>
