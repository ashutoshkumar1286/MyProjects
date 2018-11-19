package session;

public class LogEntry {
	private final String user;
	private final String time;
	private final String event;
	
	public LogEntry(String time, String user, String event) {
		this.time = time;
		this.user = user;
		this.event = event;
	}
	
	public String getUser() {
		return this.user;
	}

	public String getEvent() {
		return event;
	}

	public String getTime() {
		return time;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogEntry [user=").append(user).append(", time=").append(time).append(", event=").append(event)
				.append("]");
		return builder.toString();
	}
	
	
}
