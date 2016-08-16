package com.fatrio.java.feature;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class TimeApiTest {
	
	private static final ZoneId BA = ZoneId.of("America/Argentina/Buenos_Aires");
	private static final ZoneId PST = ZoneId.of("America/Los_Angeles");
	private static final ZoneId UTC = ZoneId.of("UTC");
	
	@Test
	public void formatLocalDateTime() {
		LocalDateTime now = LocalDateTime.now(UTC);
		System.out.println(DateTimeFormatter.ISO_DATE_TIME.format(now));
	}
	
	@Test
	public void fromDateToLocalDateTime() {
		Date date = new Date();
		for (ZoneId zoneId : Arrays.asList(BA, PST)) {
			LocalDateTime local = LocalDateTime.ofInstant(date.toInstant(), zoneId);
			System.out.println(String.format("%-30s, %s", zoneId.getId(), DateTimeFormatter.ISO_DATE_TIME.format(local)));
		}			
	}
	
	@Test
	public void fromLocalDateTimeToDate() {
		LocalDateTime now = LocalDateTime.now(UTC);
		SimpleDateFormat formatter = new SimpleDateFormat();
		System.out.println(String.format("BA: %s", formatter.format(Date.from(now.atZone(BA).toInstant()))));
		System.out.println(String.format("LA: %s", formatter.format(Date.from(now.atZone(PST).toInstant()))));
	}

	@Test
	public void calculateDuration() {
		Instant now = Instant.now();
		// Create an instant in the future
		Instant future = now.plusSeconds(this.secondsInHour() * (24 + 12));
		
		Duration duration = Duration.between(now, future);
		System.out.println(String.format("Days: %d, hours: %d, minutes: %d", duration.toDays(), duration.toHours(), duration.toMinutes()));
	}

	private long secondsInHour() {		
		return 60 * 60;
	}
	
	@Before
	public void printSeparator() {
		System.out.println("----");
	}
}
