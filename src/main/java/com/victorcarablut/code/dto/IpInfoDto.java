package com.victorcarablut.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IpInfoDto {

	// https://ipinfo.io/developers/data-types#geolocation-data

	// from ipinfo
	private String ip;
	private String hostname;
	private String city;
	private String region;
	private String country;
	private String loc;
	private String org;
	private String postal;
	private String timezone;

	// my additional fields (not from ipinfo.io)
	private String countryFullName; // ex. United Kingdom of Great Britain and Northern Ireland
	private String countryName; // ex. United Kingdom
	private String countryFlag;

	// from ipinfo
	// extra fields: anycast: 8.8.8.8 and bogon: 127.0.0.1
	// https://ipinfo.io/faq/article/129-what-is-anycast-ip-address
	// https://ipinfo.io/faq/article/79-what-is-a-bogon-ip-address
	private Boolean anycast;
	private Boolean bogon;
	
	// my additional fields (not from ipinfo.io)
	private String countryCurrencyCode;
	private String countryPhoneCode;
	
	// separated geo coordinates (latitude & longitude) - useful ex. d3 world map
	private Double lat;
	private Double lng;

}
