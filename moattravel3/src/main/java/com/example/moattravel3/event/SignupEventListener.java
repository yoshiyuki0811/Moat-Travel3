package com.example.moattravel3.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.moattravel3.entity.User;
import com.example.moattravel3.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignupEventListener {

	private final VerificationTokenService verificationTokenService;

	private final JavaMailSender javaMailSender;

	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {

		User user = signupEvent.getUser();

		String token = UUID.randomUUID().toString();

		verificationTokenService.create(user, token);

		String recipientAddress = user.getEmail();

		String subject = "メール認証";

		String confirmationUrl = signupEvent.getRequestUrl() + "/verification=" + token;

		String message = "以下のリンクをクリックして会員登録を完了してください。";

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo(recipientAddress);

		mailMessage.setSubject(subject);

		mailMessage.setText(message + "\n" + confirmationUrl);

		javaMailSender.send(mailMessage);

	}

}
