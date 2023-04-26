package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.adapters.UserAdapter;
import ru.yandex.practicum.filmorate.models.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private static Gson gson;
    private static HttpClient client;

    @BeforeAll
    public static void setup() throws InterruptedException {
        Thread.sleep(1000);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(User.class, new UserAdapter());
        gson = builder.create();
        SpringApplication.run(FilmorateApplication.class);
        client = HttpClient.newHttpClient();
    }

    @BeforeEach
    public void clear() throws IOException, URISyntaxException, InterruptedException {
        sendRequest("","DELETE");
    }

    @Test
    public void shouldCreateUser() throws IOException, URISyntaxException, InterruptedException {
        var user = new User("Vasya","test@yandex.ru","VasyaTest", LocalDate.parse("2000-05-17"));
        var json = gson.toJson(user);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(1, users.size());
    }

    @Test
    public void shouldNotCreateUserWithoutEmail() throws IOException, URISyntaxException, InterruptedException {
        var user = new User("Vasya","","VasyaTest", LocalDate.parse("2000-05-17"));
        var json = gson.toJson(user);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    @Test
    public void shouldNotCreateUserWithWrongEmail() throws IOException, URISyntaxException, InterruptedException {
        var user = new User("Vasya","Vasya.ru","VasyaTest", LocalDate.parse("2000-05-17"));
        var json = gson.toJson(user);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    @Test
    public void shouldNotCreateUserWithoutLogin() throws IOException, URISyntaxException, InterruptedException {
        var user = new User("Vasya","test@yandex.ru","", LocalDate.parse("2000-05-17"));
        var json = gson.toJson(user);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    @Test
    public void shouldNotCreateUserWithSpacesInLogin() throws IOException, URISyntaxException, InterruptedException {
        var user = new User("Vasya","test@yandex.ru","Vas ya", LocalDate.parse("2000-05-17"));
        var json = gson.toJson(user);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    @Test
    public void shouldNotCreateUserWithBirthdayInFuture() throws IOException, URISyntaxException, InterruptedException {
        var user = new User("Vasya","test@yandex.ru","VasyaTest", LocalDate.parse("3000-05-17"));
        var json = gson.toJson(user);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    @Test
    public void shouldNotCreateUserWithoutData() throws IOException, URISyntaxException, InterruptedException {
        sendRequest("", "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    @Test
    public void shouldNotCreateUserWithWrongData() throws IOException, URISyntaxException, InterruptedException {
        sendRequest("json", "POST");
        var result =  sendRequest("", "GET");
        var users = gson.fromJson(result, ArrayList.class);
        assertEquals(0, users.size());
    }

    private String sendRequest(String body, String method) throws IOException, InterruptedException, URISyntaxException {
        var requestBuilder = HttpRequest.newBuilder();
        requestBuilder.setHeader("Content-Type", "application/json; charset=utf8");
        var uri = new URI("http://localhost:8080/api/users");

        HttpRequest request;
        switch (method) {
            default:
                request = requestBuilder
                        .GET()
                        .uri(uri)
                        .build();
                break;
            case "POST":
                request = requestBuilder
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .uri(uri)
                        .build();
                break;
            case "DELETE":
                request = requestBuilder
                        .DELETE()
                        .uri(uri)
                        .build();
        }

        var handler = HttpResponse.BodyHandlers.ofString();
        var result = client.send(request, handler);
        return result.body();
    }
}
