package ua.orlov.gymtrainerworkload.mapper;

import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

public class TrainingMapper {

    public static Training trainerWorkloadToTraining(TrainerWorkload trainerWorkload, Trainer trainer) {
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingDate(trainerWorkload.getTrainingDate());
        training.setDuration(trainerWorkload.getTrainingDuration());

        return training;
    }

}
