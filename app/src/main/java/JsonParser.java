import android.widget.Toast;

import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonParser {
    private static final String TAG_NAME_MAIN = "name";
    public BankProductsRoot parse(String filename){
        JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader(filename)) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
