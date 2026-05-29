package com.example.moattravel3.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.moattravel3.entity.User;
import com.example.moattravel3.entity.VerificationToken;
import com.example.moattravel3.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	@Transactional
	public void create(User user, String token) {
		
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);
		
		verificationToken.setToken(token);
		
		verificationTokenRepository.save(verificationToken);
	}
	
	//トークンの文字列で検索した結果を返す
	public VerificationToken getVerificationToken(String token) {
		
		return verificationTokenRepository.findByToken(token);
		
		
	}

}
