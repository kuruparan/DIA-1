package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Device;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
//Maps the request after host:port/dia/
public class GardenController {
    HttpSession session;

    @RequestMapping(value = "/gardenHome", method = RequestMethod.GET)
    public String goToGardenHome(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        session = request.getSession();
        if (session.getAttribute("gardenId") != null) {
            List<Device> devices = DataLayer.getDevicesByGardenId((Integer) session.getAttribute("gardenId"));
            model.addAttribute("devices", devices);
            return "gardenHome";
        }
        return "login";
    }

}