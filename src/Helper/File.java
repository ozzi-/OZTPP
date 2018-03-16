package Helper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class File {
	public static String readFile(String path, String encoding) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException|NullPointerException e1) {
			System.out.println("Error reading "+path);
			System.exit(1);
		}
		String res ="";
		try{
			res =  new String(encoded, encoding);	
		}catch(UnsupportedEncodingException e){
			try {
				res =  new String(encoded, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				System.out.println("This should not happen");
			}
			System.out.println("Invalid encoding provided, falling back to UTF-8");
		}
		
		return res;
	}
}
