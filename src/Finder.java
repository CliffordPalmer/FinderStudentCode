import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Clifford Palmer
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";
    private static final double LOAD_FACTOR = 0.5;
    // Starting size experimentally determined. Found that bigger starting sizes always mean shorter run time,
    // tested up to 5000000, but with diminishing returns. 16001 is a good balance between speed and space;
    private static final int TABLE_STARTING_SIZE = 16001;
    private static Entry[] hashMap;
    public Finder() {}

    // Helper function for main recursive buildTable function which passes in the necessary variables
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
       hashMap = buildTable(TABLE_STARTING_SIZE, br, new Entry[0], keyCol, valCol);
    }

    // Main recursive function
    public Entry[] buildTable(int tableSize, BufferedReader br, Entry[] oldTable, int keyCol, int valCol) throws IOException{
        Entry[] newTable = new Entry[tableSize];
        // Variables to ensure the load doesn't exceed a load factor of 0.5
        int allowableSize = (int)(tableSize * LOAD_FACTOR);
        int currentSize = 0;
        // Rehashes every entry that's already been added
        for(int i = 0; i < oldTable.length; i++){
            // Searches for non-null entries
            if(oldTable[i] != null){
                // Rehash
                int hash = hash(oldTable[i].getKey(), tableSize);
                // Adjust index to the next empty slot
                while(newTable[hash] != null){
                    hash++;
                    if(hash >= tableSize){
                        hash = 0;
                    }
                }
                // Assign entry to slot
                newTable[hash] = oldTable[i];
                // Keep track of load on map
                currentSize++;
            }
        }
        // Adds more entries until load factor is reached
        for(int i = currentSize; i < allowableSize; i++){
            String nextLine = br.readLine();
            // Effectively the base case for this function. Just returns the table if there are no more elements to add
            if(nextLine == null){
                return newTable;
            }
            // Get the key and value of the entry
            String[] splitLine = nextLine.split(",");
            String key = splitLine[keyCol];
            String val = splitLine[valCol];
            // Hash key and search for next empty slot
            int hash = hash(key, tableSize);
            while(newTable[hash] != null){
                hash++;
                if(hash >= tableSize){
                    hash = 0;
                }
            }
            newTable[hash] = new Entry(key, val);
        }
        // Recursively call the function again to resize the map
        return buildTable(tableSize * 2, br, newTable, keyCol, valCol);
    }

    public String query(String key){
        int hash = hash(key, hashMap.length);
        // Search for a match, or an empty space is reached
        while(hashMap[hash] != null){
            if(!hashMap[hash].getKey().equals(key)){
                hash++;
                if(hash >= hashMap.length){
                    hash = 0;
                }
            }
            else{
                break;
            }
        }
        // If the space is empty, the key is invalid
        if(hashMap[hash] == null){
            return "INVALID KEY";
        }
        // Return the value associated with the key
        return hashMap[hash].getVal();
    }

    public int hash(String thingToHash, int modulus){
        int hash = 0;
        // Loop through String, hashing one letter at a time and multiplying by a radix
        for(int i = 0; i < thingToHash.length(); i++){
            // Bit shifting for speed, equivalent to multiplying by a radix of 256
            hash <<= 8;
            hash += thingToHash.charAt(i);
            hash = hash % modulus;
        }
        return hash;
    }
}