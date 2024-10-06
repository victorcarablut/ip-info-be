package com.victorcarablut.code.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.victorcarablut.code.exceptions.ApiUsageLimitException;
import com.victorcarablut.code.exceptions.GenericException;
import com.victorcarablut.code.exceptions.InvalidInputException;

@Service
public class OpenWeatherService {

	@Value("${token.open.weather}")
	private String TOKEN_OPEN_WEATHER;

	public JsonNode getDataFromOpenWeather(Double lat, Double lon, String units, String lang) {

		// validating inputs
		if (lat == null || lon == null || units.length() > 10 || lang.length() > 5) {
			throw new InvalidInputException();
		}

		// get data from the API URL (Free Plan)
		// Doc. https://openweathermap.org/current
		String webUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=" + units
				+ "&appid=" + TOKEN_OPEN_WEATHER;

		URL url = null;

		try {
			url = new URI(webUrl).toURL();
		} catch (URISyntaxException e) {
			throw new GenericException();
		} catch (MalformedURLException e) {
			throw new GenericException();
		}

		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			/*
			 * Free usage of API is limited to 60 calls/minute and 1,000,000 calls/month. If exceed that
			 * limit, return a 429 HTTP status code
			 */
			int connCode = conn.getResponseCode();
			if (connCode == 429) {
				throw new ApiUsageLimitException();
			}

		} catch (IOException e) {
			throw new GenericException();
		}

		BufferedReader buffReader = null;

		try {
			buffReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} catch (IOException e) {
			throw new GenericException();
		}

		String line;
		StringBuffer response = new StringBuffer();

		try {
			while ((line = buffReader.readLine()) != null) {
				response.append(line);
			}
		} catch (IOException e) {
			throw new GenericException();
		}

		ObjectMapper objMapper = new ObjectMapper();
		JsonNode fullJsonObject = null;

		try {
			// converting from String to Json
			fullJsonObject = objMapper.readValue(response.toString(), JsonNode.class);
		} catch (JsonMappingException e) {
			throw new GenericException();
		} catch (JsonProcessingException e) {
			throw new GenericException();
		}

		// return the full json response as it is from the API
		return fullJsonObject;
	}

}
