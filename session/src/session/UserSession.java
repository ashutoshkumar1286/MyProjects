package session;

import java.util.LinkedList;
import java.util.Objects;

public class UserSession {	
	private final String userName;
	private final String logStartTime;
	private LinkedList<Session> sessionsInProgress;
	private int endAwaitIdx;
	private long duration;
	
	public UserSession(String userName, String logStartTime) {
		this.userName = userName;
		this.logStartTime = logStartTime;
		this.endAwaitIdx = -1;
		this.sessionsInProgress = new LinkedList<Session>();
		this.duration = 0;
	}
             

	public String getUserName() {
		return userName;
	}
	
	public void addLogEntry(LogEntry logEntry) {
		if(Utility.START.equals(logEntry.getEvent())) {
			Session session = new Session();
			session.setStart(logEntry.getTime());
			this.sessionsInProgress.add(session);
			this.endAwaitIdx=0;
		} else if(Utility.END.equals(logEntry.getEvent())) {
			if(this.endAwaitIdx > -1) { // There is start event awaiting for end
				Session curSession = this.sessionsInProgress.get(this.endAwaitIdx);
				curSession.setEnd(logEntry.getTime());
				this.duration+= Utility.getDurationInSeconds(curSession);
				this.sessionsInProgress.remove(this.endAwaitIdx);
				if(this.sessionsInProgress.isEmpty()) {
					this.endAwaitIdx=-1;
				}
				else { 
					this.endAwaitIdx=0;
				}				
			} else {
				this.duration += Utility.getDurationInSeconds(new Session(this.logStartTime, logEntry.getTime()));
			}
		}
	}
	
	public long getDuration(String logFileEndTime) {
		long inProgressDur = 0;
		for(Session session : this.sessionsInProgress) {
			if(Objects.isNull(session.getEnd())) {
				session.setEnd(logFileEndTime); // End Event without start event, setting start event end 
			}
			inProgressDur += Utility.getDurationInSeconds(session);
		}
		return duration+inProgressDur;
	}

}
