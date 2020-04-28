package br.com.bernardolobato.pipa.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreResultWrapperDTO {
        private List<ScoreResultDTO> highscores;
}