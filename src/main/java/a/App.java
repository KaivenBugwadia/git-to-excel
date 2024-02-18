package a;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;


public class App {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String repoName = args[0];
        String repoStars = getStars(repoName);
       
        System.out.println("Stargazers count for " + repoName + ": " + repoStars);
    }

    private static String getStars(String repoName) throws URISyntaxException, IOException, InterruptedException {
        

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.github.com/repos/" + repoName))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}


   
        

     
    


