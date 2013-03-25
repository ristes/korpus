package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class TagGroup extends Model {

	public String name;

	@OneToMany(mappedBy = "group")
	public Set<Tag> tags;

	@Override
	public String toString() {
		return String.format("[%d] %s", id, name);
	}
}
