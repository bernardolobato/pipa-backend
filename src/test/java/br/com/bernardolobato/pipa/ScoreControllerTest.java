package br.com.bernardolobato.pipa;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.bernardolobato.pipa.controller.ScoreController;
import br.com.bernardolobato.pipa.service.ScoreService;

import org.hamcrest.Matchers;


@WebMvcTest(ScoreController.class)
public class ScoreControllerTest {
    @Autowired
	private MockMvc mockMvc;

	@MockBean
	private ScoreService service;

    @Test
	public void ShouldReturnErrorWhenNegativeUserIsPosted() throws Exception {
        String scoreAsString = "{\"userId\":\"-1\",\"points\": \"10\"}";
                this.mockMvc.perform(
            post("/score")
                .content(scoreAsString)
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
	public void ShouldReturnErrorWhenNegativePointsIsPosted() throws Exception {
        String scoreAsString = "{\"userId\":\"1\",\"points\": \"-1\"}";
                this.mockMvc.perform(
            post("/score")
                .content(scoreAsString)
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
	public void ShouldReturnOkWhenCorrectJsonIsPosted() throws Exception {
        String scoreAsString = "{\"userId\":\"1\",\"points\": \"10\"}";
                this.mockMvc.perform(
            post("/score")
                .content(scoreAsString)
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print()).andExpect(status().isOk())
            .andExpect(content().string(Matchers.blankString()));
    }

    @Test
	public void shouldGetEmptyPositionOK() throws Exception {
        when(this.service
            .getPosition(1))
            .thenReturn(null);
		this.mockMvc.perform(get("/1/position")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(Matchers.blankString()));
    }
}