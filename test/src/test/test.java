package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		
		String s = "C:\\Users\\Kyle\\Documents\\Documents";
		BufferedWriter bw = new BufferedWriter(new FileWriter(s + "/log.txt"));
		
		bw.write("was here");
		bw.close();
	}

}
