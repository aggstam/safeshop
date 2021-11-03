package com.safeshop.controllers;

import com.safeshop.configurations.CaptchaSettings;
import com.safeshop.forms.UserForm;
import com.safeshop.models.User;
import com.safeshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class ProfileController {

    private static final Logger logger = Logger.getLogger(ProfileController.class.getName());

    @Autowired
    private UserService userService;

    // This method creates the profile page view.
    @GetMapping(value="/profile")
    public ModelAndView getProfile(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            ModelAndView mav = new ModelAndView("profile");
            mav.addObject("user", request.getSession().getAttribute("user"));
            mav.addObject("userForm", new UserForm((User) request.getSession().getAttribute("user")));
            mav.addObject("siteKey", CaptchaSettings.SITE);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users to edit their profile information.
    @PostMapping(value="/profile")
    public ModelAndView postProfile(HttpServletRequest request, @Valid @ModelAttribute UserForm userForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            User sessionUser = (User) request.getSession().getAttribute("user");
            sessionUser.setName(userForm.getName());
            sessionUser.setAddress(userForm.getAddress());
            sessionUser.setPhone(userForm.getPhone());
            sessionUser = userService.save(sessionUser);
            request.getSession().setAttribute("user", sessionUser);
            return new ModelAndView("redirect:/profile");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

}
