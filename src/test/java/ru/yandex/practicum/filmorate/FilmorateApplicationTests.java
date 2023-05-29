package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;


@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDao userDao;
	private final FilmDao filmDao;

	@Test
	public void shouldFindFilm() {
		var film = new Film("DanilaFilm2", "SuperDanila2", LocalDate.parse("2000-08-31"), 229);
		filmDao.add(film);
		var film2 = filmDao.find(1);
		assertTrue(film2.isPresent());
		assertEquals(film2.get().getDuration(), 229);
	}

	@Test
	public void shouldRemoveFilm() {
		filmDao.remove(1);
		var films = filmDao.getAll();
		assertEquals(films.size(), 0);
	}

	@Test
	public void shouldRemoveUser() {
		userDao.remove(1);
		var users = userDao.getAll();
		assertEquals(users.size(), 0);
	}

	@Test
	public void shouldFindUser() {
		var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
		userDao.add(user);
		var user2 = userDao.find(1);
		assertTrue(user2.isPresent());
		assertEquals(user2.get().getLogin(), "Danila228");
	}

	@Test
	public void shouldCreateUser() {
		var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
		user = userDao.add(user);
		var users = userDao.getAll();

		assertEquals(users.size(), 1);
	}


	@Test
	public void shouldCreateFilm() {
		var film = new Film("DanilaFilm", "SuperDanila", LocalDate.parse("2000-07-31"), 228);
		film = filmDao.add(film);
		var films = filmDao.getAll();

		assertEquals(films.size(), 1);
	}

	@Test
	public void shouldChangeUser() {
		var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
		user = userDao.add(user);
		user.setName("Nikita");
		userDao.change(user);
		var user3 = userDao.find(3);
		assertTrue(user3.isPresent());
		assertEquals(user3.get().getName(), "Nikita");
	}

	@Test
	public void shouldChangeFilm() {
		var film = new Film("DanilaFilm", "SuperDanila", LocalDate.parse("2000-07-31"), 228);
		film = filmDao.add(film);
		film.setName("king-kong");
		filmDao.change(film);
		var film3 = filmDao.find(3);
		assertTrue(film3.isPresent());
		assertEquals(film3.get().getName(), "king-kong");
	}

	@Test
	public void shouldClearUsers() {
		userDao.clear();
		var users = userDao.getAll();
		assertEquals(users.size(), 0);
	}

	@Test
	public void shouldClearFilms() {
		filmDao.clear();
		var films = filmDao.getAll();
		assertEquals(films.size(), 0);
	}

}