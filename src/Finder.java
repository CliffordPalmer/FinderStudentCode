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
 * Completed by: [YOUR NAME HERE]
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";
    private static final int largePrime = 506683;
    ArrayList<Entry>[] hashMap;
    public Finder() {}

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // TODO: Complete the buildTable() function!
        hashMap = new ArrayList[largePrime + 1];
        for(int i = 0; i < largePrime; i++){
            hashMap[i] = new ArrayList<Entry>();
        }
        String line = br.readLine();
        do {
            String[] splitLine = line.split((","));
            hashMap[hash(splitLine[keyCol])].add(new Entry(splitLine[keyCol],splitLine[valCol]));
            line = br.readLine();
        }while(line != null);
        br.close();
    }

    public String query(String key){
        // TODO: Complete the query() function!
        ArrayList<Entry> overlaps = hashMap[hash(key)];
        for(Entry entry : overlaps){
            if(entry.getKey().equals(key)){
                return entry.getVal();
            }
        }
        return INVALID;
    }

    public int hash(String thingToHash){
        int hash = 0;
        for(int i = 0; i < thingToHash.length(); i++){
            // Bit shifting for speed, equivalent to multiplying by a radix of 4
            hash <<= 2;
            hash += thingToHash.charAt(i);
            hash = hash % largePrime;
        }
        return hash;
    }
}