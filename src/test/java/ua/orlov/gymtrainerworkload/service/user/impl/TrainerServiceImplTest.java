package ua.orlov.gymtrainerworkload.service.user.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.mapper.TrainerMapper;
import ua.orlov.gymtrainerworkload.model.ActionType;
import ua.orlov.gymtrainerworkload.model.Month;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;
import ua.orlov.gymtrainerworkload.repository.TrainerRepository;
import ua.orlov.gymtrainerworkload.repository.TrainingRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    private static final String USERNAME = "USERNAME";

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerMapper trainerMapper;

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

        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> trainerServiceImpl.findByUsername(USERNAME));
        assertEquals("Trainer doesn't exist with username = " + USERNAME, e.getMessage());
    }

    @Test
    void findByUsernameThenSuccess() {
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));

        assertNotNull(trainerServiceImpl.findByUsername(USERNAME));

        verify(trainerRepository, times(1)).findByUsername(any());
    }

    @Test
    void changeTrainerWorkloadThenCreatesNewTrainerAndTraining() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActionType(ActionType.ADD);

        when(trainerRepository.save(any())).thenReturn(new Trainer());
        when(trainerRepository.existsByUsername(any())).thenReturn(false);
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));
        when(trainingRepository.save(any())).thenReturn(new Training());

        assertDoesNotThrow(() -> trainerServiceImpl.changeTrainerWorkload(trainerWorkload));

        verify(trainerRepository, times(1)).save(any());
        verify(trainerRepository, times(1)).existsByUsername(any());
        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingRepository, times(1)).save(any());
    }

    @Test
    void changeTrainerWorkloadThenCreatesNewTraining() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActionType(ActionType.ADD);

        when(trainerRepository.existsByUsername(any())).thenReturn(true);
        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));
        when(trainingRepository.save(any())).thenReturn(new Training());

        assertDoesNotThrow(() -> trainerServiceImpl.changeTrainerWorkload(trainerWorkload));

        verify(trainerRepository, times(1)).existsByUsername(any());
        verify(trainerRepository, times(1)).findByUsername(any());
        verify(trainingRepository, times(1)).save(any());
    }

    @Test
    void trainerExistsByUsernameThenSuccess() {
        when(trainerRepository.existsByUsername(any())).thenReturn(true);

        assertTrue(trainerServiceImpl.trainerExistsByUsername(USERNAME));

        verify(trainerRepository, times(1)).existsByUsername(any());
    }

    @Test
    void createTrainingThenSuccess() {
        when(trainingRepository.save(any())).thenReturn(new Training());

        Training training = trainerServiceImpl.createTraining(any());

        assertNotNull(training);
        verify(trainingRepository, times(1)).save(any());
    }

    @Test
    void deleteTrainingThenSuccess() {
        when(trainingRepository.findByTrainerAndTrainingDateAndDurationMinutes(any(), any(), any()))
                .thenReturn(Optional.of(new Training()));

        Training training = new Training();
        training.setTrainer(new Trainer());
        trainerServiceImpl.deleteTraining(training);

        verify(trainingRepository, times(1))
                .findByTrainerAndTrainingDateAndDurationMinutes(any(), any(), any());
        verify(trainingRepository, times(1)).delete(any());
    }

    @Test
    void deleteTrainingThenNullPointerException() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> trainerServiceImpl.deleteTraining(new Training()));

        assertEquals("Trainer must not be null", e.getMessage());
    }

    @Test
    void deleteTrainingThenNoSuchElementException() {
        when(trainingRepository.findByTrainerAndTrainingDateAndDurationMinutes(any(), any(), any()))
                .thenReturn(Optional.empty());

        Training training = new Training();
        training.setTrainer(new Trainer());

        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> trainerServiceImpl.deleteTraining(training));

        assertEquals("No training found for trainer 'null' on null with duration null", e.getMessage());

        verify(trainingRepository, times(1))
                .findByTrainerAndTrainingDateAndDurationMinutes(any(), any(), any());
    }

    @Test
    void findAllTrainingsByTrainerThenSuccess() {
        when(trainingRepository.findByTrainer(any())).thenReturn(new ArrayList<>());

        assertNotNull(trainerServiceImpl.findAllTrainingsByTrainer(new Trainer()));

        verify(trainingRepository, times(1)).findByTrainer(any());
    }

    @Test
    void changeTrainerWorkloadThenDelete() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setActionType(ActionType.DELETE);

        Training training = new Training();
        training.setTrainer(new Trainer());

        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(new Trainer()));
        when(trainerMapper.trainerWorkloadToTraining(any(), any())).thenReturn(training);
        when(trainingRepository.findByTrainerAndTrainingDateAndDurationMinutes(any(), any(), any()))
                .thenReturn(Optional.of(new Training()));

        trainerServiceImpl.changeTrainerWorkload(trainerWorkload);

        verify(trainerRepository).findByUsername(any());
        verify(trainerMapper).trainerWorkloadToTraining(any(), any());
        verify(trainingRepository).findByTrainerAndTrainingDateAndDurationMinutes(any(), any(), any());
        verify(trainingRepository).delete(any());
    }

    @Test
    void getTrainerSummaryThenSuccess() {
        Trainer trainer = new Trainer();

        Training training = new Training();
        training.setTrainer(new Trainer());
        training.setDurationMinutes(10);
        training.setTrainingDate(LocalDate.of(2020, 10, 10));

        Map<Month, Integer> monthSummary = new HashMap<>();
        monthSummary.put(Month.OCTOBER, 10);
        Map<Integer, Map<Month, Integer>> yearsSummary = new HashMap<>();
        yearsSummary.put(2020, monthSummary);

        when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(trainer));
        when(trainerMapper.trainerToTrainerSummary(any(), any())).thenReturn(new TrainerSummary());
        when(trainingRepository.findByTrainer(any())).thenReturn(List.of(training));

        assertDoesNotThrow(() -> trainerServiceImpl.getTrainerSummary(USERNAME));

        verify(trainerRepository).findByUsername(any());
        verify(trainingRepository).findByTrainer(any());
    }
}
