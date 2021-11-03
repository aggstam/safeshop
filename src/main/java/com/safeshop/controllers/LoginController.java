package com.safeshop.controllers;

import com.safeshop.models.User;
import com.safeshop.services.UserService;
import com.safeshop.xss.XssSanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private UserService userService;

    // This method creates the login page view.
    @GetMapping("/")
    public ModelAndView getLoginPage(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            request.getSession().removeAttribute("user");
            return newLoginModelAndView(null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This callback method enables users to login to the site using their Google account.
    // If the user enters the site for the first time, a new User object is created.
    @GetMapping("/loginSuccess")
    public ModelAndView getLoginInfo(HttpServletRequest request, OAuth2AuthenticationToken authentication) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
            String userInfoEndpointUri = client.getClientRegistration()
                                               .getProviderDetails()
                                               .getUserInfoEndpoint()
                                               .getUri();
            if (!StringUtils.isEmpty(userInfoEndpointUri)) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
                HttpEntity<String> entity = new HttpEntity<String>("", headers);
                ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
                Map userAttributes = response.getBody();
                String email = XssSanitizerUtil.stripXSS(userAttributes.get("email").toString());
                User user = userService.findUserByEmail(email);
                if (user == null) {
                    // User creation
                    user = new User();
                    user.setEmail(email);
                    user.setName((userAttributes.get("name") != null) ? XssSanitizerUtil.stripXSS(userAttributes.get("name").toString()) : email.split("@")[0]);
                    user.setSeller(false);
                    user.setAdmin(false);
                    user = userService.save(user);
                }
                request.getSession().setAttribute("user", user);
                logger.info(String.format("[JSESSIONID - %s] [User %d successfully logged in.]", request.getSession().getId(), user.getId()));
                return new ModelAndView("redirect:/home");
            }
            return newLoginModelAndView("There was an error during login process. Please try again!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return newLoginModelAndView("There was an error during login process. Please try again!");
        }
    }

    private ModelAndView newLoginModelAndView(String errorMessage) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        if (clientRegistrations != null) {
            clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), "oauth2/authorize-client" + "/" + registration.getRegistrationId()));
        }
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("providers", oauth2AuthenticationUrls);
        mav.addObject("errorMessage", errorMessage);
        return mav;
    }

}
