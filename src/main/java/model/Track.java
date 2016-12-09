package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

@Entity
public class Track {
	@Id @GeneratedValue @DocumentId
	long id;
	@Field
	String title;
	
	public Track() {
		// empty
	}
	
	public Track(String title) {
		this.title = title;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
