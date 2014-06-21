package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Garden;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/")
//Maps the request after host:port/dia/
public class LoginController {

    HttpSession session;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void doLogin(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Garden gn = new Garden();
        gn = DataLayer.getGardenByName(request.getParameter("gardenName"));
        if (gn.getPassword().equals(request.getParameter("password"))) {
            session = request.getSession();
            session.setAttribute("gardenId", gn.getId());
            session.setAttribute("gardenName", gn.getGardenName());
            response.sendRedirect("/dia/gardenHome");
        } else {
//            return "login";
        }
    }

    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public String doLogOut(HttpServletRequest request) {
        session = request.getSession();
        session.invalidate();
        return "login";
    }

}
