package com.credibanco.tarjeta.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class TimeUtil {

	public boolean isWithin24Hours(LocalDateTime pastTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long hoursDifference = ChronoUnit.HOURS.between(pastTime, currentTime);
        return hoursDifference <= 24;
    }
	
}
