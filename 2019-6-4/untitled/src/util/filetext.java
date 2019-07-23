package util;

import java.io.File;

public class filetext {
    public static void main(String arg[]) {
        File file=new File("D:\\abc.java");
        if(file.exists())
        {
            file.renameTo(new File("d:\\123.txt"));
        }
    }
}
