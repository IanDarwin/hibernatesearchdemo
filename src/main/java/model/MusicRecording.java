package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *  This class represents a music recording.
 */
@Entity
@Indexed
@Table(name="MusicRecordings")
public class MusicRecording {

	@Id @GeneratedValue(strategy=GenerationType.AUTO) @DocumentId
	private int id;
	@Field
	private String artist;
	@Field
	protected String title;
	protected double price;
	@Version
	int version;
	@IndexedEmbedded
	@OneToMany(cascade=CascadeType.ALL)
	@OrderColumn(name="index_number")
	private Track tracks[] = new Track[0];
	
	public MusicRecording() {
		// empty
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 *  Returns the artist name
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 *  Sets the artist name
	 */
	public void setArtist(String theArtist) {
		artist = theArtist;
	}

	public List<Track> getTracks() {
		if (tracks == null || tracks.length == 0) {
			return new ArrayList<Track>();
		}
		return Arrays.asList(tracks);
	}

	public void setTracks(List<Track> tracks) {
		Track[] target = new Track[tracks.size()];
		this.tracks = (Track[]) tracks.toArray(target);
	}

	@Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(title).append(" - ");
        buffer.append(artist).append(" (");
        buffer.append(tracks.length).append(" tracks)");
        return buffer.toString();
    }
}
