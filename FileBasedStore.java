import java.time.Instant;
import java.util.HashMap;
import org.json.simple.JSONObject;

class Value {

    JSONObject value;
    int timeOut;

    Value(int timeOut, JSONObject value) {
        this.timeOut = timeOut;
        this.value = value;
    }
}

public class FileBasedStore {

    private static HashMap<String, Value> file;

    public static void createData(String key, JSONObject val) {
	
        if (file == null) {//checking if file is already created
            file = new HashMap<String, Value>();
        }

	
        if (file.containsKey(key)) { //checking if the file contains the key
            System.err.println("Error: The key " + key + " is already created");
        } else {
            if (key.length() > 32) { //check for length not greater than 32
                System.err.println("Length of the key is greater that 32");
            } else {
                if ((file.size() < (1024 * 1024 * 1024)) && (val.size() < 16 * 1024 * 1024) && key.length() <= 32) { //checking if data store file exceeded 1GB or the json file is larger than 16KB
                    int timeOut = 0;
                    Value valWithTimeout = new Value(timeOut, val);

                    file.put(key, valWithTimeout);
                } else {
                    System.err.println("Size is too big");
                }
            }
        }
    }

    //method for optional timeout parameter
    public static void createData(String key, JSONObject val, int timeOut) {
        
	if (file == null) {//checking if file is already created
            file = new HashMap<String, Value>();
        }

        if (file.containsKey(key)) { //check for length not greater than 32
            System.err.println("Error: This key " + key + " is already created");
        } else {
	    if (key.length() > 32) {
                System.err.println("Length of the key is greater that 32");
            } else {
                if ((file.size() < (1024 * 1024 * 1024)) && (val.size() < 16 * 1024 * 1024) && key.length() <= 32) { //checking if data store file exceeded 1GB or the json file is larger than 16KB
                    timeOut = (int) (Instant.now().getEpochSecond() + timeOut);
                    Value valWithTimeout = new Value(timeOut, val);
                    file.put(key, valWithTimeout);
                } else {
                    System.err.println("Size is too big");
                }
            }
        }
    }

    public static void readData(String key) {
	if (file == null) {
            System.err.println("Error: No data store is initialized");
        }

        if (file.containsKey(key)) {
            Value value = file.get(key);
            if ((value.timeOut > (int) (Instant.now().getEpochSecond())) || (value.timeOut == 0)) {
//               System.out.println(value.timeOut+" : "+Instant.now().getEpochSecond());
                System.out.println(key + " : " + value.value);
            } else {
                System.err.println("Error: Time has expired for key: " + key);
            }
        } else {
            System.err.println("Error : The key is not found in the DataStore");
        }
    }

    public static void deleteData(String key) {
	if (file == null) {
            System.err.println("Error: No data store is initialized");
        }

        if (file.containsKey(key)) {
            Value value = file.get(key);
            if ((value.timeOut < Instant.now().getEpochSecond())) {
                file.remove(key);
                System.out.println("The data with the key "+key+" has been removed");
            } else {
                System.err.println("Error: Time has expired for key: " + key);
            }
        } else {
            System.err.println("Error: Key does not Exist");
        }
    }
}
