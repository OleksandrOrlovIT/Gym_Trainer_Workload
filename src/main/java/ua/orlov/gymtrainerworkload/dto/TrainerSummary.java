package ua.orlov.gymtrainerworkload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.orlov.gymtrainerworkload.model.TrainerStatus;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerSummary {

    private String username;

    private String firstName;

    private String lastName;

    private TrainerStatus status;

    private Map<Integer, Map<Integer, Long>> durations;
}
