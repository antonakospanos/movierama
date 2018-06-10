package org.antonakospanos.movierama.web.api.v1;

import io.swagger.annotations.*;
import org.antonakospanos.movierama.service.MovieService;
import org.antonakospanos.movierama.support.ControllerUtils;
import org.antonakospanos.movierama.support.LoggingHelper;
import org.antonakospanos.movierama.web.api.BaseMovieRamaController;
import org.antonakospanos.movierama.web.dto.movies.MovieBaseDto;
import org.antonakospanos.movierama.web.dto.movies.MovieDto;
import org.antonakospanos.movierama.web.dto.response.CreateResponse;
import org.antonakospanos.movierama.web.dto.response.ResponseBase;
import org.antonakospanos.movierama.web.enums.Result;
import org.antonakospanos.movierama.web.security.exception.MovieRamaAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "Movie API", tags = "Movies", position = 3, description = "Movie Management")
@RequestMapping(value = "/movies")
public class MovieController extends BaseMovieRamaController {

    private final static Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    MovieService service;

    @RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "Creates the movie", response = CreateResponse.class)
    @ApiImplicitParam(
            name = "Authorization",
            value = "Bearer <The user's access token obtained upon registration or authentication>",
            example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
            required = true,
            dataType = "string",
            paramType = "header"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The movie is created!", response = ResponseBase.class),
            @ApiResponse(code = 400, message = "The request is invalid!"),
            @ApiResponse(code = 500, message = "server error")})
    public ResponseEntity<ResponseBase> create(@RequestHeader(value = "Authorization") String authorization, @Valid @RequestBody MovieBaseDto movieBaseDto) {
        ResponseEntity<ResponseBase> response;
        logger.debug(LoggingHelper.logInboundRequest(movieBaseDto));

        String accessToken = StringUtils.substringAfter(authorization, "Bearer ");
        UUID userExternalId;
        try {
            userExternalId = UUID.fromString(accessToken);
        } catch (Exception e) {
            throw new MovieRamaAuthenticationException("Invalid HTTP Authorization header Bearer: " + accessToken);
        }

        service.create(userExternalId, movieBaseDto);
        ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
        response = ResponseEntity.status(HttpStatus.CREATED).body(responseBase);

        logger.debug(LoggingHelper.logInboundResponse(response));

        return response;
    }

    @ApiOperation(value = "Lists all the movies", response = MovieDto.class, responseContainer = "List")
    @RequestMapping(value = "", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<Iterable> listMovies(@RequestParam (required = false) UUID userId) {

        logger.debug(LoggingHelper.logInboundRequest("/movies"));
        List<MovieDto> movies = service.list(userId);
        ResponseEntity<Iterable> response = ControllerUtils.listResources(movies);

        logger.debug(LoggingHelper.logInboundResponse(response));

        return response;
    }
}
