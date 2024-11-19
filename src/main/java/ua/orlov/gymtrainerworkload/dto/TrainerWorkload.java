package ua.orlov.gymtrainerworkload.dto;

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

    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private LocalDate trainingDate;

    private Long trainingDuration;

    private ActionType actionType;

}
