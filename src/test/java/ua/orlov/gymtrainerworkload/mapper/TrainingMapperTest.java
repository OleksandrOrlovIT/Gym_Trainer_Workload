package ua.orlov.gymtrainerworkload.mapper;

import org.junit.jupiter.api.Test;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ua.orlov.gymtrainerworkload.mapper.TrainingMapper.trainerWorkloadToTraining;

class TrainingMapperTest {

    @Test
    void trainingMapperCanBeCreated() {
        TrainingMapper trainingMapper = new TrainingMapper();
        assertNotNull(trainingMapper);
    }

    @Test
    void trainerWorkloadToTrainingThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainingDate(LocalDate.MIN);
        trainerWorkload.setTrainingDuration(50L);

        Trainer trainer = new Trainer();

        Training training = trainerWorkloadToTraining(trainerWorkload, trainer);

        assertNotNull(training);
        assertEquals(trainerWorkload.getTrainingDate(), training.getTrainingDate());
        assertEquals(trainerWorkload.getTrainingDuration(), training.getDuration());
        assertEquals(trainer, training.getTrainer());
    }

}
