package ua.orlov.gymtrainerworkload.service.user.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.mapper.TrainerMapper;
import ua.orlov.gymtrainerworkload.model.*;
import ua.orlov.gymtrainerworkload.repository.TrainerRepository;
import ua.orlov.gymtrainerworkload.repository.TrainingRepository;
import ua.orlov.gymtrainerworkload.service.user.TrainerService;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainerMapper trainerMapper;

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer findByUsername(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Trainer doesn't exist with username = " + username));
    }

    @Override
    public TrainerSummary getTrainerSummary(String username) {
        Trainer trainer = findByUsername(username);

        return trainerMapper.trainerToTrainerSummary(trainer, getTrainersDurations(trainer));
    }

    private Map<Integer, Map<Month, Integer>> getTrainersDurations(Trainer trainer) {
        List<Training> trainings = findAllTrainingsByTrainer(trainer);

        Map<Integer, Map<Month, Integer>> durations = new HashMap<>();

        for(Training training : trainings) {
            int year = training.getTrainingDate().getYear();
            Month month = Month.fromOrder(training.getTrainingDate().getMonthValue());

            durations.computeIfAbsent(year, y -> new HashMap<>());

            Map<Month, Integer> monthlyDurations = durations.get(year);

            monthlyDurations.merge(month, training.getDurationMinutes(), Integer::sum);
        }

        return durations;
    }

    @Override
    @Transactional
    public void changeTrainerWorkload(TrainerWorkload trainerWorkload) {
        if(trainerWorkload.getActionType() == ActionType.ADD && !trainerExistsByUsername(trainerWorkload.getTrainerUsername())){
            Trainer trainer = trainerMapper.trainerWorkloadToTrainer(trainerWorkload);
            createTrainer(trainer);
        }

        Trainer trainer = findByUsername(trainerWorkload.getTrainerUsername());

        Training training = trainerMapper.trainerWorkloadToTraining(trainerWorkload, trainer);

        if(trainerWorkload.getActionType() == ActionType.ADD){
            createTraining(training);
        } else {
            deleteTraining(training);
        }
    }

    @Override
    public boolean trainerExistsByUsername(String username) {
        return trainerRepository.existsByUsername(username);
    }

    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    @Transactional
    public void deleteTraining(Training training) {
        Training foundTraining =
                getTrainingByCriteria(training.getTrainer(), training.getTrainingDate(), training.getDurationMinutes());

        trainingRepository.delete(foundTraining);
    }

    @Override
    public List<Training> findAllTrainingsByTrainer(Trainer trainer) {
        return trainingRepository.findByTrainer(trainer);
    }

    private Training getTrainingByCriteria(Trainer trainer, LocalDate trainingDate, Integer trainingDuration) {
        Objects.requireNonNull(trainer, "Trainer must not be null");

        return trainingRepository.findByTrainerAndTrainingDateAndDurationMinutes(trainer, trainingDate, trainingDuration)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("No training found for trainer '%s' on %s with duration %d",
                                trainer.getUsername(), trainingDate, trainingDuration)
                ));
    }
}
