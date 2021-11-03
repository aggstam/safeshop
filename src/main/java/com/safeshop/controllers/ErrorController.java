package com.safeshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class ErrorController {

    private static final Logger logger = Logger.getLogger(ErrorController.class.getName());

    // This method creates the error page view.
    @RequestMapping(value="/error", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView getError(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            ModelAndView mav = new ModelAndView("error");
            Integer errorStatusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            if (errorStatusCode != null) {
                if (errorStatusCode.equals(404)) {
                    mav = new ModelAndView("notFound");
                } else if (errorStatusCode.equals(403)) {
                    mav = new ModelAndView("forbidden");
                }
            }
            mav.addObject("user", request.getSession().getAttribute("user"));
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("/error");
        }
    }

    // This method creates the notFound page view.
    @RequestMapping(value="/notFound", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView getNotFound(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            ModelAndView mav = new ModelAndView("notFound");
            mav.addObject("user", request.getSession().getAttribute("user"));
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method creates the forbidden page view.
    @RequestMapping(value="/forbidden", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView getForbidden(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            ModelAndView mav = new ModelAndView("forbidden");
            mav.addObject("user", request.getSession().getAttribute("user"));
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

}
