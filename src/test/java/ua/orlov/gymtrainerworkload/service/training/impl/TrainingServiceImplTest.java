package ua.orlov.gymtrainerworkload.service.training.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;
import ua.orlov.gymtrainerworkload.repository.TrainingRepository;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    TrainingServiceImpl trainingService;

    @Test
    void createTrainingThenSuccess() {
        when(trainingRepository.save(any())).thenReturn(new Training());

        Training training = trainingService.createTraining(any());

        assertNotNull(training);
        verify(trainingRepository, times(1)).save(any());
    }

    @Test
    void deleteTrainingThenSuccess() {
        when(trainingRepository.findByTrainerAndTrainingDateAndDuration(any(), any(), any()))
                .thenReturn(Optional.of(new Training()));

        Training training = new Training();
        training.setTrainer(new Trainer());
        trainingService.deleteTraining(training);

        verify(trainingRepository, times(1))
                .findByTrainerAndTrainingDateAndDuration(any(), any(), any());
        verify(trainingRepository, times(1)).delete(any());
    }

    @Test
    void deleteTrainingThenNullPointerException() {
        var e = assertThrows(NullPointerException.class, () -> trainingService.deleteTraining(new Training()));

        assertEquals("Trainer must not be null", e.getMessage());
    }

    @Test
    void deleteTrainingThenNoSuchElementException() {
        when(trainingRepository.findByTrainerAndTrainingDateAndDuration(any(), any(), any()))
                .thenReturn(Optional.empty());

        Training training = new Training();
        training.setTrainer(new Trainer());

        var e = assertThrows(NoSuchElementException.class, () -> trainingService.deleteTraining(training));

        assertEquals("No training found for trainer 'null' on null with duration null", e.getMessage());

        verify(trainingRepository, times(1))
                .findByTrainerAndTrainingDateAndDuration(any(), any(), any());
    }

    @Test
    void findAllTrainingsByTrainerThenSuccess() {
        when(trainingRepository.findByTrainer(any())).thenReturn(new ArrayList<>());

        assertNotNull(trainingService.findAllTrainingsByTrainer(new Trainer()));

        verify(trainingRepository, times(1)).findByTrainer(any());
    }
}
