package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Device;
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
public class DeviceController {
    HttpSession session;

    @RequestMapping(value = "/goToAddDevice", method = RequestMethod.GET)
    public String goTo(HttpServletRequest request) {
        session = request.getSession();
        if(session.getAttribute("gardenId")!=null){
            return "addDevice";
        }
        return "login";
    }

    @RequestMapping(value = "/addDevice", method = RequestMethod.POST)
    public String addDevice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        Device device = new Device();
        device = DataLayer.getDeviceByName(request.getParameter("deviceName"));
        if (device.getPin().equals(request.getParameter("pin"))) {
            session = request.getSession();
            device.setGardenId((Integer) session.getAttribute("gardenId"));
            DataLayer.updateDevice(device);

            List<Device> devices = DataLayer.getDevicesByGardenId(device.getGardenId());
            model.addAttribute("devices", devices);
            return "gardenHome";
        } else {
            return "addDevice";
        }


    }

    @RequestMapping(value = "/deviceHome", method = RequestMethod.GET)
    public String goToDevice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        Device device;
        device = DataLayer.getDeviceByName(request.getParameter("deviceName"));
        if (device.getSchedule() != null) {
            String[] ss = device.getSchedule().split(";");
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
        model.addAttribute("device", device);
        model.addAttribute("temperature", DiaCommonUtil.temperatureValue(device.getSensorData()));
        model.addAttribute("moisture", DiaCommonUtil.moistureValue(device.getSensorData()));
        return "deviceHome";
    }

    @RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
    public void updateSchedule(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ss1 = request.getParameter("days");
        String[] ss2 = request.getParameterValues("start");
        String[] ss3 = request.getParameterValues("end");
        String schedule = null;

        if (ss1 != null) {
            ss1 = ss1.replace("b", "");
            schedule = ss1;
        }
        if (ss2 != null & ss2 != null) {
            for (int n = 0; n < ss2.length; n++) {
                ss2[n] = ss2[n].replace("start:", "");
                ss3[n] = ss3[n].replace("end:", "");
                schedule += ";" + ss2[n] + "-" + ss3[n];
            }
        }
        Device device = DataLayer.getDeviceByName(request.getParameter("device"));
        device.setSchedule(schedule);
        DataLayer.updateDevice(device);
        SmsRequestProcessor.sendWebDeviceCommand(device.getDeviceMask(), "shd "+String.valueOf(DiaCommonUtil.getCurrentDay())+";"+schedule);

        request.setAttribute("deviceName", device.getDeviceName());
        response.sendRedirect("/dia/deviceHome?deviceName=" + device.getDeviceName());

    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    public String changeStatus(HttpServletRequest request) {
        String s1 = request.getParameter("deviceName");
        Device device = DataLayer.getDeviceByName(s1);
        if ("ON".equals(request.getParameter("status"))) {
            device.setCurrentStatus(1);
            SmsRequestProcessor.sendWebDeviceCommand(device.getDeviceMask(), "on");
        } else {
            device.setCurrentStatus(0);
            SmsRequestProcessor.sendWebDeviceCommand(device.getDeviceMask(), "off");
        }
        DataLayer.updateDevice(device);
        return "gardenHome";
    }

    @RequestMapping(value = "/changeMode", method = RequestMethod.GET)
    public String changeMode(HttpServletRequest request) {
        Device device = DataLayer.getDeviceByName(request.getParameter("deviceName"));
        device.setOperationMode(Integer.parseInt(request.getParameter("operationMode")));
        DataLayer.updateDevice(device);
        return "gardenHome";
    }

    @RequestMapping(value = "/changeOperationType", method = RequestMethod.GET)
    public String changeOperationType(HttpServletRequest request) {
        Device device = DataLayer.getDeviceByName(request.getParameter("deviceName"));
        device.setOperationType(Integer.parseInt(request.getParameter("operationType")));
        DataLayer.updateDevice(device);
        return "gardenHome";
    }

}