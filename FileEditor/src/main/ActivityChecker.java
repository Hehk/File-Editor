package main;

//Imports
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Checks all activity during this implementation in user determined directory
// - ones done inside and outside the console
// - records activity in log.txt saved in user determined directory
public class ActivityChecker implements Runnable {

	//used to determine if the run method should run
	private boolean running = true;
	// used as path for all functions
	private Path path;

	// constructor
	public ActivityChecker(Path path) {
		this.path = path;
	}

	// stops run()
	public void stopRun(){
		running = false;
	}

	// Performs the main purpose of the class
	@Override
	public void run() {

		try ( 
				// declaring variables that need to be closed in case of error
				WatchService service = path.getFileSystem().newWatchService();
				PrintWriter out = new PrintWriter(path.toString() + "/log.txt");
				) {
			
			// declaring variables that do not need to/have 
			// functionality to be closed through try/catch
			Map<WatchKey, Path> keyMap = new HashMap<>();
			keyMap.put(path.register(service,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.OVERFLOW)
					, path);
			WatchKey watchKey = null;
			Date date= new Date();

			// loops infinitely until running = false
			do {
				// sets watchKey to the new events and then path to their location
				watchKey = service.take();
				Path eventDir = keyMap.get(watchKey);

				// loops until all events are reported
				// puts the events into log.txt
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path eventPath = (Path)event.context();
					out.println("[" + new Timestamp(date.getTime()) + "] " 
							+ eventDir + ": " + kind + ": " + eventPath);
				}

			} while (watchKey.reset() && running);

		} catch (Exception e) {
			//outputs for the user if there are problem logging
			System.out.println("error in making log");
		}

	}

}
