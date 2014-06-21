/**
 * Project YIT DIA
 * Created by jaykrish on 5/23/14.
 */

package org.yarlithub.dia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
//Maps the request after host:port/dia/
public class BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        //Spring uses InternalResourceViewResolver and return back index.jsp
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
