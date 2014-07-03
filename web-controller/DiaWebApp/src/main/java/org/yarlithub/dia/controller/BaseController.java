/**
 * Project YIT DIA
 * Created by jaykrish on 5/23/14.
 */

package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
//Maps the request after host:port/dia/
public class BaseController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model,@RequestParam(value = "error", required = false) String error,
                 @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }
        return "login";

    }

//    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
//    public String welcomeName(@PathVariable String name, ModelMap model) {
//
//        model.addAttribute("message", "DIA Web Project + Spring 3 MVC - you are now at page: " + name);
//        return "index";
//
//    }
}
