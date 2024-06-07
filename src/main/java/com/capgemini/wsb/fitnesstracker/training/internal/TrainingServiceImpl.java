package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingProvider {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public Optional<Training> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    public Training createTraining(TrainingDto trainingDto) {
        User user = userRepository.findById(trainingDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Training training = trainingMapper.toEntity(trainingDto, user);
        return trainingRepository.save(training);
    }

    public List<TrainingDto> getAllTrainings() {
        return trainingRepository.findAll().stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TrainingDto> getTrainingsByUserId(Long userId) {
        return trainingRepository.findAll().stream()
                .filter(training -> userId.equals(training.getUser().getId()))
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TrainingDto> getTrainingsByDate(Date date) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getEndTime().after(date))
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TrainingDto> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getActivityType().equals(activityType))
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    public Training updateTraining(Long trainingId, TrainingDto trainingDto) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));
        training.setStartTime(trainingDto.getStartTime());
        training.setEndTime(trainingDto.getEndTime());
        training.setActivityType(trainingDto.getActivityType());
        training.setDistance(trainingDto.getDistance());
        training.setAverageSpeed(trainingDto.getAverageSpeed());
        return trainingRepository.save(training);
    }

    public void deleteTraining(Long trainingId) {
        trainingRepository.deleteById(trainingId);
    }
}
