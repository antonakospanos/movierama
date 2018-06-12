package org.antonakospanos.movierama.service;

import org.antonakospanos.movierama.dao.model.Movie;
import org.antonakospanos.movierama.dao.model.User;
import org.antonakospanos.movierama.dao.repository.MovieRepository;
import org.antonakospanos.movierama.web.dto.votes.VoteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
public class VoteService {

    private final static Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserService userService;

    @Transactional
    public String put(UUID userExternalId, VoteDto voteDto) throws Exception {
        String result;

        Movie movie = movieRepository.findByExternalId(voteDto.getMovie());
        User user = userService.find(userExternalId);

        if (movie == null) {
            throw new IllegalArgumentException("Movie with ID '" + voteDto.getMovie() + "' does not exists!");
        } if (user == null) {
            throw new IllegalArgumentException("Access token '" + userExternalId + "' is not valid!");
        } else {
            User publisher = movie.getPublisher();
            Set<User> fans = movie.getFans();
            Set<User> haters = movie.getHaters();

            if (user.equals(publisher)) {
                throw new Exception(publisher.getName() + " is the publisher of '" + movie.getTitle() + "'!");
            } else if (fans.contains(user) && voteDto.isLike()) {
                throw new Exception(user.getName() + " has already voted '" + movie.getTitle() + "' positively!");
            } else if (haters.contains(user) && !voteDto.isLike()) {
                throw new Exception(user.getName() + " has already voted '" + movie.getTitle() + "' negatively!");
            } else if (fans.contains(user) && !voteDto.isLike()) {
                // Change vote to a hate!
                movie.removeFan(user);
                movie.addHater(user);
                movieRepository.save(movie);
                result = user.getName() + " changed the '" + movie.getTitle() + " vote to a hate!";
            } else if (haters.contains(user) && voteDto.isLike()) {
                // Change vote to a like!
                movie.removeHater(user);
                movie.addFan(user);
                movieRepository.save(movie);
                result = user.getName() + " changed the '" + movie.getTitle() + " vote to a like!";
            } else {
                // New vote!
                String vote;
                if (voteDto.isLike()) {
                    vote = "positive vote";
                    movie.addFan(user);
                } else {
                    vote = "negative vote";
                    movie.addHater(user);
                }
                movieRepository.save(movie);
                result = "A " +vote+" to '" + movie.getTitle() + "' was added by " + user.getName();
            }

            logger.info(result);

            return result;
        }
    }

    @Transactional
    public String delete(UUID userExternalId, UUID movieExternalId) throws Exception {
        String result;
        Movie movie = movieRepository.findByExternalId(movieExternalId);
        User user = userService.find(userExternalId);

        if (movie == null) {
            throw new IllegalArgumentException("Movie with ID '" + movieExternalId + "' does not exists!");
        } if (user == null) {
            throw new IllegalArgumentException("Access token '" + userExternalId + "' is not valid!");
        } else {
            Set<User> fans = movie.getFans();
            Set<User> haters = movie.getHaters();

            if (fans.contains(user)) {
                movie.removeFan(user);
                movieRepository.save(movie);
                result = "A positive vote to '" + movie.getTitle() + "' was retracted by " + user.getName();
            } else if (haters.contains(user)) {
                movie.removeHater(user);
                result = "A negative vote to '" + movie.getTitle() + "' was retracted by " + user.getName();
                movieRepository.save(movie);
            } else {
                throw new Exception(user.getName() + " has not voted '" + movie.getTitle() + "' yet!");
            }
        }

        logger.info(result);

        return result;
    }
}