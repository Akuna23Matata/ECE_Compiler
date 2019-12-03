import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main( String[] args ) throws IOException {
        ArrayList<String> input = new ArrayList<String>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(args[0]));
            String line;
            while ((line = br.readLine()) != null) {
                input.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }

        Bytecode bc = new Bytecode(input);
        ArrayList<Integer> output = bc.compile();
        DataOutputStream outputWriter = new DataOutputStream(new FileOutputStream("output"));
        for (int i : output) {
            outputWriter.write(i);
        }
        outputWriter.close();
    }
}
