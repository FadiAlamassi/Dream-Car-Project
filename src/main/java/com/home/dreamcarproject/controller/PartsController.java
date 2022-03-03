package com.home.dreamcarproject.controller;

import com.home.dreamcarproject.model.Parts;
import com.home.dreamcarproject.model.User;
import com.home.dreamcarproject.repository.PartsRepository;
import com.home.dreamcarproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/parts")
public class PartsController {
    @Autowired
    private PartsRepository partsRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping()
    public ModelAndView partsList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isLoggedIn",isLoggedIn());
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        modelAndView.addObject("parts", partsRepository.findAll());
        modelAndView.addObject("isEdit", null);
        modelAndView.addObject("part", new Parts());
        modelAndView.addObject("successMessage", null);
        modelAndView.setViewName("parts/list");
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView addParts(@Valid Parts parts) {
        ModelAndView modelAndView = new ModelAndView();
        if(getLoggedUser().getRole().equals("ADMIN")){
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            if (isLoggedIn())
                modelAndView.addObject("loggedInUser",getLoggedUser());
            System.out.println(parts.getName());
            System.out.println(parts.getDescription());
            partsRepository.save(parts);
            modelAndView.addObject("parts", partsRepository.findAll());
            modelAndView.addObject("successMessage", "Parts has been added successfully!");
            modelAndView.addObject("part", new Parts());
            modelAndView.setViewName("/parts/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }
    }

    @PostMapping(value = "/{id}" ,params="delete=true")
    public ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        if(getLoggedUser().getRole().equals("ADMIN")){
            partsRepository.deleteById(id);
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            if (isLoggedIn())
                modelAndView.addObject("loggedInUser",getLoggedUser());
            modelAndView.addObject("parts", partsRepository.findAll());
            modelAndView.addObject("successMessage", "Parts has been deleted successfully!");
            modelAndView.addObject("part", new Parts());
            modelAndView.setViewName("/parts/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }



    @GetMapping(value = "/{id}", params = "edit=true")
    public ModelAndView editForm(@PathVariable Long id, ModelAndView modelAndView) {
        if (getLoggedUser().getRole().equals("ADMIN")){
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            if (isLoggedIn())
                modelAndView.addObject("loggedInUser",getLoggedUser());
            modelAndView.addObject("parts", partsRepository.findAll());
            modelAndView.addObject("part", partsRepository.getById(id));
            modelAndView.addObject("isEdit", true);
            modelAndView.setViewName("parts/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }

    @PostMapping("/{id}")
    public ModelAndView saveparts(@PathVariable Long id, @Valid Parts parts) {

        if(getLoggedUser().getRole().equals("ADMIN")){
            partsRepository.save(parts);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            if (isLoggedIn())
                modelAndView.addObject("loggedInUser",getLoggedUser());
            modelAndView.addObject("parts", partsRepository.findAll());
            modelAndView.addObject("successMessage", "Parts has been Edited successfully!");
            modelAndView.addObject("part", new Parts());
            modelAndView.setViewName("/parts/list");

            return modelAndView;
        }else{
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }
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
