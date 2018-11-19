package session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFileParser {
	private Map<String, UserSession> sessionMap;
	private String logFileStartTime;
	private String logFileEndTime;
	public LogFileParser() {
		this.sessionMap = new HashMap<String, UserSession>();
	}

	private Predicate<String> isLogEntryValid = (line) -> {
		String [] tokens = line.split(" ");
		return (tokens.length == 3) && Utility.isValidTime(tokens[0]) && Utility.isValidEvent(tokens[2]); 
	};
	
	
	private void addSessionLog(LogEntry logEntry) {
		if(this.sessionMap.isEmpty()) {
			this.logFileStartTime = logEntry.getTime(); // Log file start time
		}
		
		if( ! this.sessionMap.containsKey(logEntry.getUser())) {
			this.sessionMap.put(logEntry.getUser(), new UserSession(logEntry.getUser(), this.logFileStartTime));
		}
		UserSession userSession = this.sessionMap.get(logEntry.getUser());
		userSession.addLogEntry(logEntry);
		this.logFileEndTime = logEntry.getTime(); // Log file end time
	}
	
	public void printSessionDetail() {
		sessionMap.keySet().forEach(user -> System.out.println(user +" "+ this.sessionMap.get(user).getDuration(this.logFileEndTime)));
	}
	
	public void parseLog(List<String> logEntries) {
		if( ! logEntries.isEmpty()) {
			logEntries.stream()
			.filter(isLogEntryValid)
			.map(line -> line.split(" "))
			.map(tokens -> new LogEntry(tokens[0], tokens[1], tokens[2].toUpperCase()))
			.forEach(this::addSessionLog);
		}
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length < 1) {
			System.out.println("Input Log File missing !!!");
			System.exit(1);
		}
		Path path = Paths.get(args[0]);
		LogFileParser logFileParser = new LogFileParser();
		try(Stream<String> logStream = Files.lines(path)) {
			logFileParser.parseLog(logStream.collect(Collectors.toList()));
			logFileParser.printSessionDetail();
		}
	}

}
