package com.eventbooking.paymentservice.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    // WHY: Reads stripe secret key
    // from application.properties
    @Value("${stripe.secret.key}")
    private String secretKey;

    // WHY: @PostConstruct runs after
    // Spring creates this bean
    // Sets Stripe API key globally
    // All Stripe API calls use this key
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
        System.out.println(
                "Stripe initialized successfully ✅");
    }
}
