package org.antonakospanos.movierama.service;

import org.antonakospanos.movierama.dao.model.Movie;
import org.antonakospanos.movierama.dao.model.User;
import org.antonakospanos.movierama.dao.repository.MovieRepository;
import org.antonakospanos.movierama.web.dto.response.CreateResponseData;
import org.antonakospanos.movierama.web.dto.votes.VoteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VoteService {

    private final static Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserService userService;

    @Transactional
    public CreateResponseData create(UUID userExternalId, VoteDto voteDto) throws Exception {
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

            Set<Long> voters =
                    Stream.of(fans, haters)
                    .flatMap(Set::stream)
                    .map(voter -> voter.getId())
                    .collect(Collectors.toSet());

            if (user.getId().equals(publisher.getId())) {
                throw new Exception("The vote is rejected. " + publisher.getName() + " is the publisher of '" + movie.getTitle() + "'!");
            } else if (voters.contains(user.getId())) {
                throw new Exception("The vote is rejected. " + user.getName() + " has already voted for movie '" + movie.getTitle() + "'!");
            } else {
                String vote;
                if (voteDto.isLike()) {
                    vote = "like";
                    movie.addFan(user);
                } else {
                    vote = "hate";
                    movie.addHater(user);
                }
                movieRepository.save(movie);
                logger.info(user.getName()+" added a "+vote+" for movie: " + movie.getTitle());
            }
        }

        return new CreateResponseData(movie.getExternalId().toString());
    }
}