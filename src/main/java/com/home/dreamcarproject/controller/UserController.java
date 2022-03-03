package com.home.dreamcarproject.controller;

import com.home.dreamcarproject.model.User;
import com.home.dreamcarproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
//@CrossOrigin(origins = "http://localhost:4200") //since we’re just working locally
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @GetMapping()
    public ModelAndView userPage() {
        ModelAndView modelAndView = new ModelAndView();
        if(getLoggedUser().getRole().equals("ADMIN")){
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            if (isLoggedIn())
                modelAndView.addObject("loggedInUser",getLoggedUser());
            modelAndView.addObject("users", userRepository.findAll());
            modelAndView.setViewName("user/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }

    @PostMapping(value = {"/deactivate/{id}"})
    public ModelAndView invalidateUser(@PathVariable Long id) {
        User user = userRepository.getById(id);
        user.setActive(0);
        userRepository.save(user);
        return new ModelAndView("redirect:/user");
    }
    @PostMapping(value = {"/activate/{id}"})
    public ModelAndView validateUser(@PathVariable Long id) {
        User user = userRepository.getById(id);
        user.setActive(1);
        userRepository.save(user);
        return new ModelAndView("redirect:/user");
    }


        public User getLoggedUser() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return userRepository.findByEmail(auth.getName());
        }
        public boolean isLoggedIn(){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null || auth instanceof AnonymousAuthenticationToken){
                return false;
            }
            return true;
        }
}