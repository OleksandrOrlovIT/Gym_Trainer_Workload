package ua.orlov.gymtrainerworkload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.orlov.gymtrainerworkload.model.Trainer;
import ua.orlov.gymtrainerworkload.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTrainer(Trainer trainer);

    Optional<Training> findByTrainerAndTrainingDateAndDuration(Trainer trainer, LocalDate trainingDate, Long duration);
}
