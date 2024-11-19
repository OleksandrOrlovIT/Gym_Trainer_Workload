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

    @NotBlank(message = "username is required.")
    private String username;

    @NotBlank(message = "firstName is required.")
    private String firstName;

    @NotBlank(message = "lastName is required.")
    private String lastName;

    @NotNull(message = "isActive is required.")
    private Boolean isActive;

    @NotNull(message = "trainingDate is required.")
    private LocalDate trainingDate;

    @NotNull(message = "trainingDuration is required.")
    private Long trainingDuration;

    @NotNull(message = "actionType is required.")
    private ActionType actionType;

}
