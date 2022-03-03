package com.home.dreamcarproject.controller;

import com.home.dreamcarproject.model.*;
import com.home.dreamcarproject.repository.AuctionRepository;
import com.home.dreamcarproject.repository.OfferRepository;
import com.home.dreamcarproject.repository.PartsRepository;
import com.home.dreamcarproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private PartsRepository partsRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ModelAndView auctionList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isLoggedIn",isLoggedIn());
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        Auction auction = new Auction();
        auction.setExpirationDate(new Date());
        modelAndView.addObject("auctions", auctionRepository.findAll());
        modelAndView.addObject("auction", auction);
        setDropdownValues(modelAndView);

        setDropdownValues(modelAndView);
        modelAndView.addObject("successMessage", null);
        modelAndView.addObject("isEdit", null);
        modelAndView.setViewName("auction/list");
        return modelAndView;
    }


    @PostMapping()
    public ModelAndView addAuction(@Valid Auction auction) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isLoggedIn",isLoggedIn());
        if (isLoggedIn() && getLoggedUser().getRole().equals("ADMIN")){
            modelAndView.addObject("loggedInUser",getLoggedUser());
            System.out.println(auction.getParts().getName());
            System.out.println(auction.getTargetPricePerParts());
            System.out.println(auction.getNumberOfParts());
            System.out.println(auction.getExpirationDate());
            System.out.println(auction.getCurrency());
            System.out.println(auction.getStatus());
            Parts parts = partsRepository.findByName(auction.getParts().getName());
            auction.setParts(parts);

            auctionRepository.save(auction);
            auction = new Auction();
            auction.setExpirationDate(new Date());
            modelAndView.addObject("auction", auction);
            setDropdownValues(modelAndView);
            modelAndView.addObject("auctions", auctionRepository.findAll());
            modelAndView.addObject("successMessage", "Auction has been added successfully!");
            modelAndView.setViewName("/auction/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }

    @GetMapping(value = "/{id}", params = "edit=true")
    public ModelAndView editAuction(@PathVariable Long id, ModelAndView modelAndView) {
        modelAndView.addObject("isLoggedIn",isLoggedIn());
        if (isLoggedIn() && getLoggedUser().getRole().equals("ADMIN")){
            modelAndView.addObject("loggedInUser",getLoggedUser());
            modelAndView.addObject("auctions", auctionRepository.findAll());
            Iterable<Auction> auctionss = auctionRepository.findAll();
            if (auctionss != null){
                for (Auction auct:auctionss){
                    if (auct.getOffers() == null){
                        System.out.println("null");
                    }else{
                        System.out.println(auct.getOffers().size());

                    }
                }
            }
            modelAndView.addObject("auction", auctionRepository.getById(id));
            setDropdownValues(modelAndView);
            modelAndView.addObject("isEdit", true);
            modelAndView.setViewName("auction/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }


    }


    @PostMapping("/{id}")
    public ModelAndView saveAuction(@PathVariable Long id, @Valid Auction auction) {
        ModelAndView modelAndView = new ModelAndView();
        if(isLoggedIn() && getLoggedUser().getRole().equals("ADMIN")){
            Parts parts = partsRepository.findByName(auction.getParts().getName());
            auction.setParts(parts);
            auctionRepository.save(auction);
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            modelAndView.addObject("loggedInUser",getLoggedUser());
            auction = new Auction();
            auction.setExpirationDate(new Date());
            modelAndView.addObject("auction", auction);
            setDropdownValues(modelAndView);
            modelAndView.addObject("auctions", auctionRepository.findAll());
            modelAndView.addObject("successMessage", "Auction has been Edited successfully!");
            modelAndView.setViewName("/auction/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }
    }


    @PostMapping(value ="/{id}", params = "delete=true")
    public ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("loggedInUser",getLoggedUser());
        if(isLoggedIn() && getLoggedUser().getRole().equals("ADMIN")){
            auctionRepository.deleteById(id);
            modelAndView.addObject("isLoggedIn",isLoggedIn());
            Auction auction = new Auction();
            auction.setExpirationDate(new Date());
            modelAndView.addObject("auction", auction);
            setDropdownValues(modelAndView);
            modelAndView.addObject("auctions", auctionRepository.findAll());
            modelAndView.addObject("successMessage", "Auction has been deleted successfully!");
            modelAndView.setViewName("/auction/list");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }

    @GetMapping("{id}")
    public ModelAndView details(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isLoggedIn",isLoggedIn());
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        Auction auction = auctionRepository.getById(id);

        modelAndView.addObject("auction", auction);
        modelAndView.addObject("auctionid", id);

        modelAndView.addObject("offer", new Offer());
        List<String> statuses = Arrays.asList(Status.PENDING.toString(), Status.LOST.toString(), Status.WON.toString());

        modelAndView.addObject("statuses", statuses);
        modelAndView.addObject("offers", offerRepository.findByAuction(auction));
        modelAndView.setViewName("/auction/details");
        return modelAndView;
    }

    private void setDropdownValues(ModelAndView model){
        List<String> statuses = Arrays.asList(Status.ACTIVE.toString(), Status.CLOSED.toString());
        model.addObject("statuses", statuses);
        List<String> currencies = Arrays.asList(Currency.RON.toString(), Currency.EUR.toString(), Currency.USD.toString());
        model.addObject("currencies", currencies);
        Iterable<Parts> parts = partsRepository.findAll();
        model.addObject("parts", parts);
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
