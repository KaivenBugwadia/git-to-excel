package a;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.util.List;
import java.net.http.HttpClient;
import org.json.simple.parser.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import org.json.simple.JSONObject; 


public class App {


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ParseException, GeneralSecurityException {
        String repoName = args[0];
        Long repoStars = getStars(repoName);

        final String spreadsheetId = "1YzmsjFN8BBw3mLvFZ4MGV34dJxj4rcBgSUz65fn5Ce0"; // Replace with your actual spreadsheet ID
        final String range = "Sheet1!A1"; // Replace with the desired cell or range
       
        System.out.println("Stargazers count for " + repoName + ": " + repoStars);
        writeValueToSheet(repoStars, spreadsheetId, range);
    }

    private static Long getStars(String repoName) throws URISyntaxException, IOException, InterruptedException, ParseException {
        

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.github.com/repos/" + repoName))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        return parseJson(body);
    }

    static Long parseJson(String in) throws ParseException{
        Object obj = new JSONParser().parse(in);
        JSONObject jo = (JSONObject) obj; 
        Long stars = (Long) jo.get("stargazers_count");
        return stars;


        

    }
  

  


private static void writeValueToSheet(Long value, String spreadsheetId, String range) throws IOException, GeneralSecurityException {
    // Authentication and service setup
    NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new FileReader("/home/kaiven/projects/project-metrics/src/main/java/git-stars-415400-9b75cc9a83de.json")); // Path to your credentials file
    List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
            .setAccessType("offline")
            .build();

    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

}
}
  
   
   
        

     
    


