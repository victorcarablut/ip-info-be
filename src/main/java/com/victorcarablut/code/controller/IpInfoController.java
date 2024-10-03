package com.victorcarablut.code.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victorcarablut.code.dto.IpInfoDto;
import com.victorcarablut.code.exceptions.ApiUsageLimitException;
import com.victorcarablut.code.exceptions.GenericException;
import com.victorcarablut.code.exceptions.InvalidInputException;
import com.victorcarablut.code.service.IpInfoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "${url.fe.cross.origin}")
@RestController
@RequestMapping("/api/ip")
@RequiredArgsConstructor
public class IpInfoController {

	private final IpInfoService ipInfoService;
	
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
		final String message = "Free usage of API is limited to 50,000 requests per month.";
		return new ResponseEntity<String>(message, HttpStatus.TOO_MANY_REQUESTS);
	}
	
	@PostMapping("/info")
	public IpInfoDto getIpInfo(@RequestBody IpInfoDto ipInfo) {
		return ipInfoService.getDataFromIpInfo(ipInfo.getIp());
	}
}
