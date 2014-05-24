package main;

// Imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileEditor {
	
	// Declaring general variables
	private Path path;
	private ActivityChecker checker;
	private Scanner s = new Scanner(System.in);

	// Intakes a valid path and starts the ActivityChecker
	public void start(){

		// Declares general variables
		boolean validPath = true;
		String file;

		do {
			// prompts user for path and intakes it
			System.out.print("Path: ");
			file = s.nextLine();
			
			// Tests if the path is valid
			// If it is not the loop will repeat
			try {
				path = Paths.get(file);
				validPath = true;
			} catch (Exception e) {
				System.out.println("invalid path.");
			}
			
			// Reports to the user if they inputed an invalid path
			if (!(new File(file).isFile() || new File(file).isDirectory())) {
				System.out.println("invalid path.");
			}

		} while (!validPath 
				|| !(new File(file).isFile() || new File(file).isDirectory()));

		// Sets the path to be that of a directory if it is a file
		if(!new File(file).isDirectory()){
			file = new File(file).getParent();
			System.out.println(file);
			path = Paths.get(file);
		} 

		// Starts the ActivityChecker
		checker = new ActivityChecker(path);
		new Thread(checker).start();
	}

	// Ends processes
	public void end(){
		s.close();
		checker.stopRun();
	}

	// Puts in all the user operations
	public void operator() throws IOException{
		// Declares variables for the user input 
		boolean validInput = false;
		s = new Scanner(System.in);
		String oper;

		do {
			// Takes in the user input
			System.out.print("Operation: ");
			oper = s.nextLine().toLowerCase().trim();
			
			// Switch for the different user inputs
			switch (oper) {
			case "copy":
				// Runs copy method
				do{
					System.out.print("File: ");
				}while(!copy(s.nextLine()));
				break;
				
			case "rename":
				// Runs rename method
				String file;
				do{
					System.out.print("File: ");
					file = s.nextLine();
					System.out.print("New name: ");
				}while(!rename(file, s.nextLine()));
				break;
				
			case "delete":
				// Runs delete method
				do{
					System.out.println("WHY!!!!");
					System.out.print("File: ");
				}while(!delete(s.nextLine()));
				break;
				
			case "create":
				// Runs create method
				do{
					System.out.print("File: ");
				}while(!create(s.nextLine()));
				break;
				
			case "end":
				// Opens log.txt so the checker is prompted through its loop
				FileWriter out = new FileWriter(path.toString() + "//log.txt");
				out.close();
				
				// Ends the looping and Runs end method
				end();
				validInput = true;
				break;
				
			default:
				// loops again and gives error message
				System.out.println("Invalid operation was inputed.");
				break;
				
			}

		} while (!validInput);

		s.close();
	}

	// Copies a user picked file
	private boolean copy(String file1) {
		// Creates the name for the copy
		String file2 = file1.substring(0, file1.lastIndexOf(".")) + " (copy)" + file1.substring(file1.lastIndexOf("."));
		
		// Makes the new file and outputs if there is an error
		try ( 
				// Creates file readers and writers
				BufferedReader in = new BufferedReader(new FileReader(path.toString() + "\\" + file1));
				BufferedWriter out = new BufferedWriter(new FileWriter(path.toString() + "\\" + file2));
				) {
			
			// Copies over the contents byte by byte
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			
			// Tells the operator method the method worked
			return true;
			
		} catch (FileNotFoundException e) {
			System.out.println("invalid input: 0");
		} catch (IOException e) {
			System.out.println("invalid input: 1");
		} 

		// Tells the operator method the method did not worked
		return false;
	}

	// Renames a user picked file
	private boolean rename(String file1, String newName) throws IOException {
		// Makes the renamed file and outputs if there is an error
		try ( 
				// Creates file readers and writers
				BufferedReader in = new BufferedReader(new FileReader(path.toString() + "\\" + file1));
				BufferedWriter out = new BufferedWriter(new FileWriter(path.toString() + "\\" + newName));
				) {

			// Copies over the contents byte by byte
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}

			// Closes the in File and deletes it 
			in.close();
			Files.delete(Paths.get(path.toString() + "\\" +  file1));

			// Tells the operator method the method worked
			return true;
			
		} catch (FileNotFoundException e) {
			System.out.println("invalid input: 0");
		} catch (IOException e) {
			System.out.println("invalid input: 1");
		} 

		// Tells the operator method the method did not worked
		return false;
	}

	// Deletes a user picked file
	private boolean delete(String file) {

		// Deletes the user selected file and reports any error
		try {
			// Deletes the File
			Files.delete(Paths.get(path.toString() + "\\" + file));

			// Tells the operator method the method worked
			return true;

		} catch (Exception e) {
			System.out.println("invalid input");
		}

		// Tells the operator method the method did not worked
		return false;
	}

	// Creates a user picked file
	private boolean create(String file) {

		// Create the new File and reports any error
		try ( BufferedWriter out = new BufferedWriter(new FileWriter(path.toString() + "\\" + file)); ) {

			// Tells the operator method the method worked
			return true;

		} catch (FileNotFoundException e) {
			System.out.println("invalid input: 0");
		} catch (IOException e) {
			System.out.println("invalid input: 1");
		} 

		// Tells the operator method the method did not worked
		return false;
	}

}
