package com.example.moattravel3.servive;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.moattravel3.entity.Role;
import com.example.moattravel3.entity.User;
import com.example.moattravel3.form.SignupForm;
import com.example.moattravel3.repository.RoleRepository;
import com.example.moattravel3.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public User create(SignupForm signupForm) {
		
		User user = new User();
		
		Role role = roleRepository.findByName("ROLE_GENERAL");
		
		user.setName(signupForm.getName());
		
		user.setFurigana(signupForm.getFurigana());
		
		user.setPostalCode(signupForm.getPostalCode());
		
		user.setAddress(signupForm.getAddress());
		
		user.setPhoneNumber(signupForm.getPhoneNumber());
		
		user.setEmail(signupForm.getEmail());
		
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		
		user.setRole(role);
		
		user.setEnabled(true);
		
		return userRepository.save(user);
			
	}
	
	//メールアドレスが登録済みかどうかチェックする
	public boolean isEmailRegistered(String email) {
		
		User user = userRepository.findByEmail(email);
		
		return user!=null;
	}

	//パスワードとパスワード（確認用）の入力値が一致するかどうかチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		
		return password.equals(passwordConfirmation);
		
	}
	
	//ユーザーを有効にする
	@Transactional
	public void enableUser(User user) {
		
		user.setEnabled(true);
		
		userRepository.save(user);
		
	}
	
}
