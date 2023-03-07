package pck;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;

public class Main {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            String elements = reader.readLine();
            while (elements != null) {
                if (elements.length() == 0) {
                    elements = reader.readLine();
                    continue;
                }
                String[] el = elements.split("[ \t]");
                if (el.length == 0) {
                    elements = reader.readLine();
                    continue;
                }
                try {
                    int res = Calculator.calculatePostFix(el);
                    System.out.println(res);
                }
                catch (EmptyStackException | NumberFormatException e1) {
                    System.out.println("Malformed expression");
                }
                catch (Exception e2) {
                    System.out.println("Zero division");
                }
                elements = reader.readLine();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}