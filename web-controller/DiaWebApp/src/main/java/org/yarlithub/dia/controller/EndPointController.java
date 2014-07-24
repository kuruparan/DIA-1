package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.DaySchedule;
import org.yarlithub.dia.repo.object.Device;
import org.yarlithub.dia.repo.object.EndPoint;
import org.yarlithub.dia.repo.object.Schedule;
import org.yarlithub.dia.sms.SmsRequestProcessor;
import org.yarlithub.dia.util.DiaCommonUtil;
import sun.print.resources.serviceui_zh_CN;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.DateFormatSymbols;

@Controller
@RequestMapping("/")
//Maps the request after host:port/dia/
public class EndPointController {
    HttpSession session;


    @RequestMapping(value = "/createEndPoint", method = RequestMethod.GET)
    public String addDevice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        Device device =DataLayer.getDeviceByName(request.getParameter("deviceName"));

        DataLayer.addEndPoint(device.getId());

        List<EndPoint> endPoints = DataLayer.getEndPointsByDeviceId(device.getId());
        model.addAttribute("endPoints", endPoints);
        model.addAttribute("device", device);
        return "deviceHome";
    }

    @RequestMapping(value = "/endPoint", method = RequestMethod.GET)
    public String goToDevice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        List<String> days=new ArrayList<String>();
        days.add("Sun Day");
        days.add("Mon Day");
        model.addAttribute("days", days);

        EndPoint endPoint=DataLayer.getEndPointById(Integer.parseInt(request.getParameter("id")));
        String scheduleString=endPoint.getSchedule();
        if (scheduleString != null) {
            char c=scheduleString.charAt(0);
            model.addAttribute("scheduleOption", c);
            scheduleString=scheduleString.substring(2);
            if(c=='0'){
                char[] daySche = scheduleString.substring(0,6).toCharArray();
                scheduleString=scheduleString.substring(8);
                String[] ss = scheduleString.split(",");
                String[] temSS;
                Schedule schedule;
                List<Schedule> schedules = new ArrayList<Schedule>();
                for (String s : ss) {
                    if (s.contains("-")) {
                        schedule = new Schedule();
                        temSS = s.split("-");
                        schedule.setFrom(temSS[0]);
                        schedule.setTo(temSS[1]);
                        schedules.add(schedule);
                    }
                }

                model.addAttribute("schedules0", schedules);
                model.addAttribute("daySche", daySche);
            }else{
                String[] eachDaySchedules= scheduleString.split(";",-1);

                List<DaySchedule> schedulesList=new ArrayList<DaySchedule>();

                int n=1;
                for(String dS:eachDaySchedules){
                    String[] ss = dS.split(",");
                    String[] temSS;
                    Schedule schedule;
                    List<Schedule> schedules = new ArrayList<Schedule>();

                    for (String s : ss) {
                        if (s.contains("-")) {
                            schedule = new Schedule();
                            temSS = s.split("-");
                            schedule.setFrom(temSS[0]);
                            schedule.setTo(temSS[1]);
                            schedules.add(schedule);
                        }
                    }

                    schedulesList.add(new DaySchedule(schedules,n++));
                }
                model.addAttribute("schedulesList", schedulesList);
            }
        }
        Device device=DataLayer.getDeviceById(endPoint.getDeviceId());
        model.addAttribute("endPoint", endPoint);
        model.addAttribute("device", device);
        model.addAttribute("temperature", DiaCommonUtil.temperatureValue(endPoint.getSensorData()));
        model.addAttribute("moisture", DiaCommonUtil.moistureValue(endPoint.getSensorData()));
        return "endPoint";
    }

    @RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
    public void updateSchedule(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String schedule = request.getParameter("schedule");

        if (schedule != null) {
            schedule = schedule.replace("b", "");

        }

        EndPoint endPoint = DataLayer.getEndPointById(Integer.parseInt(request.getParameter("id")));
        endPoint.setSchedule(schedule);
        DataLayer.updateEndPoint(endPoint);
       // SmsRequestProcessor.sendWebDeviceCommand(device.getDeviceMask(), "shd "+String.valueOf(DiaCommonUtil.getCurrentDay())+";"+schedule);

        Device device=DataLayer.getDeviceById(endPoint.getDeviceId());
        model.addAttribute("endPoint", endPoint);
        model.addAttribute("device", device);
        response.sendRedirect("/dia/endPoint?id=" + endPoint.getId());

    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    public String changeStatus(HttpServletRequest request) {
        String s1 = request.getParameter("id");
        EndPoint endPoint = DataLayer.getEndPointById(Integer.parseInt(s1));
        if ("ON".equals(request.getParameter("status"))) {
            endPoint.setCurrentStatus(1);
           // SmsRequestProcessor.sendWebDeviceCommand(device.getDeviceMask(), "on");
        } else {
            endPoint.setCurrentStatus(0);
            //SmsRequestProcessor.sendWebDeviceCommand(device.getDeviceMask(), "off");
        }
        DataLayer.updateEndPoint(endPoint);
        return "deviceHome";
    }

    @RequestMapping(value = "/changeMode", method = RequestMethod.GET)
    public String changeMode(HttpServletRequest request) {
        EndPoint endPoint = DataLayer.getEndPointById(Integer.parseInt(request.getParameter("id")));
        endPoint.setOperationMode(Integer.parseInt(request.getParameter("operationMode")));
        DataLayer.updateEndPoint(endPoint);
        return "deviceHome";
    }

    @RequestMapping(value = "/changeOperationType", method = RequestMethod.GET)
    public String changeOperationType(HttpServletRequest request) {
        EndPoint endPoint = DataLayer.getEndPointById(Integer.parseInt(request.getParameter("id")));
        endPoint.setOperationType(Integer.parseInt(request.getParameter("operationType")));
        DataLayer.updateEndPoint(endPoint);
        return "deviceHome";
    }

}