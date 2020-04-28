package br.com.bernardolobato.pipa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bernardolobato.pipa.controller.dto.ScoreFormDTO;
import br.com.bernardolobato.pipa.controller.dto.ScoreResultDTO;
import br.com.bernardolobato.pipa.controller.dto.ScoreResultWrapperDTO;
import br.com.bernardolobato.pipa.service.ScoreService;

@RestController
@RequestMapping("/")
public class ScoreController {

    @Autowired
    ScoreService service;

    @PostMapping("/score")
    public ResponseEntity<Void> score(@RequestBody @Valid ScoreFormDTO score) {
        this.service.addScore(score);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/{userId}/position")
    public ResponseEntity<ScoreResultDTO> position(@PathVariable Integer userId) {
        return ResponseEntity.ok(this.service.getPosition(userId));
    }

    @GetMapping("/highscorelist")
    public ResponseEntity<ScoreResultWrapperDTO> highscorelist() {
        return ResponseEntity.ok(new ScoreResultWrapperDTO(this.service.getHighScoreList()));
    }
}