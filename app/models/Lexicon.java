package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Lexicon extends Model {

	public String word;
	
	public String lema;

	public String tag;

	@Override
	public String toString() {
		return String.format("[%d] %s - %s", id, word, tag);
	}

}
