package ua.orlov.gymtrainerworkload.service.training;

import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

import java.util.List;

public interface TrainingService {

    Training createTraining(Training training);

    void deleteTraining(Training training);

    List<Training> findAllTrainingsByTrainer(Trainer trainer);
}
