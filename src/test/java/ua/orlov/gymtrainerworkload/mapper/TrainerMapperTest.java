package ua.orlov.gymtrainerworkload.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Month;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerMapperTest {

    private static final String USERNAME = "USERNAME";
    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "FIRST";

    private TrainerMapper trainerMapper;

    @BeforeEach
    void setUp() {
        trainerMapper = new TrainerMapper();
    }

    @Test
    void trainerWorkloadToTrainerGivenIsActiveFalseThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainerUsername(USERNAME);
        trainerWorkload.setTrainerFirstName(FIRST_NAME);
        trainerWorkload.setTrainerLastName(LAST_NAME);
        trainerWorkload.setTrainerIsActive(false);

        Trainer trainer = trainerMapper.trainerWorkloadToTrainer(trainerWorkload);

        assertNotNull(trainer);
        assertEquals(USERNAME, trainer.getUsername());
        assertEquals(FIRST_NAME, trainer.getFirstName());
        assertEquals(LAST_NAME, trainer.getLastName());
        assertFalse(trainer.isActive());
    }

    @Test
    void trainerWorkloadToTrainerGivenIsActiveTrueThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainerUsername(USERNAME);
        trainerWorkload.setTrainerFirstName(FIRST_NAME);
        trainerWorkload.setTrainerLastName(LAST_NAME);
        trainerWorkload.setTrainerIsActive(true);

        Trainer trainer = trainerMapper.trainerWorkloadToTrainer(trainerWorkload);

        assertNotNull(trainer);
        assertEquals(USERNAME, trainer.getUsername());
        assertEquals(FIRST_NAME, trainer.getFirstName());
        assertEquals(LAST_NAME, trainer.getLastName());
        assertTrue(trainer.isActive());
    }

    @Test
    void trainerToTrainerSummaryThenSuccess() {
        Trainer trainer = new Trainer();
        trainer.setUsername(USERNAME);
        trainer.setFirstName(FIRST_NAME);
        trainer.setLastName(LAST_NAME);

        Map<Integer, Map<Month, Integer>> durations = new HashMap<>();

        TrainerSummary trainerSummary = trainerMapper.trainerToTrainerSummary(trainer, durations);

        assertNotNull(trainerSummary);
        assertEquals(USERNAME, trainerSummary.getUsername());
        assertEquals(FIRST_NAME, trainerSummary.getFirstName());
        assertEquals(LAST_NAME, trainerSummary.getLastName());
        assertFalse(trainerSummary.isStatus());
        assertEquals(durations, trainerSummary.getTrainingMinutesByYearAndMonth());
    }

    @Test
    void trainerWorkloadToTrainingThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTrainingDate(LocalDate.MIN);
        trainerWorkload.setTrainingDurationMinutes(50);

        Trainer trainer = new Trainer();

        Training training = trainerMapper.trainerWorkloadToTraining(trainerWorkload, trainer);

        assertNotNull(training);
        assertEquals(trainerWorkload.getTrainingDate(), training.getTrainingDate());
        assertEquals(trainerWorkload.getTrainingDurationMinutes(), training.getDurationMinutes());
        assertEquals(trainer, training.getTrainer());
    }
}
