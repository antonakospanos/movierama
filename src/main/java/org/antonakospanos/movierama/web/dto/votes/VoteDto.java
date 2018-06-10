package org.antonakospanos.movierama.web.dto.votes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * AccountDto
 */
@JsonPropertyOrder({ "title", "like" })
@Component
public class VoteDto {

	@NotEmpty
	@ApiModelProperty(example = "Star Wars", required = true)
	private String title;

	@NotNull
	@ApiModelProperty(example = "true", required = true)
	private boolean like;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}
}