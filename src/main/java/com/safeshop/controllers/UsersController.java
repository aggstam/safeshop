package com.safeshop.controllers;

import com.safeshop.configurations.CaptchaSettings;
import com.safeshop.forms.DeleteUserForm;
import com.safeshop.forms.UserForm;
import com.safeshop.models.User;
import com.safeshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class UsersController {

    private static final Logger logger = Logger.getLogger(UsersController.class.getName());

    @Autowired
    private UserService userService;

    // This method creates the users page view, accessible by users with the appropriate right.
    @GetMapping(value="/users")
    public ModelAndView getUsers(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            List<User> users = userService.findAll();
            ModelAndView mav = new ModelAndView("users");
            mav.addObject("user", request.getSession().getAttribute("user"));
            mav.addObject("users", users);
            mav.addObject("userForm", new UserForm());
            mav.addObject("deleteUserForm", new DeleteUserForm());
            mav.addObject("siteKey", CaptchaSettings.SITE);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to create a new user.
    @PostMapping(value="/users")
    public ModelAndView addUser(HttpServletRequest request, @Valid @ModelAttribute UserForm userForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // User creation.
            setUserInformation(new User(), userForm);
            return new ModelAndView("redirect:/users");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to edit the information of a user.
    @PostMapping(value="/users/{id}")
    public ModelAndView editUser(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute UserForm userForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // User existence check.
            User user = userService.findUserById(id);
            if (user == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // Edit execution.
            setUserInformation(user, userForm);
            if (user.getId().equals(((User) request.getSession().getAttribute("user")).getId())) {
                request.getSession().setAttribute("user", user);
            }
            return new ModelAndView("redirect:/users");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to delete a user.
    @PostMapping(value="/users/delete/{id}")
    public ModelAndView deleteUser(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute DeleteUserForm deleteUserForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // User existence check.
            User user = userService.findUserById(id);
            if (user == null) {
                return new ModelAndView("redirect:/notFound");
            }
            userService.delete(user);
            if (((User) request.getSession().getAttribute("user")).getId().equals(user.getId())) {
                return new ModelAndView("redirect:/");
            }
            return new ModelAndView("redirect:/users");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    private void setUserInformation(User user, UserForm userForm) {
        user.setEmail(userForm.getEmail());
        user.setName(userForm.getName());
        user.setAddress(userForm.getAddress());
        user.setPhone(userForm.getPhone());
        user.setSeller((userForm.getSeller() != null) ? userForm.getSeller() : false);
        user.setAdmin((userForm.getAdmin() != null) ? userForm.getAdmin() : false);
        userService.save(user);
    }

}
