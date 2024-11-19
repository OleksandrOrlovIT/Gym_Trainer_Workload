package ua.orlov.gymtrainerworkload.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;
import ua.orlov.gymtrainerworkload.repository.TrainingRepository;
import ua.orlov.gymtrainerworkload.service.TrainerService;
import ua.orlov.gymtrainerworkload.service.TrainingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;

    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public void deleteTraining(Training training) {
        trainingRepository.delete(training);
    }

    @Override
    public List<Training> findAllTrainingsByTrainer(Trainer trainer) {
        return trainingRepository.findByTrainer(trainer);
    }
}
