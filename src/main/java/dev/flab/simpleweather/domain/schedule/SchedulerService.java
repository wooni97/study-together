package dev.flab.simpleweather.domain.schedule;

import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;

    public SchedulerService(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

}
