/*
 * For CSCI 211 Assignment: Count the Occurence of Key Word in a Java Text File
 * Student: Huajing Lin
 * date: 11/19/2017
 */
package countkeywords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountKeyWords {

    StringBuffer buffer = null; //The contents of the file into a buffer string
    HashMap<String, Integer> map = null;
    List<Map.Entry<String, Integer>> list = null;//Store key-value pairs

    //load Java file
    public boolean loadJavaFile() {
        String line = null;
        try {
            //Import the text in the file into memory
            BufferedReader reader
                    = new BufferedReader(new FileReader("CityProject.java"));
            //Define a string buffer object
            buffer = new StringBuffer();

            //put text into StringBuffer
            int n = -1;
            while ((line = reader.readLine()) != null) {
                //find string "//" and delete text behind it
                n = line.indexOf("//");
                if (n > -1) {
                    //System.out.println(line.substring(n));
                    line = line.substring(0, n);
                }
                buffer.append(line);
            }
            reader.close();// Close the input stream
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //clean comments and String Literal  
    public void cleanCommentsString() {

        // convert character of StringBuffer to a string
        String string = buffer.toString();

        //Define a regular expression to delete text between two double quotes
        Pattern p = Pattern.compile("\"(.*?)\"");
        Matcher m = p.matcher(string);
        String delStr;
        while (m.find()) {
            //System.out.println(m.group());
            delStr = m.group();
            string = string.replace(delStr, "");
        }
        //System.out.println(string);

        //System.out.println("================================");
        //Define a regular expression to delete text between /* and */
        p = Pattern.compile("/\\*{1,2}[\\s\\S]*?\\*/");
        m = p.matcher(string);
        while (m.find()) {
            //System.out.println(m.group());
            delStr = m.group();
            string = string.replace(delStr, "");
        }

        //new buffer, after cleaning
        buffer = new StringBuffer(string);
    }

    //put all the java key words to hashmap
    public void putJavaKeyWords() {
        String[] javaKeyWords = {"abstract", "continue", "for", "new",
            "assert", "default", "package", "synchronized",
            "boolean", "do", "if", "private", "this",
            "break", "double", "implements", "protected", "throw",
            "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "float", "native", "super", "while", "switch"};

        if (map == null) {
            return;
        }
        for (int i = 0; i < javaKeyWords.length; i++) {
            map.put(javaKeyWords[i], 0);
        }
    }

    /* get the words in java file
     * to count the key word */
    public void matchWord() {
        String word = "";//the key of map;
        int times = 0; //the values of map

        //define the regular expression get matching word
        Pattern expression = Pattern.compile("([a-zA-Z]+)");

        // convert StringBuffer to string
        String string = buffer.toString();
        Matcher matcher = expression.matcher(string);

        while (matcher.find()) {// Start matching
            word = matcher.group();
            if (map.containsKey(word)) {// If it includes the key
                times = map.get(word);  // Get the number of times the word appears
                map.put(word, times + 1);
                //System.out.printf("matcher word: %s\n", word);
            }
        }
        //Put in the array to prepare for sorting
        list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
    }

    public void SortAndPrint() {
        
        //sort by value
        Collections.sort(list, new ValuesComparator());// sort by the pattern
        
        String key;
        Integer value = 0;
        System.out.println("    Key word \t times");
        for (int i = list.size() - 1; i > 0; i--) {

            key = list.get(i).getKey();     // key
            value = list.get(i).getValue(); // values
            
            System.out.printf("%12s \t %s \n",key, value);
            
        }
        
    }

    public static void main(String[] args) {

        CountKeyWords ckw = new CountKeyWords();
        ckw.loadJavaFile();
        ckw.cleanCommentsString();
        ckw.map = new HashMap<String, Integer>();
        ckw.putJavaKeyWords();
        ckw.matchWord();
        ckw.SortAndPrint();
    }

}

//for the comparison of hashmap by value
class ValuesComparator implements Comparator<Map.Entry<String, Integer>> {

    public int compare(Map.Entry<String, Integer> left, Map.Entry<String, Integer> right) {
        if (left.getValue().equals(right.getValue())) {
            return 0;
        } else if ((left.getValue()) < (right.getValue())) {
            return -1;
        } else {
            return 1;
        }
    }
}
