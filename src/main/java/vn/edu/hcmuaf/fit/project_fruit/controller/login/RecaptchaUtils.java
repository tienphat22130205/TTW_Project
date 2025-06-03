package vn.edu.hcmuaf.fit.project_fruit.controller.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecaptchaUtils {

    private static final String SECRET_KEY = "6LfPlFQrAAAAAIDIgYOXSYoJgDIj2lsuV0svJVZY"; // secret key của bạn

    public static boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            String postParams = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;

            OutputStream os = con.getOutputStream();
            os.write(postParams.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String json = response.toString();
            return json.contains("\"success\": true");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

