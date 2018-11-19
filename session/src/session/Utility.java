package session;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Utility {
	public static final String START = "START";
	public static final String END = "END";
	private static final Pattern timePattern = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM:ss");
	
	public static long getDurationInSeconds(Session session) {
		return getTimeInSec(session.getEnd()) - getTimeInSec(session.getStart());
	}
	
	public static long getTimeInSec(String time) {
		String [] timePart = time.split(":");
		int hrs = Integer.parseInt(timePart[0]);
		int min = Integer.parseInt(timePart[1]);
		int sec = Integer.parseInt(timePart[2]);
		return hrs * 60 * 60 + min * 60 + sec; 
	}
	
	public static boolean isValidTime(String strTime) {
		return timePattern.matcher(strTime).find();
	}
	
	public static boolean isValidEvent(String event) {
		String tmpEvent = event.toUpperCase();
		return START.equals(tmpEvent) || END.equals(tmpEvent);
	}
}
