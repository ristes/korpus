package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Tag extends Model {

	public String name;

	public String binding;

	public String color;

	@ManyToOne
	public TagGroup group;

	@OneToMany(mappedBy = "tag")
	public Set<TaggedWord> taggedWords;

	@Override
	public String toString() {
		return String.format("[%s: %d] %s (%s)", group.name, id, name, binding);
	}
}
