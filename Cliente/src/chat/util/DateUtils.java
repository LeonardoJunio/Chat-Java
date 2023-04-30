package chat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
	public static String dateNowFormated() {
		LocalDateTime dateToday = LocalDateTime.now();
		DateTimeFormatter formaterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("pt", "BR"));

		return formaterDate.format(dateToday);
	}

	public static String dateTimeNowFormated() {
		LocalDateTime dateToday = LocalDateTime.now();
		DateTimeFormatter formaterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss", new Locale("pt", "BR"));

		return formaterDate.format(dateToday);
	}
}
