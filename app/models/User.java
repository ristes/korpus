package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity(name = "KORPUS_USER")
public class User extends Model {

	public String username;

	public String password;

	@ManyToOne
	@JoinColumn(name = "role_id")
	public Role role;

	@OneToMany(mappedBy = "user")
	public List<TaggedWord> taggedWords;

	public static User findByLogin(String username) {
		return find("username", username).first();
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	@Override
	public String toString() {
		return String.format("[%d] %s", id, username);
	}

}
