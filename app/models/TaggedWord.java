package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import play.db.jpa.Model;
import controllers.Authentication;

@Entity
public class TaggedWord extends Model {

	public Date dateCreated;

	public int start;

	public int end;

	@ManyToOne
	public Dokument dokument;

	@ManyToOne
	public Tag tag;

	@ManyToOne
	public User user;

	@PrePersist
	protected void onCreate() {
		dateCreated = new Date();
		user = Authentication.getAuthenticatedUser();
	}

	@Override
	public String toString() {
		return String.format("[%d] %s (%d-%d) @ %s", id, dokument.name, start,
				end, tag.name);
	}
}
