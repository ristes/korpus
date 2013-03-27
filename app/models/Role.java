package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity(name = "KORPUS_ROLE")
public class Role extends Model {

	public static final int ADMIN = 1;
	public static final int EDITOR = 2;

	public String name;

	public Integer code;

	@OneToMany(mappedBy = "role")
	public Set<User> users;
	
	@Override
	public String toString() {
		return String.format("%s (%d)", name, code);
	}

}
