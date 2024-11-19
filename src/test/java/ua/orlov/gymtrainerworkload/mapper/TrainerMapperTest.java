package ua.orlov.gymtrainerworkload.mapper;

import org.junit.jupiter.api.Test;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.TrainerStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ua.orlov.gymtrainerworkload.mapper.TrainerMapper.trainerToTrainerSummary;
import static ua.orlov.gymtrainerworkload.mapper.TrainerMapper.trainerWorkloadToTrainer;

class TrainerMapperTest {

    private static final String USERNAME = "USERNAME";
    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "FIRST";

    @Test
    void trainerMapperCanBeCreated() {
        TrainerMapper trainerMapper = new TrainerMapper();
        assertNotNull(trainerMapper);
    }

    @Test
    void trainerWorkloadToTrainerGivenIsActiveNullThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setUsername(USERNAME);
        trainerWorkload.setFirstName(FIRST_NAME);
        trainerWorkload.setLastName(LAST_NAME);
        trainerWorkload.setIsActive(null);

        Trainer trainer = trainerWorkloadToTrainer(trainerWorkload);

        assertNotNull(trainer);
        assertEquals(USERNAME, trainer.getUsername());
        assertEquals(FIRST_NAME, trainer.getFirstName());
        assertEquals(LAST_NAME, trainer.getLastName());
        assertFalse(trainer.isActive());
    }

    @Test
    void trainerWorkloadToTrainerGivenIsActiveFalseThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setUsername(USERNAME);
        trainerWorkload.setFirstName(FIRST_NAME);
        trainerWorkload.setLastName(LAST_NAME);
        trainerWorkload.setIsActive(false);

        Trainer trainer = trainerWorkloadToTrainer(trainerWorkload);

        assertNotNull(trainer);
        assertEquals(USERNAME, trainer.getUsername());
        assertEquals(FIRST_NAME, trainer.getFirstName());
        assertEquals(LAST_NAME, trainer.getLastName());
        assertFalse(trainer.isActive());
    }

    @Test
    void trainerWorkloadToTrainerGivenIsActiveTrueThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setUsername(USERNAME);
        trainerWorkload.setFirstName(FIRST_NAME);
        trainerWorkload.setLastName(LAST_NAME);
        trainerWorkload.setIsActive(true);

        Trainer trainer = trainerWorkloadToTrainer(trainerWorkload);

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

        TrainerStatus trainerStatus = TrainerStatus.WORKING;
        Map<Integer, Map<Integer, Long>> durations = new HashMap<>();

        TrainerSummary trainerSummary = trainerToTrainerSummary(trainer, trainerStatus, durations);

        assertNotNull(trainerSummary);
        assertEquals(USERNAME, trainerSummary.getUsername());
        assertEquals(FIRST_NAME, trainerSummary.getFirstName());
        assertEquals(LAST_NAME, trainerSummary.getLastName());
        assertEquals(trainerStatus, trainerSummary.getStatus());
        assertEquals(durations, trainerSummary.getDurations());
    }

}
