package org.yarlithub.dia.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Device;
import org.yarlithub.dia.repo.object.Garden;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username

        Garden gn;
        gn = DataLayer.getGardenByName(name);
        session = request.getSession();
        session.setAttribute("gardenId", gn.getId());
        session.setAttribute("gardenName", gn.getGardenName());

        List<Device> devices = DataLayer.getDevicesByGardenId(gn.getId(),true);
        model.addAttribute("devices", devices);
        return "gardenHome";

    }

    @RequestMapping(value="/logout")
    public String logout(){
        return "logout";
    }

    @RequestMapping(value="/403")
    public String denied(ModelMap model){


        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetail.getUsername());
        }

        return "403";

     }
}

