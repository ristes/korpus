package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Category extends Model {

	public String name;

	@OneToMany(mappedBy = "category")
	public Set<Dokument> documents;

	@Override
	public String toString() {
		return String.format("[%d] %s", id, name);
	}

}
