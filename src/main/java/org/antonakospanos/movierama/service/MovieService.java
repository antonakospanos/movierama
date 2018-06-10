package org.antonakospanos.movierama.service;

import org.antonakospanos.movierama.dao.model.Movie;
import org.antonakospanos.movierama.dao.repository.MovieRepository;
import org.antonakospanos.movierama.web.dto.movies.MovieBaseDto;
import org.antonakospanos.movierama.web.dto.movies.MovieDto;
import org.antonakospanos.movierama.web.dto.patch.PatchDto;
import org.antonakospanos.movierama.web.dto.response.CreateResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final static Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserService userService;

    @Transactional
    public CreateResponseData create(UUID userExternalId, MovieBaseDto movieBaseDto) {
        MovieDto movieDto = new MovieDto(movieBaseDto);
        Movie movie = movieRepository.findByTitle(movieDto.getTitle());

        if (movie != null) {
            throw new IllegalArgumentException("Movie '" + movie.getTitle() + "' already exists!");
        } else {
            // Add new Movie in DB
            movie = movieDto.toEntity();
            movie.setPublisher(userService.find(userExternalId));
            movieRepository.save(movie);

            logger.info("New Movie added: " + movie);
        }

        return new CreateResponseData(movie.getExternalId().toString());
    }

    @Transactional
    public CreateResponseData update(UUID externalId, List<PatchDto> patches) {
        validateMovie(externalId);
        Movie movie = movieRepository.findByExternalId(externalId);

        return update(movie, patches);
    }

    @Transactional
    public CreateResponseData update(Movie movie, List<PatchDto> patches) {
        patches.stream().forEach(patchDto -> {
            // Update Movie in DB
//            movieConverter.updateMovie(patchDto, movie);
        });

        movieRepository.save(movie);

        logger.info("Movie updated: " + movie);

        return new CreateResponseData(movie.getExternalId().toString());
    }

    @Transactional
    public MovieDto find(UUID externalId) {
        MovieDto movieDto = null;
        validateMovie(externalId);

        if (externalId != null) {
            Movie movie = movieRepository.findByExternalId(externalId);
            movieDto = new MovieDto().fromEntity(movie);
        }

        return movieDto;
    }

    @Transactional
    public List<MovieDto> list(UUID userId) {
        List<MovieDto> movieDtos;

        if (userId != null) {
            movieDtos = movieRepository.findByPublisher_ExternalId(userId)
                    .stream()
                    .map(movie -> new MovieDto().fromEntity(movie))
                    .collect(Collectors.toList());

        } else {
            // Fetch all movies
            movieDtos =  movieRepository.findAll()
                    .stream()
                    .map(movie -> new MovieDto().fromEntity(movie))
                    .collect(Collectors.toList());
        }

        return movieDtos;
    }

    // ************************ Validations ************************ //
    @Transactional
    public void validateMovie(UUID externalId) {
        if (externalId != null) {
            Movie movie = movieRepository.findByExternalId(externalId);
            if (movie == null) {
                throw new IllegalArgumentException("Movie '" + externalId + "' does not exist!");
            }
        }
    }
}