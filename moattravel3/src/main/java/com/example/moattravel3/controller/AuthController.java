package com.example.moattravel3.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.moattravel3.entity.User;
import com.example.moattravel3.entity.VerificationToken;
import com.example.moattravel3.event.SignupEventPublisher;
import com.example.moattravel3.form.SignupForm;
import com.example.moattravel3.service.UserService;
import com.example.moattravel3.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final UserService userService;
	
	private final SignupEventPublisher signupEventPublisher;
	
	private final VerificationTokenService verificationTokenService;
	
	@GetMapping("/login")
	public String login() {
		
		return "auth/login";
		
	}
	@GetMapping("/signup")
	public String signup(Model model) {
		
		model.addAttribute("signupForm", new SignupForm());
		
		return "auth/signup";
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		//メールアドレス「が登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
		if(userService.isEmailRegistered(signupForm.getEmail())) {
			
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "既に登録済みのメールアドレスです。");
			
			bindingResult.addError(fieldError);
			
		}
		//パスワードとパスワード（確認用）の入力値が一致しなければBindingResultオブジェクトにエラー内容を追加する
		if(!userService.isSamePassword(signupForm.getPassword(),signupForm.getPasswordConfirmation())) {
			
			FieldError fieldError =new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
			
			bindingResult.addError(fieldError);
			
		}
		if(bindingResult.hasErrors()) {
			
			return "auth/signup";
		}
		
		userService.create(signupForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。");
		
		User createUser = userService.create(signupForm);
		
		String requestUrl = new String(httpServletRequest.getRequestURL());
		
		signupEventPublisher.publishSignupEvent(createUser, requestUrl);
		
		redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");
		
		return "redirect:/";
	}
	
	@GetMapping("/signup/verify")
	public String verify(@RequestParam(name = "token")String token, Model model) {
		
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
		
		if(verificationToken !=null) {
			
			User user = verificationToken.getUser();
			
			userService.enableUser(user);
			
			String succesMessage ="会員登録を完了しました。";
			
			model.addAttribute("succesMessage", succesMessage);
					
		}else {
			
			String errorMessage ="トークンが無効です";
			
			model.addAttribute("errorMessage", errorMessage);
			
		}
		
		return "auth/verify";
	}

}
	