package org.pesho.mydictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleTranslator {

    private static final String URL_FORMAT = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=bg&dt=t&q=%s";

    private static GoogleTranslator INSTANCE = new GoogleTranslator();

    public static GoogleTranslator getInstance() {
        return INSTANCE;
    }

    private GoogleTranslator() {
    }

    public String translate(String word) {
        try {
            URL url = new URL(String.format(URL_FORMAT, word));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return null;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                String[] split = response.toString().split("\"");
                if (split.length > 1) {
                    return split[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
