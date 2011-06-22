package us.davidphillips.georgie;

/* Status are:
	public final static int NOT_STARTED = 0;
	public final static int STARTED = 1;
	public final static int COMPLETED = 2;
	public final static int ERROR = 3;
*/

public interface HttpFilePosterObserver {
	void statusUpdated(int newStatus);
}
