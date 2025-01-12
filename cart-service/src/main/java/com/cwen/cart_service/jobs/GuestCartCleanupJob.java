package com.cwen.cart_service.jobs;

import com.cwen.cart_service.domain.GuestCartService;
import com.cwen.cart_service.domain.repositories.guest.GuestCartRepository;
import com.cwen.cart_service.web.controllers.CartController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class GuestCartCleanupJob {
    private final GuestCartService guestCartService;

    public GuestCartCleanupJob(GuestCartService guestCartService) {
        this.guestCartService = guestCartService;
    }

    @Scheduled(fixedRate = 3600000)
    public void removeExpiredCarts() {
        guestCartService.removeExpiredCarts();
    }
}
