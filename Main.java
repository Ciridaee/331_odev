import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] testFiles = {"test1.txt", "test2.txt", "test3.txt", "test4.txt"};
        
        for (String testFile : testFiles) {
            System.out.println("Running test: " + testFile);
            
            try {
                // Tüm testler için doğrudan MstProgram'ı çalıştır
                String expectedOutput = readExpectedOutput(testFile);
                String actualOutput = runMstProgram(testFile);
                
                if (expectedOutput.equals(actualOutput)) {
                    System.out.println(testFile + " passed.");
                } else {
                    System.out.println(testFile + " failed.");
                    System.out.println("Expected output:");
                    System.out.println(expectedOutput);
                    System.out.println("Program output:");
                    System.out.println(actualOutput);
                }
            } catch (Exception e) {
                System.out.println("Error running test: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println();
        }
    }
    
    private static String runMstProgram(String testFile) throws IOException, InterruptedException {
        // Komut oluştur
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", ".", "MstProgram", testFile);
        pb.redirectErrorStream(true); // stderr'i stdout'a yönlendir
        
        // Process'i başlat
        Process process = pb.start();
        
        // Çıktıyı oku
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        
        // Process'in bitmesini bekle
        process.waitFor();
        
        return output.toString();
    }
    
    private static String readExpectedOutput(String testFile) throws IOException {
        // Beklenen çıktıları hardcoded olarak döndür
        if (testFile.equals("test1.txt")) {
            return "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". b\n" +
                   ". . d\n" +
                   ". c\n";
        } else if (testFile.equals("test2.txt")) {
            return "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". b\n" +
                   ". c\n" +
                   ". . d\n" +
                   ". . f\n" +
                   ". . . e\n" +
                   ". . . g\n" +
                   "Directive-----------------> print-mst f\n" +
                   "f\n" +
                   ". c\n" +
                   ". . a\n" +
                   ". . . b\n" +
                   ". . d\n" +
                   ". e\n" +
                   ". g\n" +
                   "Directive-----------------> path f b\n" +
                   "f, c, a, b\n" +
                   "Directive-----------------> path f f\n" +
                   "f\n" +
                   "Directive-----------------> path a g\n" +
                   "a, c, f, g\n" +
                   "Directive-----------------> print-mst b\n" +
                   "b\n" +
                   ". a\n" +
                   ". . c\n" +
                   ". . . d\n" +
                   ". . . f\n" +
                   ". . . . e\n" +
                   ". . . . g\n";
        } else if (testFile.equals("test3.txt")) {
            return "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". b\n" +
                   ". c\n" +
                   ". . d\n" +
                   ". . f\n" +
                   ". . . e\n" +
                   ". . . g\n" +
                   "Directive-----------------> insert-edge a f 12\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". b\n" +
                   ". c\n" +
                   ". . d\n" +
                   ". . f\n" +
                   ". . . e\n" +
                   ". . . g\n" +
                   "Directive-----------------> insert-edge b g 1.5\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". b\n" +
                   ". . g\n" +
                   ". . . f\n" +
                   ". . . . c\n" +
                   ". . . . . d\n" +
                   ". . . . e\n" +
                   "Directive-----------------> path a e\n" +
                   "a, b, g, f, e\n" +
                   "Directive-----------------> decrease-weight a f 11.5\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". f\n" +
                   ". . c\n" +
                   ". . . d\n" +
                   ". . e\n" +
                   ". . g\n" +
                   ". . . b\n" +
                   "Directive-----------------> decrease-weight b c 5.5\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". f\n" +
                   ". . c\n" +
                   ". . . d\n" +
                   ". . e\n" +
                   ". . g\n" +
                   ". . . b\n" +
                   "Directive-----------------> decrease-weight b e 7\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". f\n" +
                   ". . c\n" +
                   ". . . d\n" +
                   ". . g\n" +
                   ". . . b\n" +
                   ". . . . e\n" +
                   "Directive-----------------> insert-edge b c 1\n" +
                   "Invalid Operation\n" +
                   "Directive-----------------> decrease-weight c g 4\n" +
                   "Invalid Operation\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". f\n" +
                   ". . c\n" +
                   ". . . d\n" +
                   ". . g\n" +
                   ". . . b\n" +
                   ". . . . e\n" +
                   "Directive-----------------> insert-edge a e 1.5\n" +
                   "Directive-----------------> print-mst a\n" +
                   "a\n" +
                   ". e\n" +
                   ". f\n" +
                   ". . c\n" +
                   ". . . d\n" +
                   ". . g\n" +
                   ". . . b\n" +
                   "Directive-----------------> path e b\n" +
                   "e, a, f, g, b\n";
        } else if (testFile.equals("test4.txt")) {
            return "Directive-----------------> print-mst v0\n" +
                   "v0\n" +
                   ". v6\n" +
                   ". v7\n" +
                   ". . v1\n" +
                   ". . . v2\n" +
                   ". . . . v3\n" +
                   ". . . . v5\n" +
                   ". . . . . v4\n" +
                   ". . . v9\n" +
                   ". . . . v10\n" +
                   ". . v8\n" +
                   "Directive-----------------> print-mst v10\n" +
                   "v10\n" +
                   ". v9\n" +
                   ". . v1\n" +
                   ". . . v2\n" +
                   ". . . . v3\n" +
                   ". . . . v5\n" +
                   ". . . . . v4\n" +
                   ". . . v7\n" +
                   ". . . . v0\n" +
                   ". . . . . v6\n" +
                   ". . . . v8\n" +
                   "Directive-----------------> insert-edge v9 v3 0.25\n" +
                   "Directive-----------------> insert-edge v10 v4 0.8\n" +
                   "Directive-----------------> insert-edge v10 v7 15\n" +
                   "Directive-----------------> print-mst v10\n" +
                   "v10\n" +
                   ". v4\n" +
                   ". . v5\n" +
                   ". . . v2\n" +
                   ". . . . v1\n" +
                   ". . . . . v7\n" +
                   ". . . . . . v0\n" +
                   ". . . . . . . v6\n" +
                   ". . . . . . v8\n" +
                   ". . . . v3\n" +
                   ". . . . . v9\n" +
                   "Directive-----------------> decrease-weight v0 v8 8.9\n" +
                   "Directive-----------------> decrease-weight v10 v7 14.1\n" +
                   "Directive-----------------> print-mst v10\n" +
                   "v10\n" +
                   ". v4\n" +
                   ". v7\n" +
                   ". . v0\n" +
                   ". . . v6\n" +
                   ". . . v8\n" +
                   ". . v1\n" +
                   ". . . v2\n" +
                   ". . . . v3\n" +
                   ". . . . . v9\n" +
                   ". . . . v5\n" +
                   "Directive-----------------> decrease-weight v7 v1 0.1\n" +
                   "Directive-----------------> print-mst v10\n" +
                   "v10\n" +
                   ". v4\n" +
                   ". v7\n" +
                   ". . v0\n" +
                   ". . . v6\n" +
                   ". . . v8\n" +
                   ". . v1\n" +
                   ". . . v2\n" +
                   ". . . . v3\n" +
                   ". . . . . v9\n" +
                   ". . . . v5\n";
        }
        
        return "";
    }
} 