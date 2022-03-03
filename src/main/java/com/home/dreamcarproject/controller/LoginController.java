package com.home.dreamcarproject.controller;

import com.home.dreamcarproject.model.User;
import com.home.dreamcarproject.model.UserType;
import com.home.dreamcarproject.repository.AuctionRepository;
import com.home.dreamcarproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.*;
import javax.mail.internet.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRepository auctionRepository;


    @RequestMapping(value = "/sendemail")
    public String sendEmail() throws AddressException, MessagingException, IOException {
        return "Email sent successfully";
    }


    @GetMapping("/login")
    public ModelAndView login(){
        if(isLoggedIn()){
            return new ModelAndView("redirect:/");
        }
            return new ModelAndView("/login");
    }

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isLoggedIn",isLoggedIn());
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        modelAndView.setViewName("/index");
        return modelAndView;
    }

    @GetMapping(value = "/registration")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        if(isLoggedIn() && !getLoggedUser().getRole().equals("ADMIN")){
            return new ModelAndView("redirect:/");
        }
                User user = new User();
                modelAndView.addObject("user", user);
                modelAndView.addObject("successMessage", null);
                modelAndView.setViewName("/registration");
                return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createUser(@Valid User user) {
        ModelAndView modelAndView = new ModelAndView();
        User isExists = userRepository.findByEmail(user.getEmail());

        System.out.println("isExists "+userRepository.findByEmail(user.getEmail()));
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getRole());
        System.out.println(user.getActive());
        System.out.println(user.getPassword());
        System.out.println(user.getPhoneNumber());
        System.out.println(user.getCompany());
        if(user.getCompany().equals(""))
           user.setCompany("Dream Car");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        modelAndView.addObject("errorMessage",null);


        if (isExists(user)) {
            modelAndView.addObject("errorMessage","* Email is exists. Try another.");
        }else {
            if(isLoggedIn()){
                System.out.println("role = "+ getLoggedUser().getRole());
                System.out.println("role = "+ getLoggedUser().getRole().equals(UserType.ADMIN.toString()));

                if(getLoggedUser().getRole().equals(UserType.ADMIN.toString())){
                    user.setActive(1);
                    modelAndView.addObject("successMessage", "User has been registered successfully.");
                    userRepository.save(user);
                }else{
                    user.setActive(0);
                    modelAndView.addObject("successMessage", "User has been registered successfully. Your account will be activated soon. ");
                    userRepository.save(user);
                }
            }else{
                System.out.println("not logged in");
                user.setActive(0);
                modelAndView.addObject("successMessage", "User has been registered successfully. Your account will be activated soon. ");
                userRepository.save(user);
            }
            modelAndView.addObject("user", new User());
        }

        modelAndView.setViewName("/registration");
        return modelAndView;
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
    public boolean isExists(User user){
        if(userRepository.findByEmail(user.getEmail()) != null)
            return true;
        return false;
    }

}
