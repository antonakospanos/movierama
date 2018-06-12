package org.antonakospanos.movierama.web.dto.votes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * VoteDto
 */
@JsonPropertyOrder({ "movie", "like" })
@Component
public class VoteDto {

	@NotNull
	@ApiModelProperty(example = "6b6f2985-ae5b-46bc-bad1-f9176ab90171", required = true)
	private UUID movie;

	@NotNull
	@ApiModelProperty(example = "true", required = true)
	private boolean like;

	public VoteDto() {
	}

	public VoteDto(UUID movie) {
		this.movie = movie;
	}

	public UUID getMovie() {
		return movie;
	}

	public void setMovie(UUID movie) {
		this.movie = movie;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}