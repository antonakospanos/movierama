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

        Movie movie = movieRepository.findByTitle(voteDto.getTitle());
        User user = userService.find(userExternalId);

        if (movie == null) {
            throw new IllegalArgumentException("Movie '" + voteDto.getTitle() + "' does not exists!");
        } if (user == null) {
            throw new IllegalArgumentException("Access token '" + userExternalId + "' is not valid!");
        } else {
            User publisher = movie.getPublisher();
            Set<User> fans = movie.getFans();
            Set<User> haters = movie.getHaters();

            if (user.equals(publisher)) {
                throw new Exception("The vote is rejected. " + publisher.getName() + " is the publisher of '" + movie.getTitle() + "'!");
            } else if (fans.contains(user) && voteDto.isLike()) {
                throw new Exception(user.getName() + " has already submitted a positive vote to '" + movie.getTitle() + "'!");
            } else if (haters.contains(user) && !voteDto.isLike()) {
                throw new Exception(user.getName() + " has already submitted a negative vote to '" + movie.getTitle() + "'!");
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
}