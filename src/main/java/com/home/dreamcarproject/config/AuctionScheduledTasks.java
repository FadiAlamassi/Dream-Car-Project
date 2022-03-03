package com.home.dreamcarproject.config;

import com.home.dreamcarproject.model.Auction;
import com.home.dreamcarproject.model.Offer;
import com.home.dreamcarproject.model.Status;
import com.home.dreamcarproject.repository.AuctionRepository;
import com.home.dreamcarproject.repository.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Component
public class AuctionScheduledTasks {
    @Autowired
    AuctionRepository auctionRepository;
    @Autowired
    OfferRepository offerRepository;

    private static final Logger log = LoggerFactory.getLogger(AuctionScheduledTasks.class);

    @Scheduled(cron ="*/10 * * * * *")
//    @Scheduled(cron = "0 */2 * * * *")
    public void checkAuction() {
        log.info("Check auction");
        Iterable<Auction> auctions = auctionRepository.findAll();
        if (auctions != null) {
            Date date = new Date();
            for (Auction auction : auctions) {
                if(auction.getStatus().equals(Status.ACTIVE.toString())){
                    Offer winningOffer = null;
                    List<Offer> offers = auction.getOffers();

                    //=================================================
                    if (date.after(auction.getExpirationDate())) {
                        log.info("found expired auction");

                        if (offers.size() > 0) {
                            if (offers.size() == 1) {
                                log.info("one offer");
                                winningOffer = offers.get(0);
                            } else {
                                final Comparator<Offer> comparator = Comparator.comparingDouble(Offer::getPricePerParts);
                                winningOffer = offers.stream().max(comparator).get();
                            }

                            try {
                                EmailServiceImpl.sendemail(auction.getId(), auction.getParts(),winningOffer.getUser().getEmail());
                            } catch (AddressException e) {
                                e.printStackTrace();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            log.info("no offers");
                        }
                    }else{
                        log.info("No expired auction");
                        if (offers.size() > 0) {
                            for (Offer offer:offers){
                                if(offer.getPricePerParts() >= auction.getTargetPricePerParts()){
                                    log.info("winOffer "+offer.getDescription());
                                    winningOffer=offer;
                                }
                            }
                        }else {
                            log.info("no offers");
                        }

                }
                    if(winningOffer != null){
                        for (Offer offer : offers) {
                            offer.setStatus(Status.LOST.toString());
                            offerRepository.save(offer);
                        }
                        winningOffer.setStatus(Status.WON.toString());
                        offerRepository.save(winningOffer);
                        auction.setStatus(Status.CLOSED.toString());
                        auctionRepository.save(auction);

                        try {
                            EmailServiceImpl.sendemail(auction.getId(), auction.getParts(),winningOffer.getUser().getEmail());
                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }

}
