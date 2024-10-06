package com.victorcarablut.code.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.victorcarablut.code.dto.OpenWeatherRequestDto;
import com.victorcarablut.code.exceptions.ApiUsageLimitException;
import com.victorcarablut.code.exceptions.GenericException;
import com.victorcarablut.code.exceptions.InvalidInputException;
import com.victorcarablut.code.service.OpenWeatherService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "${url.fe.cross.origin}")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class OpenWeatherController {

	private final OpenWeatherService openWeatherService;

	// Custom exceptions response
	@ExceptionHandler({ GenericException.class })
	public ResponseEntity<String> handleGenericException() {
		final String message = "Error";
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ InvalidInputException.class })
	public ResponseEntity<String> handleInvalidInputException() {
		final String message = "Invalid Input";
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ ApiUsageLimitException.class })
	public ResponseEntity<String> handleApiUsageLimitException() {
		final String message = "Free usage of API is limited to 60 calls/minute and 1,000,000 calls/month.";
		return new ResponseEntity<String>(message, HttpStatus.TOO_MANY_REQUESTS);
	}

	@PostMapping("/info")
	public JsonNode getWeatherInfo(@RequestBody OpenWeatherRequestDto openWeather) {
		return openWeatherService.getDataFromOpenWeather(openWeather.getLat(), openWeather.getLon(),
				openWeather.getUnits(), openWeather.getLang());
	}

}
