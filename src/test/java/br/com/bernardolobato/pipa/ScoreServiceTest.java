package br.com.bernardolobato.pipa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import br.com.bernardolobato.pipa.controller.dto.ScoreFormDTO;
import br.com.bernardolobato.pipa.controller.dto.ScoreResultDTO;
import br.com.bernardolobato.pipa.service.ScoreService;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScoreServiceTest {

    @Autowired
    private ScoreService scoreService;
    
    @Test
    public void shouldInsertOnPosition0() {
        ScoreFormDTO dto = new ScoreFormDTO(1,1);
        this.scoreService.addScore(dto);
        ScoreResultDTO result = this.scoreService.getPosition(1);
        assertEquals(0, result.getPosition());
    }

    @Test
    public void shouldReturnNullWhenNoScoreIsPosted() {
        ScoreResultDTO result = this.scoreService.getPosition(1);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenNoUserScoreIsPosted() {
        ScoreFormDTO dto = new ScoreFormDTO(1, 1);
        this.scoreService.addScore(dto);
        ScoreResultDTO result = this.scoreService.getPosition(2);
        assertEquals(null, result);
    }

    @Test
    public void shouldReturnEmptyWhenNoUserScoreIsPosted() {
        List<ScoreResultDTO> result = this.scoreService.getHighScoreList();
        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnOneElementListWhenUserScoreIsPosted() {
        ScoreFormDTO dto = new ScoreFormDTO(1, 1);
        this.scoreService.addScore(dto);

        List<ScoreResultDTO> result = this.scoreService.getHighScoreList();
        assertEquals(1, result.size());
    }

    @Test
    public void shouldReturnOrderedElementListWhenUserScoreIsPosted() {
        ScoreFormDTO dto = new ScoreFormDTO(1, 11);
        this.scoreService.addScore(dto);
        ScoreFormDTO dto2 = new ScoreFormDTO(2, 10);
        this.scoreService.addScore(dto2);
        ScoreFormDTO dto3 = new ScoreFormDTO(3, 5);
        this.scoreService.addScore(dto3);
        ScoreFormDTO dto4 = new ScoreFormDTO(4, 100);
        this.scoreService.addScore(dto4);

        List<ScoreResultDTO> result = this.scoreService.getHighScoreList();
        assertEquals(result.get(0).getUserId(), 4);
        assertEquals(result.get(1).getUserId(), 1);
        assertEquals(result.get(2).getUserId(), 2);
        assertEquals(result.get(3).getUserId(), 3);
    }
}