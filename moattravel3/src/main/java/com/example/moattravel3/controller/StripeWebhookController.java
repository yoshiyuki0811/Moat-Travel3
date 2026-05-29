package com.example.moattravel3.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.moattravel3.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
	public class StripeWebhookController {
		
		private final StripeService stripeService;
		
		@Value("${stripe.api-key}")
		
		private String stripeApiKey;
		
		@Value("${stripe.webhook-secret}")
		
		private String wabhookSecret;
		
		
		@PostMapping("/stripe/wabhook")
		public ResponseEntity<String> wabhook(@RequestBody String payload, @RequestHeader("stripe-signature")String signature){
			
			Stripe.apiKey = stripeApiKey;
			
			Event event =null;
			
			
	try {
		
		event = Webhook.constructEvent(wabhookSecret, payload, signature);
	}catch(SignatureVerificationException e) {
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		
	}
	
	if("checkout.session.completed".equals(event.getType())) {
		
		stripeService.processSessionCompleted(event);
	}
			
		return new ResponseEntity<>("Success", HttpStatus.OK);	
			
		}
	

}
