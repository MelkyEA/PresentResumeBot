package com.melky.bot.weather;


import com.google.gson.JsonObject;
import com.melky.bot.parser.ParserJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Weather {

    private ParserJson parserJson;

    private String lang = "ru";
    private String city;
    @Value("${weather.appId}")
    private String weatherAppId;

    public Weather(ParserJson parserJson) {
        this.parserJson = parserJson;
    }

    public String getCurrentWeather(String city){
        JsonObject json = null;
        this.city = city;
        try {
            json = parserJson.readJsonFromUrl("https://api.openweathermap.org/data/2.5/forecast?q=" + city
                    + "&cnt=1" +"&appid=" + weatherAppId + "&lang=" + lang + "&units=metric");
        }catch (IOException e){
            e.printStackTrace();
        }
        String description, name;
        int temp, feelsLike, windSpeed;
        JsonObject listJson = json.getAsJsonArray("list").get(0).getAsJsonObject();
        description = listJson.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
        name = json.getAsJsonObject("city").get("name").getAsString();
        temp = listJson.getAsJsonObject("main").get("temp").getAsInt();
        feelsLike = listJson.getAsJsonObject("main").get("feels_like").getAsInt();
        windSpeed = listJson.getAsJsonObject("wind").get("speed").getAsInt();
        return  "Прогноз погоды на близжайшие 3 часа: \n" +
                "Город: " + name + "\n" +
                description.substring(0,1).toUpperCase() + description.substring(1) + "\n" +
                "Температура: " + temp + "℃, ощущается как: " + feelsLike + "℃\n" +
                "Скорость ветра: " + windSpeed + "м/с";
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherAppId() {
        return weatherAppId;
    }

    public void setWeatherAppId(String weatherAppId) {
        this.weatherAppId = weatherAppId;
    }
}
