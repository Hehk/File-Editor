package main;

import java.io.IOException;

public class runner {

	public static void main(String[] args) throws IOException {
		
    // Makes and instance of the FileEditor class
    FileEditor editor = new FileEditor();
		
    // Runs methods to edit files
		editor.start();
		editor.operator();
		
	}

}
