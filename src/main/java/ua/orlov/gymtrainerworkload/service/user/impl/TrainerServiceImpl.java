package ua.orlov.gymtrainerworkload.service.user.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.ActionType;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.TrainerStatus;
import ua.orlov.gymtrainerworkload.model.Training;
import ua.orlov.gymtrainerworkload.repository.TrainerRepository;
import ua.orlov.gymtrainerworkload.service.user.TrainerService;
import ua.orlov.gymtrainerworkload.service.training.TrainingService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static ua.orlov.gymtrainerworkload.mapper.TrainerMapper.trainerToTrainerSummary;
import static ua.orlov.gymtrainerworkload.mapper.TrainerMapper.trainerWorkloadToTrainer;
import static ua.orlov.gymtrainerworkload.mapper.TrainingMapper.trainerWorkloadToTraining;

@AllArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingService trainingService;

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

        Map<Integer, Map<Integer, Long>> durations = getTrainersDurations(trainer);
        LocalDate currentDate = LocalDate.now();
        TrainerStatus status;

        if(durations.containsKey(currentDate.getYear())
                && durations.get(currentDate.getYear()).containsKey(currentDate.getMonthValue())) {
            status = TrainerStatus.WORKING;
        } else {
            status = TrainerStatus.RESTING;
        }

        return trainerToTrainerSummary(trainer, status, durations);
    }

    private Map<Integer, Map<Integer, Long>> getTrainersDurations(Trainer trainer) {
        List<Training> trainings = trainingService.findAllTrainingsByTrainer(trainer);

        Map<Integer, Map<Integer, Long>> durations = new HashMap<>();

        for(Training training : trainings) {
            int year = training.getTrainingDate().getYear();
            int month = training.getTrainingDate().getMonthValue();

            durations.computeIfAbsent(year, y -> new HashMap<>());

            Map<Integer, Long> monthlyDurations = durations.get(year);

            monthlyDurations.merge(month, training.getDuration(), Long::sum);
        }

        return durations;
    }

    @Override
    @Transactional
    public void changeTrainerWorkload(TrainerWorkload trainerWorkload) {
        if(trainerWorkload.getActionType() == ActionType.ADD && !trainerExistsByUsername(trainerWorkload.getUsername())){
            Trainer trainer = trainerWorkloadToTrainer(trainerWorkload);
            createTrainer(trainer);
        }

        Trainer trainer = findByUsername(trainerWorkload.getUsername());

        Training training = trainerWorkloadToTraining(trainerWorkload, trainer);

        if(trainerWorkload.getActionType() == ActionType.ADD){
            trainingService.createTraining(training);
        } else {
            trainingService.deleteTraining(training);
        }
    }

    @Override
    public boolean trainerExistsByUsername(String username) {
        return trainerRepository.existsByUsername(username);
    }
}
