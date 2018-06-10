package org.antonakospanos.movierama.dao.repository;

import org.antonakospanos.movierama.dao.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	Movie findByTitle(String title);

	Movie findByExternalId(UUID externalId);

	List<Movie> findByPublisher_ExternalId(UUID userId);
}