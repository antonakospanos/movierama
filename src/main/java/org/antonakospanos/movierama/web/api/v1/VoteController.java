package org.antonakospanos.movierama.web.api.v1;

import io.swagger.annotations.*;
import org.antonakospanos.movierama.service.VoteService;
import org.antonakospanos.movierama.support.LoggingHelper;
import org.antonakospanos.movierama.web.api.BaseMovieRamaController;
import org.antonakospanos.movierama.web.dto.response.CreateResponse;
import org.antonakospanos.movierama.web.dto.response.ResponseBase;
import org.antonakospanos.movierama.web.dto.votes.VoteDto;
import org.antonakospanos.movierama.web.enums.Result;
import org.antonakospanos.movierama.web.support.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Api(value = "Vote API", tags = "Votes", position = 3, description = "Vote Management")
@RequestMapping(value = "/votes")
public class VoteController extends BaseMovieRamaController {

    private final static Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    VoteService service;

    @RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.PUT)
    @ApiOperation(value = "Adds or updates a vote to the movie", response = CreateResponse.class)
    @ApiImplicitParam(
            name = "Authorization",
            value = "Bearer <The user's access token obtained upon registration or authentication>",
            example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
            required = true,
            dataType = "string",
            paramType = "header"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The vote is accepted!", response = ResponseBase.class),
            @ApiResponse(code = 400, message = "The request is invalid!"),
            @ApiResponse(code = 500, message = "server error")})
    public ResponseEntity<ResponseBase> put(@RequestHeader(value = "Authorization") String authorization, @Valid @RequestBody VoteDto voteDto) throws Exception {
        ResponseEntity<ResponseBase> response;
        logger.debug(LoggingHelper.logInboundRequest(voteDto));

        UUID accessToken = SecurityHelper.getAccessToken(authorization);
        String result = service.put(accessToken, voteDto);
        ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS).description(result);
        response = ResponseEntity.ok().body(responseBase);

        logger.debug(LoggingHelper.logInboundResponse(response));

        return response;
    }
}
