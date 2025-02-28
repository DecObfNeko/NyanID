package moe.takanashihoshino.nyaniduserserver.utils;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;


@Component
public class ScheduledTask {

    @Autowired
    private  UserDevicesRepository userDeviceRepository;
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    @Async
    public void asyncTask() {
        LocalDateTime cutoffDateTime = LocalDateTime.now()
                .minusDays(7)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        userDeviceRepository.deleteByCreateTimeBefore(cutoffDateTime);
    }
}
