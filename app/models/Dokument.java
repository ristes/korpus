package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Dokument extends Model {

	public String name;

	public String author;

	public Date date;

	@Lob
	public String text;

	@ManyToOne
	public Category category;
	
	@OneToMany(mappedBy="dokument")
	public Set<TaggedWord> taggedWords;
	

	@Override
	public String toString() {
		return String.format("[%d- %s] %s", id, author, name);
	}

	public String getDateString() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		if (date != null) {
			return sdf.format(date);
		} else {
			return "";
		}
	}
}
