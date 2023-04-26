package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.adapters.FilmAdapter;
import ru.yandex.practicum.filmorate.models.Film;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private static Gson gson;
    private static HttpClient client;
    private static ConfigurableApplicationContext applicationContext;

    @BeforeAll
    public static void setup() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Film.class, new FilmAdapter());
        gson = builder.create();
        applicationContext = SpringApplication.run(FilmorateApplication.class);
        client = HttpClient.newHttpClient();
    }

    @BeforeEach
    public void clear() throws IOException, URISyntaxException, InterruptedException {
        sendRequest("","DELETE");
    }

    @Test
    public void ShouldCreateFilm() throws IOException, URISyntaxException, InterruptedException {
        var film = new Film("Konosuba","test@yandex.ru",LocalDate.parse("2000-05-17"),100);
        var json = gson.toJson(film);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(1, films.size());
    }

    @Test
    public static void ShouldNotCreateFilmWithoutName() throws IOException, URISyntaxException, InterruptedException {
        var film = new Film("","test@yandex.ru",LocalDate.parse("2000-05-17"),100);
        var json = gson.toJson(film);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(0, films.size());
    }

    @Test
    public void ShouldNotCreateFilmWithLongDescription() throws IOException, URISyntaxException, InterruptedException {
        var film = new Film("Konosuba","test@yandex.ru3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222",LocalDate.parse("2000-05-17"),100);
        var json = gson.toJson(film);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(0, films.size());
    }

    @Test
    public void ShouldNotCreateFilmWithWrongDate() throws IOException, URISyntaxException, InterruptedException {
        var film = new Film("Konosuba","test@yandex.ru",LocalDate.parse("1001-05-17"),100);
        var json = gson.toJson(film);
        sendRequest(json, "POST");
        var result =  sendRequest("", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(0, films.size());
    }

    @Test
    public void ShouldNotCreateFilmWithWrongDuration() throws IOException, URISyntaxException, InterruptedException {
        var film = new Film("Konosuba","test@yandex.ru",LocalDate.parse("2000-05-17"),0);
        var json = gson.toJson(film);
        sendRequest(json, "POST");
        var result =  sendRequest( "", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(0, films.size());
    }

    @Test
    public void ShouldNotCreateFilmWithoutData() throws IOException, URISyntaxException, InterruptedException {
        sendRequest("", "POST");
        var result =  sendRequest( "", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(0, films.size());
    }

    @Test
    public void ShouldNotCreateFilmWithWrongData() throws IOException, URISyntaxException, InterruptedException {
        sendRequest("json", "POST");
        var result =  sendRequest( "", "GET");
        var films = gson.fromJson(result, ArrayList.class);
        assertEquals(0, films.size());
    }

    private String sendRequest(String body, String method) throws IOException, InterruptedException, URISyntaxException {
        var requestBuilder = HttpRequest.newBuilder();
        requestBuilder.setHeader("Content-Type", "application/json; charset=utf8");
        var uri = new URI("http://localhost:8080/api/films");

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

    @AfterAll
    public static void exit() {
        applicationContext.close();
    }
}
