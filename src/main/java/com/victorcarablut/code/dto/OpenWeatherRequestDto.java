package com.victorcarablut.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OpenWeatherRequestDto {
	
	// Free Plan: https://openweathermap.org/current
	
	// fields from openweathermap.org
	private Double lat; // required: 37.4056
	private Double lon; // required: -122.0775
	private String units; // optional: metric / imperial / standard (default) 
	private String lang; // optional: en, it, ...
	// private String mode; // optional: xml / html / json (default)

}
