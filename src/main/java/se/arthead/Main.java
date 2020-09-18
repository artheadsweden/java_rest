package se.arthead;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Main {

    private static ApiResponse getRequest() throws IOException {
        URL url = new URL("https://reqres.in/api/users");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");



        ObjectMapper mapper = new ObjectMapper();

        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine;
            while((readLine = in.readLine()) != null) {
                response.append(readLine);
            } in.close();
            String jsonString = response.toString();

            return mapper.readValue(jsonString, ApiResponse.class);
        }
        else {
            System.out.println("Connection failed");
            return null;
        }
    }

    private static ApiCreatedUser postRequest(ApiUser user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        URL url = new URL("https://reqres.in/api/users");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream out = connection.getOutputStream();
        out.write(jsonString.getBytes());
        out.flush();
        out.close();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine;
            while((readLine = in.readLine()) != null) {
                response.append(readLine);
            } in.close();
            String jsonResponseString = response.toString();
            return mapper.readValue(jsonResponseString, ApiCreatedUser.class);
        }
        else {
            System.out.println("Could not post data");
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
	    ApiResponse response = getRequest();

	    if(response != null) {
            for(User user : response.getData())	{
                //System.out.println(user.getFirst_name() + " " + user.getLast_name());
            }
        }

	    ApiUser apiUser = new ApiUser("Pelle Plutt", "Bossen");
	    ApiCreatedUser createdUser = postRequest(apiUser);
	    if(createdUser != null) {
            System.out.println(createdUser.getName() + " " + createdUser.getCreatedAt());
        }
    }
}
