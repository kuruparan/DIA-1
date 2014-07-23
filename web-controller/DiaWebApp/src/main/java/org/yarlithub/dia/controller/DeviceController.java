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
public class DeviceController {
    HttpSession session;

    @RequestMapping(value = "/addDevice", method = RequestMethod.GET)
    public String goTo(HttpServletRequest request) {
        session = request.getSession();
        if(session.getAttribute("gardenId")!=null){
            return "addDevice";
        }
        return "login";
    }

    @RequestMapping(value = "/addDeviceToGarden", method = RequestMethod.POST)
    public String addDevice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        Device device = DataLayer.getDeviceByName(request.getParameter("deviceName"));
        if (device!=null&&device.getPin().equals(request.getParameter("pin"))) {
            session = request.getSession();
            device.setGardenId((Integer) session.getAttribute("gardenId"));
            DataLayer.updateDevice(device);

            List<Device> devices = DataLayer.getDevicesByGardenId(device.getGardenId(),true);
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
//        if (device.getSchedule() != null) {
//            String[] ss = device.getSchedule().split(";");
//            String[] temSS;
//            Schedule schedule;
//            List<Schedule> schedules = new ArrayList<Schedule>();
//            for (String s : ss) {
//                if (s.contains("-")) {
//                    schedule = new Schedule();
//                    temSS = s.split("-");
//                    schedule.setFrom(temSS[0]);
//                    schedule.setTo(temSS[1]);
//                    schedules.add(schedule);
//                }
//            }
//            char[] daySche = ss[0].toCharArray();
//            model.addAttribute("schedules", schedules);
//            model.addAttribute("daySche", daySche);
//        }

//        model.addAttribute("temperature", DiaCommonUtil.temperatureValue(device.getSensorData()));
//        model.addAttribute("moisture", DiaCommonUtil.moistureValue(device.getSensorData()));

        model.addAttribute("device", device);
        List<EndPoint> endPoints = DataLayer.getEndPointsByDeviceId(device.getId());
        model.addAttribute("endPoints", endPoints);
        model.addAttribute("device", device);
        return "deviceHome";
    }




}