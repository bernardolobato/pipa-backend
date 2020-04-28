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
    Integer userId;
    Integer score;
    Integer position;
}