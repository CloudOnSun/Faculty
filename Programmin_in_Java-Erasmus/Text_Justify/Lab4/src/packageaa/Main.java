package packageaa;


import java.io.*;
import java.util.Scanner;


public class Main {

    public static int readLineLength(BufferedReader reader) throws IOException {
        int eofValue = reader.read();
        char character = (char) eofValue;
        StringBuilder lineLength = new StringBuilder();
        while (character != '\n' && character != '\r') {
            lineLength.append(character);
            eofValue = reader.read();
            character = (char) eofValue;
        }
        return Integer.parseInt(lineLength.toString());
    }

    public static void main(String[] args) throws IOException {

        //PrintStream printer = new PrintStream(System.out);
        //FileReader fileReader = new FileReader("in.txt");
        //BufferedReader reader1 = new BufferedReader(fileReader);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));

        try {

            int value = readLineLength(reader2);
            LineWorker lineWorker = new LineWorker(System.out, reader2, value);
            lineWorker.writeAll();

        }  catch (NumberFormatException e) {
            System.out.println("Error");
        }
        catch (IOException e) {

        }
        finally {
            try {
                //printer.close();
                reader2.close();
            }
            catch (IOException e) {

            }
        }
    }
}