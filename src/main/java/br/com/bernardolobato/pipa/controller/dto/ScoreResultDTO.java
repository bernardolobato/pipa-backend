package br.com.bernardolobato.pipa.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ScoreResultDTO {
    @NonNull
    Long userId;
    Long score;
    Integer position;
}