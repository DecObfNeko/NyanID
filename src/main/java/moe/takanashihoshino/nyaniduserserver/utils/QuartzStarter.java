package moe.takanashihoshino.nyaniduserserver.utils;

import jakarta.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzStarter {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        if (scheduler.isInStandbyMode()) {
            scheduler.start();
        }
    }
}