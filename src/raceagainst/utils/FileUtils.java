package raceagainst.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Reads files as a string.
 * @source Pulled from TheCherno's Flappy tutorial FileUtils class.
 *         https://www.youtube.com/watch?v=527bR2JHSR0&t=1266s
 */
public class FileUtils {

    private FileUtils() {

    }

    public static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer = "";
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}