package ua.orlov.gymtrainerworkload.service.training.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;
import ua.orlov.gymtrainerworkload.repository.TrainingRepository;
import ua.orlov.gymtrainerworkload.service.training.TrainingService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;

    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    @Transactional
    public void deleteTraining(Training training) {
        Training foundTraining =
                getTrainingByCriteria(training.getTrainer(), training.getTrainingDate(), training.getDuration());

        trainingRepository.delete(foundTraining);
    }

    @Override
    public List<Training> findAllTrainingsByTrainer(Trainer trainer) {
        return trainingRepository.findByTrainer(trainer);
    }

    private Training getTrainingByCriteria(Trainer trainer, LocalDate trainingDate, Long trainingDuration) {
        Objects.requireNonNull(trainer, "Trainer must not be null");

        return trainingRepository.findByTrainerAndTrainingDateAndDuration(trainer, trainingDate, trainingDuration)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("No training found for trainer '%s' on %s with duration %d",
                        trainer.getUsername(), trainingDate, trainingDuration)
                ));
    }

}
