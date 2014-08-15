package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Garden;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/")
//Maps the request after host:port/dia/
public class RegistrationController {

    HttpSession session;

    @RequestMapping(value = "/registerMe", method = RequestMethod.POST)
    public void doLogin(ModelMap model,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Garden gn = new Garden();
        gn.setGardenName(request.getParameter("gardenName"));
        gn.setPassword(request.getParameter("password"));
        DataLayer.addNewGarden(gn);

       /* gn = DataLayer.getGardenByName(request.getParameter("gardenName"));
        session = request.getSession();
        session.setAttribute("gardenId", gn.getId());
        session.setAttribute("gardenName", gn.getGardenName());*/
        request.setAttribute("gardenName",gn.getGardenName());
        request.setAttribute("password",gn.getPassword());
        response.sendRedirect("/dia/j_spring_security_check");

    /*
        RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
        try{
            dispatcher.forward(request, response);

        }catch(Exception e){

        }*/

    }
}
