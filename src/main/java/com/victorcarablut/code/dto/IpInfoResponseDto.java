package com.victorcarablut.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IpInfoResponseDto {

	// Free Plan: https://ipinfo.io/developers/data-types#geolocation-data

	// fields from ipinfo.io
	private String ip; // 8.8.8.8
	private String hostname; // dns.google
	private String city; // Mountain View
	private String region; // California
	private String country; // US
	private String loc; // 37.4056,-122.0775
	private String org; // AS15169 Google LLC
	private String postal; // 94043
	private String timezone; // America/Los_Angeles

	// my own extra additional fields
	private String countryFullName; // United States of America
	private String countryName; // United States
	private String countryFlag; // world-countries/flags/...

	// other fields from ipinfo.io 
	// https://ipinfo.io/faq/article/129-what-is-anycast-ip-address | ex. anycast: 8.8.8.8
	// https://ipinfo.io/faq/article/79-what-is-a-bogon-ip-address | ex. bogon: 127.0.0.1
	private Boolean anycast; // true
	private Boolean bogon; // false
	
	// my own extra additional fields
	private String countryCurrencyCode; // USD
	private String countryPhoneCode; // +1
	
	// separated geo coordinates (latitude & longitude) - useful in d3 world map
	private Double lat; // 37.4056
	private Double lng; // -122.0775

}
