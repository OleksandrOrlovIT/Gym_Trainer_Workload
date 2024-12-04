package ua.orlov.gymtrainerworkload.mapper;

import org.springframework.stereotype.Component;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Month;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

import java.util.Map;

@Component
public class TrainerMapper {

    public Trainer trainerWorkloadToTrainer(TrainerWorkload trainerWorkload) {
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerWorkload.getTrainerUsername());
        trainer.setFirstName(trainerWorkload.getTrainerFirstName());
        trainer.setLastName(trainerWorkload.getTrainerLastName());
        trainer.setActive(trainerWorkload.isTrainerIsActive());

        return trainer;
    }

    public TrainerSummary trainerToTrainerSummary(Trainer trainer, Map<Integer, Map<Month, Integer>> durations) {
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setUsername(trainer.getUsername());
        trainerSummary.setFirstName(trainer.getFirstName());
        trainerSummary.setLastName(trainer.getLastName());
        trainerSummary.setStatus(trainer.isActive());
        trainerSummary.setTrainingMinutesByYearAndMonth(durations);
        return trainerSummary;
    }

    public Training trainerWorkloadToTraining(TrainerWorkload trainerWorkload, Trainer trainer) {
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingDate(trainerWorkload.getTrainingDate());
        training.setDurationMinutes(trainerWorkload.getTrainingDurationMinutes());

        return training;
    }
}
