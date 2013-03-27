package controllers;

import java.io.FileNotFoundException;

import models.Role;
import models.User;
import play.cache.Cache;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;

public class Authentication extends Controller {
	static final String SESSION_USER_ID = "user_id";
	static final String SESSION_USERNAME = "username";
	static final String SESSION_ROLE_ID = "role_id";
	static final String SESSION_ROLE_NAME = "role_name";
	static final String REDIRECT_URL = "url";
	static final String DEFAULT_REDIRECT = "/";

	/***
	 * Check if user is authenticated. If he is not, he is redirected to login
	 * page.
	 */
	@Before(unless = { "index", "login", "authenticate", "accessDenied" })
	static void checkAuthenticated() {
		// If user is not logged in redirect to login screen
		if (!session.contains(SESSION_USER_ID)) {
			String redirectUrl = request.method == "GET" ? request.url
					: DEFAULT_REDIRECT;
			// Add in session variable the redirect url
			session.put(REDIRECT_URL, redirectUrl);
			index();
		} else {
			renderArgs.put("user", getAuthenticatedUser());
		}
	}

	/***
	 * Check if user is authenticated. If he is not, he is redirected to login
	 * page.
	 * 
	 * @throws FileNotFoundException
	 */
	@Before(unless = { "index", "login", "authenticate", "logout",
			"accessDenied" })
	static void checkAccess() {
		
	}

	/***
	 * Authenticates the user (puts user.id and user.username in session)
	 * 
	 * @param user
	 *            The user that we authenticate
	 */
	static void authenticate(User user) {
		session.put(SESSION_USER_ID, user.id);
		session.put(SESSION_USERNAME, user.username);
		session.put(SESSION_ROLE_ID, user.role.id);
		session.put(SESSION_ROLE_NAME, user.role.name);
		setRole();
		// user.save();
		String redirectUrl = session.get(REDIRECT_URL);
		if (redirectUrl != null) {
			redirect(redirectUrl);
		} else {
			redirect(DEFAULT_REDIRECT);
		}
	}

	/***
	 * Get the authenticated user id.
	 * 
	 * @return The authenticated user id.
	 */
	public static Long getAuthenticatedId() {
		return Long.parseLong(session.get(SESSION_USER_ID));
	}

	/***
	 * Get the authenticated user object.
	 * 
	 * @return The authenticated user object.
	 */
	public static User getAuthenticatedUser() {
		if (session == null || !session.contains(SESSION_USER_ID)) {
			return null;
		}
		Long id = getAuthenticatedId();
		String key = String.format("user_%d", id);
		User user = (User) Cache.get(key);
		if (user == null) {
			user = User.findById(id);
			Cache.add(key, user);
		}
		return user;
	}

	/***
	 * Checks if user is authenticated.
	 * 
	 * @return True if user is authenticated.
	 */
	static boolean isUserAuthenticated() {
		return session.contains(SESSION_USER_ID);
	}

	static void setRole() {
		Long role_id = Long.parseLong(session.get(SESSION_ROLE_ID));
		session.put("isAdmin", role_id == Role.ADMIN);
		session.put("isEditor", role_id == Role.EDITOR);
	}

	public static void index() {
		if (isUserAuthenticated()) {
			Application.index();
		}
		render();
	}

	public static void accessDenied() {
		render();
	}

	public static void login(
			@Required(message = "Корисничкото име е задолжително") String login,
			@Required(message = "Лозинка е задолжителна") String password) {
		if (validation.hasErrors()) {
			//
		} else {
			User user = User.findByLogin(login);
			if (user != null) {
				if (user.checkPassword(password)) {
					authenticate(user);
				} else {
					validation.addError("password", "Невалидна лозинка");
				}
			} else {
				validation.addError("username", "Невалидно корисничко име");
			}
		}
		render("Authentication/index.html", validation, login);
	}

	/***
	 * Logout the authenticated user.
	 */
	public static void logout() {
		session.clear();
		redirect(DEFAULT_REDIRECT);
	}

}
