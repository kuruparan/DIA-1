package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
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
import java.util.List;

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

        EndPoint endPoint=DataLayer.getEndPointById(Integer.parseInt(request.getParameter("id")));
        if (endPoint.getSchedule() != null) {
            String[] ss = endPoint.getSchedule().split(";");
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
            char[] daySche = ss[0].toCharArray();
            model.addAttribute("schedules", schedules);
            model.addAttribute("daySche", daySche);
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
        /*if (ss2 != null & ss2 != null) {
            for (int n = 0; n < ss2.length; n++) {
                ss2[n] = ss2[n].replace("start:", "");
                ss3[n] = ss3[n].replace("end:", "");
                schedule += ";" + ss2[n] + "-" + ss3[n];
            }
        }*/
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