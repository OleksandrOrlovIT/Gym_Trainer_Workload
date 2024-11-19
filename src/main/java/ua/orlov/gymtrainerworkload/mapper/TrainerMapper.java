package ua.orlov.gymtrainerworkload.mapper;

import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.TrainerStatus;

import java.util.Map;

public class TrainerMapper {

    public static Trainer trainerWorkloadToTrainer(TrainerWorkload trainerWorkload){
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerWorkload.getUsername());
        trainer.setFirstName(trainerWorkload.getFirstName());
        trainer.setLastName(trainerWorkload.getLastName());
        trainer.setActive(trainerWorkload.getIsActive() != null && trainerWorkload.getIsActive());

        return trainer;
    }

    public static TrainerSummary trainerToTrainerSummary
            (Trainer trainer, TrainerStatus status, Map<Integer, Map<Integer, Long>> durations){
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setUsername(trainer.getUsername());
        trainerSummary.setFirstName(trainer.getFirstName());
        trainerSummary.setLastName(trainer.getLastName());
        trainerSummary.setStatus(status);
        trainerSummary.setDurations(durations);
        return trainerSummary;
    }

}
