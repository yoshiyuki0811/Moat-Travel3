package com.example.moattravel3.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.moattravel3.entity.House;
import com.example.moattravel3.entity.Reservation;
import com.example.moattravel3.entity.User;
import com.example.moattravel3.repository.HouseRepository;
import com.example.moattravel3.repository.ReservationRepository;
import com.example.moattravel3.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;

	private final HouseRepository houseRepository;

	private final UserRepository userRepository;

	@Transactional
	public void create(Map<String, String>paymentIntentObect) {

		Reservation reservation = new Reservation();
		
		Integer houseId = Integer.valueOf(paymentIntentObect.get("houseId"));
		
		Integer userId = Integer.valueOf(paymentIntentObect.get("userId"));

		House house = houseRepository.getReferenceById(houseId);

		User user = userRepository.getReferenceById(userId);
		
		LocalDate checkinDate = LocalDate.parse(paymentIntentObect.get("checkinDate"));

		LocalDate checkoutDate = LocalDate.parse(paymentIntentObect.get("checkoutDate"));

		reservation.setHouse(house);

		reservation.setUser(user);

		reservation.setCheckinDate(checkinDate);

		reservation.setCheckoutDate(checkoutDate);

		Integer numberOfPeople =Integer.valueOf(paymentIntentObect.get("numberOfPeople"));

		Integer amount = Integer.valueOf(paymentIntentObect.get("amount"));
		
		reservationRepository.save(reservation);
	}

	//宿泊人数が定員以下かどうかチェックする
	public boolean isWithnCapacity(Integer numberOfPeople, Integer capacity) {

		return numberOfPeople <= capacity;
	}

	//宿泊料金を計算する
	public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {

		long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

		int amount = price * (int) numberOfNights;

		return amount;
	}
}
