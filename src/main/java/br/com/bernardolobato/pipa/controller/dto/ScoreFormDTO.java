package br.com.bernardolobato.pipa.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ScoreFormDTO {
    @NonNull
    @NotNull
    @Min(1)
    Integer userId;
    @Min(0)
    @NotNull
    Integer points;
}