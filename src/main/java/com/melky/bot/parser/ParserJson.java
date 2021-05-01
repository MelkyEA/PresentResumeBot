package com.melky.bot.parser;


import com.google.gson.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

@Component
public class ParserJson {


    private Gson gson = new Gson();

    // Перевод в String
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    // Чтение json из URL
    public JsonObject  readJsonFromUrl(String url) throws IOException{
        InputStream is = new URL(url).openStream();
        JsonObject json = null;
        try(BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));) {
            String jsonText = readAll(rd);
            json =(JsonObject)this.gson.fromJson(jsonText, JsonObject.class);
        }
        return json;
    }

}
