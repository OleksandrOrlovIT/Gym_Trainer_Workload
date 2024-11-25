package ua.orlov.gymtrainerworkload.service.user;

import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;

public interface TrainerService {

    Trainer createTrainer(Trainer trainer);

    Trainer findByUsername(String username);

    TrainerSummary getTrainerSummary(String username);

    void changeTrainerWorkload(TrainerWorkload trainerWorkload);

    boolean trainerExistsByUsername(String username);
}
