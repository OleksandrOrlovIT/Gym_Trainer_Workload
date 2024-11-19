package ua.orlov.gymtrainerworkload.mapper;

import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;

public class TrainerMapper {

    public static Trainer trainerWorkloadToTrainer(TrainerWorkload trainerWorkload){
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerWorkload.getUsername());
        trainer.setFirstName(trainerWorkload.getFirstName());
        trainer.setLastName(trainerWorkload.getLastName());
        trainer.setActive(trainerWorkload.isActive());

        return trainer;
    }

}
