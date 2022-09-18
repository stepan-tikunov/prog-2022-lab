import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Test {
	public static void main(String... args) {
		InputStreamReader reader = new InputStreamReader(System.in);
		try {
			while(!reader.ready()) {
			}
			System.out.println("ready");

		} catch (IOException e) {}
	}
}
