package com.victorcarablut.code.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorcarablut.code.dto.IpInfoDto;
import com.victorcarablut.code.exceptions.ApiUsageLimitException;
import com.victorcarablut.code.exceptions.GenericException;
import com.victorcarablut.code.exceptions.InvalidInputException;

@Service
public class IpInfoService {

	@Value("${token.ip.info}")
	private String TOKEN_IP_INFO;

	public IpInfoDto getDataFromIpInfo(String ip) {

		// validating IP input
		if (ip == null || ip.contains(" ") || ip.length() == 0 || ip.length() > 50 || ip.isEmpty() || ip.isBlank()) {
			throw new InvalidInputException();
		}

		// get data from the URL
		// // https://ipinfo.io/developers/data-types#geolocation-data
		String webUrl = "https://ipinfo.io/" + ip + "/json?token=" + TOKEN_IP_INFO;
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
			conn.setRequestMethod("GET");

			/*
			 * Free usage of API is limited to 50,000 API requests per month. If exceed that
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
		IpInfoDto ipInfo = new IpInfoDto();

		// World countries (full names, flags)
		// downloaded from: https://stefangabos.github.io/world_countries

		// unix / , windows \
		String separator = File.separator;

		String file_countries_fullname = "world-countries" + separator + "countries" + separator + "en" + separator
				+ "world.json";
		String file_countries_flag = "world-countries" + separator + "flags" + separator + "64x64" + separator
				+ "flags-64x64.json";

		// downloaded from: https://country.io/data
		String file_countries_name = "world-countries" + separator + "country-name" + separator + "names.json";
		String file_countries_currency_code = "world-countries" + separator + "currency-code" + separator
				+ "currency.json";
		String file_countries_phone_code = "world-countries" + separator + "phone-code" + separator + "phone.json";

		try {
			ipInfo = objMapper.readValue(response.toString(), IpInfoDto.class);

			if (ipInfo.getCountry() != null) {

				JsonNode jsonNodeCountryFullName = objMapper.readTree(new File(file_countries_fullname));
				JsonNode jsonNodeCountryName = objMapper.readTree(new File(file_countries_name));
				JsonNode jsonNodeCountryFlag = objMapper.readTree(new File(file_countries_flag));
				JsonNode jsonNodeCountryCurrencyCode = objMapper.readTree(new File(file_countries_currency_code));
				JsonNode jsonNodeCountryPhoneCode = objMapper.readTree(new File(file_countries_phone_code));

				// get country full name
				if (jsonNodeCountryFullName.isArray()) {
					for (JsonNode objNode : jsonNodeCountryFullName) {
						if (objNode.get("alpha2").asText().equals(ipInfo.getCountry().toLowerCase())) {
							ipInfo.setCountryFullName(objNode.get("name").asText());
						}
					}
				}

				// set country name
				ipInfo.setCountryName(jsonNodeCountryName.get(ipInfo.getCountry()).asText());

				// set country flag
				ipInfo.setCountryFlag(jsonNodeCountryFlag.get(ipInfo.getCountry().toLowerCase()).asText());

				// set country currency code
				ipInfo.setCountryCurrencyCode(jsonNodeCountryCurrencyCode.get(ipInfo.getCountry()).asText());

				// set country phone code
				if (!jsonNodeCountryPhoneCode.get(ipInfo.getCountry()).asText().contains("+")) {
					ipInfo.setCountryPhoneCode("+" + jsonNodeCountryPhoneCode.get(ipInfo.getCountry()).asText());
				} else {
					ipInfo.setCountryPhoneCode(jsonNodeCountryPhoneCode.get(ipInfo.getCountry()).asText());
				}

				// split "loc" in geo coordinates (latitude & longitude)
				String[] loc = ipInfo.getLoc().split(",");
				double lat = Double.valueOf(loc[0]);
				double lng = Double.valueOf(loc[1]);

				ipInfo.setLat(lat);
				ipInfo.setLng(lng);

			}

		} catch (JsonProcessingException e) {
			throw new GenericException();
		} catch (Exception e) {
			throw new GenericException();
		}

		return ipInfo;

	}

}
