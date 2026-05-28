package com.example.moattravel3.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.moattravel3.entity.House;
import com.example.moattravel3.repository.HouseRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final HouseRepository houseRepository;
	
	@GetMapping("/")
	public String index(Model model) {
		
		List<House> newHouses = houseRepository.findTop10ByOrderByCreatedAtDesc();
		
		model.addAttribute("newHouses", newHouses);
		
		return "index";
	}

}
