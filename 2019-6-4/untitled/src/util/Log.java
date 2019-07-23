package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	public static void d(Object data) {
		String data1 = data.toString();
		File f = new File("g://debug.txt");

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(f, true));
			b.write(data1);
			b.newLine();
			b.flush();
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
