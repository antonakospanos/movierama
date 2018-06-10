package org.antonakospanos.movierama.web.dto.movies;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.movierama.dao.model.Movie;
import org.antonakospanos.movierama.web.dto.Dto;
import org.antonakospanos.movierama.web.dto.users.UserDto;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieDto
 */
@JsonPropertyOrder({ "title", "description", "publisher", "publicationDate", "likes", "hates" })
public class MovieDto extends MovieBaseDto implements Dto<Movie> {

	public static List<String> fields = Arrays.asList(MovieBaseDto.class.getDeclaredFields())
			.stream()
			.map(field -> field.getName())
			.collect(Collectors.toList());

	private UserDto publisher;

	private ZonedDateTime publicationDate;

	private Long likes;

	private Long hates;

	public MovieDto() {
	}

	public MovieDto(MovieBaseDto movieBaseDto) {
		super(movieBaseDto.getTitle(), movieBaseDto.getDescription());
	}

	public static List<String> getFields() {
		return fields;
	}

	public static void setFields(List<String> fields) {
		MovieDto.fields = fields;
	}

	public UserDto getPublisher() {
		return publisher;
	}

	public void setPublisher(UserDto publisher) {
		this.publisher = publisher;
	}

	public ZonedDateTime getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(ZonedDateTime publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public Long getHates() {
		return hates;
	}

	public void setHates(Long hates) {
		this.hates = hates;
	}

	@Override
	public MovieDto fromEntity(Movie movie) {
        setTitle(movie.getTitle());
        setDescription(movie.getDescription());
		setPublicationDate(movie.getPublicationDate());
		setPublisher(new UserDto().fromEntity(movie.getPublisher()));

		setLikes(Long.valueOf(movie.getFans().size()));
		setHates(Long.valueOf(movie.getHaters().size()));

		return this;
	}

	@Override
	public Movie toEntity() {
		Movie movie = new Movie();

		return toEntity(movie);
	}

	@Override
	public Movie toEntity(Movie movie) {
		movie.setTitle(this.getTitle());
        movie.setDescription(this.getDescription());
		if (movie.getPublicationDate() == null) {
			movie.setPublicationDate(ZonedDateTime.now());
		}
		if (movie.getFans() == null) {
			movie.setFans(new HashSet<>());
		}
		if (movie.getHaters() == null) {
			movie.setHaters(new HashSet<>());
		}
		// DAO: Add publisher!

		return movie;
	}
}
