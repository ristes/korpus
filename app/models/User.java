package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity(name = "KORPUS_USER")
public class User extends Model {

	public String username;

	public String password;

	@ManyToOne
	@JoinColumn(name = "role_id")
	public Role role;
	
	public static User findByLogin(String username) {
		return find("username", username).first();
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

}
