package org.antonakospanos.movierama.dao.model;


import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "MOVIE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "movierama.entity-cache")
public class Movie implements Serializable {

	// Surrogate primary key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Exposed resource ID
	private UUID externalId;

	private String title;

	private String description;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User publisher;

	private ZonedDateTime publicationDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "MOVIE_FANS_ASSOCIATIONS",
			joinColumns = {@JoinColumn(name = "MOVIE_ID", referencedColumnName = "ID") },
			inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }
	)
	public Set<User> fans;


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "MOVIE_HATERS_ASSOCIATIONS",
			joinColumns = {@JoinColumn(name = "MOVIE_ID", referencedColumnName = "ID") },
			inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }
	)
	public Set<User> haters;


	public Movie() {
		this.externalId = UUID.randomUUID();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getExternalId() {
		return externalId;
	}

	public void setExternalId(UUID externalId) {
		this.externalId = externalId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getPublisher() {
		return publisher;
	}

	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}

	public ZonedDateTime getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(ZonedDateTime publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Set<User> getFans() {
		return fans;
	}

	public void setFans(Set<User> fans) {
		this.fans = fans;
	}

	public Set<User> getHaters() {
		return haters;
	}

	public void setHaters(Set<User> haters) {
		this.haters = haters;
	}

	public void addFan(User user) {
		if (this.fans == null) {
			this.fans = new HashSet<>();
		}

		this.fans.add(user);
	}

	public void addHater(User user) {
		if (this.haters == null) {
			this.haters = new HashSet<>();
		}

		this.haters.add(user);
	}

	public void removeFan(User user) {
		if (this.fans == null) {
			this.fans = new HashSet<>();
		}

		this.fans.remove(user);
	}

	public void removeHater(User user) {
		if (this.haters == null) {
			this.haters = new HashSet<>();
		}

		this.haters.remove(user);
	}
}
