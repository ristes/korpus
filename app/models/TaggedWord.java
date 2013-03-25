package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class TaggedWord extends Model {

	public int start;

	public int end;

	@ManyToOne
	public Dokument dokument;

	@ManyToOne
	public Tag tag;

	@Override
	public String toString() {
		return String.format("[%d] %s (%d-%d) @ %s", id, dokument.name, start,
				end, tag.name);
	}
}
