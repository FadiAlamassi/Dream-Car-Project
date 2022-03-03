package com.home.dreamcarproject.controller;

import com.home.dreamcarproject.model.Auction;
import com.home.dreamcarproject.model.Offer;
import com.home.dreamcarproject.model.Status;
import com.home.dreamcarproject.model.User;
import com.home.dreamcarproject.repository.AuctionRepository;
import com.home.dreamcarproject.repository.OfferRepository;
import com.home.dreamcarproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private UserRepository userRepository;




    @GetMapping()
    public ModelAndView addOffer(@RequestParam("auctionid") Long auctionid){
        ModelAndView modelAndView = new ModelAndView();
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        modelAndView.addObject("auctionid",auctionid);
        modelAndView.addObject("offer",new Offer());
        modelAndView.addObject("isEdit", false);
        modelAndView.addObject("errorMessage", null);

        List<String> statuses = Arrays.asList(Status.PENDING.toString(),Status.LOST.toString(), Status.WON.toString());
        modelAndView.addObject("statuses", statuses);
        modelAndView.setViewName("/offer/add");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView addOffer(@Valid Offer offer, @RequestParam("auctionid") Long auctionid) {
        ModelAndView modelAndView = new ModelAndView();
        Auction auction = auctionRepository.getById(auctionid);
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        if(auction.getStatus().equals("ACTIVE")){
            List<Offer> offers = auction.getOffers();
            System.out.println("post offer");
            if(offers.size()>0){
                double max=offers.get(0).getPricePerParts();
                for(Offer off:offers){
                    if(off.getPricePerParts()>max){
                        max= off.getPricePerParts();
                    }
                }
                if(max >= offer.getPricePerParts()){
                    System.out.println("price less");
                    modelAndView.addObject("auctionid",auctionid);
                    modelAndView.addObject("offer",offer);
                    modelAndView.addObject("isEdit", false);
                    List<String> statuses = Arrays.asList(Status.PENDING.toString(),Status.LOST.toString(), Status.WON.toString());
                    modelAndView.addObject("statuses", statuses);
                    modelAndView.addObject("errorMessage", "You have to add price more than "+max);

                    modelAndView.setViewName("/offer/add");
                    return modelAndView;
                }
            }

            offer.setAuction(auction);
            offer.setUser(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
            System.out.println(auctionid);
            System.out.println(offer.getDescription());
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

            offerRepository.save(offer);
            return new ModelAndView("redirect:/auction/" + auctionid);
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }

    @PostMapping("/{id}")
    public ModelAndView saveOffer(@PathVariable Long id, @RequestParam("auctionid") Long auctionid, @Valid Offer offer) {
        ModelAndView modelAndView = new ModelAndView();
        Auction auction = auctionRepository.getById(auctionid);
        if (isLoggedIn())
            modelAndView.addObject("loggedInUser",getLoggedUser());
        if(auction.getStatus().equals("ACTIVE")){
            List<Offer> offers = auction.getOffers();
            double max=offers.get(0).getPricePerParts();
            for(Offer off:offers){
                if(off.getPricePerParts()>max){
                    max= off.getPricePerParts();
                }
            }
            offer.setAuction(auction);
            offer.setUser(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
            System.out.println("post id offer");
            if(max >= offer.getPricePerParts()){
                System.out.println("price less");
                System.out.println(offer.getAuction().getId());
                modelAndView.addObject("auctionid",auctionid);
                modelAndView.addObject("offer",offer);
                modelAndView.addObject("isEdit", true);
                List<String> statuses = Arrays.asList(Status.PENDING.toString(),Status.LOST.toString(), Status.WON.toString());
                modelAndView.addObject("statuses", statuses);
                modelAndView.addObject("errorMessage", "You have to add price more than "+max);

                modelAndView.setViewName("offer/add");
                return modelAndView;
            }


            offerRepository.save(offer);
            return new ModelAndView("redirect:/auction/" + auctionid);
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }


    @GetMapping(value = "/{id}", params = "edit=true")
    public ModelAndView editForm(@PathVariable Long id,@RequestParam("auctionid") Long auctionid, ModelAndView modelAndView) {
        Auction auction = auctionRepository.getById(auctionid);
        if(auction.getStatus().equals("ACTIVE")){
            if (isLoggedIn())
                modelAndView.addObject("loggedInUser",getLoggedUser());
            modelAndView.addObject("offer", offerRepository.getById(id));
            modelAndView.addObject("isEdit", true);
            List<String> statuses = Arrays.asList(Status.PENDING.toString(),Status.LOST.toString(), Status.WON.toString());
            modelAndView.addObject("statuses", statuses);

            modelAndView.setViewName("offer/add");
            return modelAndView;
        }else{
            modelAndView.setViewName("/access-denied");
            return modelAndView;
        }

    }

    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id, @RequestParam("auctionid") Long auctionid) {
        ModelAndView modelAndView = new ModelAndView();
        Auction auction = auctionRepository.getById(auctionid);
        if(auction.getStatus().equals("ACTIVE")){
            Offer offer = offerRepository.getById(id);
            System.out.println(offer.getId());
            System.out.println(id);
            offerRepository.deleteByPid(id);
            return new ModelAndView("redirect:/auction/" + auctionid);
        }else{
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
