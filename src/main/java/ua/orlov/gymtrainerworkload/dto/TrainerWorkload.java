package ua.orlov.gymtrainerworkload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.orlov.gymtrainerworkload.model.ActionType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkload {

    @NotBlank(message = "trainerUsername is required.")
    private String trainerUsername;

    @NotBlank(message = "trainerFirstName is required.")
    private String trainerFirstName;

    @NotBlank(message = "trainerLastName is required.")
    private String trainerLastName;

    @NotNull(message = "trainerIsActive is required.")
    private boolean trainerIsActive;

    @NotNull(message = "trainingDate is required.")
    private LocalDate trainingDate;

    @NotNull(message = "trainingDurationMinutes is required.")
    private Integer trainingDurationMinutes;

    @NotNull(message = "actionType is required.")
    private ActionType actionType;

}
