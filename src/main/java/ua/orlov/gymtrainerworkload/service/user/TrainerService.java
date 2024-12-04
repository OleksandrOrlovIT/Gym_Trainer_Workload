package ua.orlov.gymtrainerworkload.service.user;

import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

import java.util.List;

public interface TrainerService {

    Trainer createTrainer(Trainer trainer);

    Trainer findByUsername(String username);

    TrainerSummary getTrainerSummary(String username);

    void changeTrainerWorkload(TrainerWorkload trainerWorkload);

    boolean trainerExistsByUsername(String username);

    Training createTraining(Training training);

    void deleteTraining(Training training);

    List<Training> findAllTrainingsByTrainer(Trainer trainer);
}
