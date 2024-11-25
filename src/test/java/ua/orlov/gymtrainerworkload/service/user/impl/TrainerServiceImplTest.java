package ua.orlov.gymtrainerworkload.service.user.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.ActionType;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.TrainerStatus;
import ua.orlov.gymtrainerworkload.model.Training;
import ua.orlov.gymtrainerworkload.repository.TrainerRepository;
import ua.orlov.gymtrainerworkload.service.training.TrainingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    private static final String USERNAME = "USERNAME";

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

    @Test
    void createTrainerThenSuccess() {
        when(trainerRepository.save(any())).thenReturn(new Trainer());

        assertNotNull(trainerServiceImpl.createTrainer(new Trainer()));

        verify(trainerRepository, times(1)).save(any());
    }

    @Test
    void findByUsernameThenException() {
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainerServiceImpl.findByUsername(USERNAME));
        assertEquals("Trainer doesn't exist with username = " + USERNAME, e.getMessage());
    }

    @Test
    void findByUsernameThenSuccess() {
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));

        assertNotNull(trainerServiceImpl.findByUsername(USERNAME));

        verify(trainerRepository, times(1)).findByUsername(any());
    }

    @Test
    void getTrainerSummaryWithoutTrainingsThenSuccess() {
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));
        when(trainingService.findAllTrainingsByTrainer(any())).thenReturn(new ArrayList<>());

        TrainerSummary trainerSummary = trainerServiceImpl.getTrainerSummary(USERNAME);

        assertNotNull(trainerSummary);
        assertTrue(trainerSummary.getDurations().isEmpty());
        assertEquals(TrainerStatus.RESTING, trainerSummary.getStatus());

        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingService, times(1)).findAllTrainingsByTrainer(any());
    }

    @Test
    void getTrainerSummaryWithTrainingsThenSuccess() {
        Trainer trainer = new Trainer();
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingDate(LocalDate.now());
        training.setDuration(60L);

        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(trainer));
        when(trainingService.findAllTrainingsByTrainer(any())).thenReturn(List.of(training));

        TrainerSummary trainerSummary = trainerServiceImpl.getTrainerSummary(USERNAME);

        assertNotNull(trainerSummary);
        assertEquals(TrainerStatus.WORKING, trainerSummary.getStatus());
        assertEquals(trainer.getUsername(), trainerSummary.getUsername());
        assertFalse(trainerSummary.getDurations().isEmpty());

        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingService, times(1)).findAllTrainingsByTrainer(any());
    }

    @Test
    void getTrainerSummaryWithTrainingsOfPastMonthsThenSuccess() {
        Trainer trainer = new Trainer();
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingDate(LocalDate.now().minusMonths(2L));
        training.setDuration(60L);

        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(trainer));
        when(trainingService.findAllTrainingsByTrainer(any())).thenReturn(List.of(training));

        TrainerSummary trainerSummary = trainerServiceImpl.getTrainerSummary(USERNAME);

        assertNotNull(trainerSummary);
        assertEquals(TrainerStatus.RESTING, trainerSummary.getStatus());
        assertEquals(trainer.getUsername(), trainerSummary.getUsername());
        assertFalse(trainerSummary.getDurations().isEmpty());

        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingService, times(1)).findAllTrainingsByTrainer(any());
    }

    @Test
    void changeTrainerWorkloadThenCreatesNewTrainerAndTraining() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActionType(ActionType.ADD);

        when(trainerRepository.save(any())).thenReturn(new Trainer());
        when(trainerRepository.existsByUsername(any())).thenReturn(false);
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));
        when(trainingService.createTraining(any())).thenReturn(new Training());

        assertDoesNotThrow(() -> trainerServiceImpl.changeTrainerWorkload(trainerWorkload));

        verify(trainerRepository, times(1)).save(any());
        verify(trainerRepository, times(1)).existsByUsername(any());
        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingService, times(1)).createTraining(any());
    }

    @Test
    void changeTrainerWorkloadThenCreatesNewTraining() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActionType(ActionType.ADD);

        when(trainerRepository.existsByUsername(any())).thenReturn(true);
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));
        when(trainingService.createTraining(any())).thenReturn(new Training());

        assertDoesNotThrow(() -> trainerServiceImpl.changeTrainerWorkload(trainerWorkload));

        verify(trainerRepository, times(1)).existsByUsername(any());
        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingService, times(1)).createTraining(any());
    }

    @Test
    void changeTrainerWorkloadThenDeletesTraining() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActionType(ActionType.DELETE);

        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));

        assertDoesNotThrow(() -> trainerServiceImpl.changeTrainerWorkload(trainerWorkload));

        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingService, times(1)).deleteTraining(any());
    }

    @Test
    void trainerExistsByUsernameThenSuccess() {
        when(trainerRepository.existsByUsername(any())).thenReturn(true);

        assertTrue(trainerServiceImpl.trainerExistsByUsername(USERNAME));

        verify(trainerRepository, times(1)).existsByUsername(any());
    }
}
