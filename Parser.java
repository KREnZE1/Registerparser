import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class Parser {

    BufferedReader br;
    ArrayList<String> lines;
    String[] numericKeywords = new String[] { //Keywords, which need a number afterwards
        "load",
        "store", //TODO: Normally you can't have a #0 as the param to store, needs to be fixed
        "add",
        "sub",
        "mul",
        "div"
    };
    String[] stringKeywords = new String[] { //Keywords, which need a string afterwards
        "goto",
        "jzero",
        "jnzero"
    };

    String[] otherKeywords = new String[] { //Keywords, which need nothing afterwards
        "end"
    };

    /**
     * Constructor to create a {@code Parser} without a file.
     * Creates a list to prevent errors in ther places
     */
    public Parser() {
        this.lines = new ArrayList<String>();
    }

    /**
     * Constructor to create a {@code Parser} with a file
     * @param file the file to be parsed
     */
    public Parser(File file) {
        setFile(file);
    }

    /**
     * Sets up the parsing process by accepting a file and preparing all internal values
     * @param newFile the file to be parsed
     */
    public void setFile(File newFile) {
        this.lines = new ArrayList<String>();
        translateFile(newFile);
    }

    /**
     * Checks the entire list of lines for validity and stops, as soon as one is invalid, because that invalidates the file
     * @return whether or not the analysed lines are all valid
     */
    public boolean test() {
        if (this.lines.size() == 0) return false; //An empty list can't contain a valid program
        for (String line : this.lines) { //Iterates over the lines
            if (line.length() < 3) continue; //Lines have to contain 3 or more characters to be valid, the shortest instruction is END
            if (!validLine(line)) return false; //If ny line is invalid the entire file is invalid
        }
        return true; //If this is reached, every line has been analysed and validated -> the file is valid
    }

    /**
     * Translates the text within the file into seperate lines to analyse
     * @param file a file containing text
     */
    public void translateFile(File file) {
        try {
            br = new BufferedReader(new FileReader(file));

            //Translates all lines from the file to a list
            String line = "";
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close(); //Closes the reader to prevent leaks
        } catch (IOException ioe) {
            //If the file is not found or any similar errors occur,
            // the message and stacktrace are printed to help with error finding
            System.err.println(ioe.getMessage());
            System.err.println(ioe.getStackTrace()+"\n");
        }
    }

    /**
     * A valid line is any line beginning with a comment, a valid instruction or a mark followed by a valid instruction
     * @param line
     * @return whether or not the input is a valid line
     */
    public boolean validLine(String line) {
        String[] words = removeWhitespaces(line);

        //The first word could be a comment
        if (words[0].contains("//")) return true;
        //It could also be a mark. Marks always end with : and are followed by an instruction
        if (words[0].contains(":")) return isInstruction(words, 1);
        //if it is neither, the first word should already be an instruction
        return isInstruction(words, 0);

    }

    /**
     * Removes all whitespaces (spacebar, tab) from the input so it can more easily be processed further down the code
     * @param line the line, from which all whitespaces are to be removed
     * @return the line in an array, stripped of all whitespaces
     */
    public String[] removeWhitespaces(String line) {
        char[] characters = line.toCharArray();
        int index = 0;
        while (characters[index] == ((char)9) || characters[index] == ' ') index++; //There can be an infinite amount of whitespaces before the instructions
        line = line.substring(index); //All whitespaces in the beginning of the line get removed

        String[] words = line.split("( |\t)"); //Splits the words of the line on any whitespace
        for (int i=0; i<words.length; i++) {
            //If there are multiple empty spaces or tabs after one another, this code moves them to the back of the array
            if (words[i].equals("")) {
                for (int j=i+1; j<words.length; j++) {
                    words[j-1] = words[j];
                }
                i--;
                words[words.length-1] = "-"; //and replaces them with a - to prevent loops
            }
        }
        return words; //the cleaned up line is then returned, split into the words
    }

    /**
     * Checks, whether or not the given word from the given line is a valid instruction and returns its findings
     *
     * @param words list of all words in the current line
     * @param wordPos position of the word assumed to be an instruction
     * @return whether or not the line contains an instruction
     */
    public boolean isInstruction(String[] words, int wordPos) {
        for (int i=0; i<otherKeywords.length; i++) { //Loops through all keywords, which don't need another word after them
            if (words[wordPos].toLowerCase().equals(otherKeywords[i])) { //If any equals the found instruction, the following checks,
                return endOfInstruction(words, wordPos); // if there is anything after the instruction, which shouldn't be there
            }
        }

        for (int i=0; i<numericKeywords.length; i++) { //Loops through all keywords, which need to be followed by a number
            if (words[wordPos].toLowerCase().equals(numericKeywords[i])) {
                //Check, if the next word is either *N or N, with N>0 or #N with N>=0
                String number = words[wordPos+1];
                if (number.charAt(0) == '#' && Character.isDigit(number.charAt(1)) && number.charAt(1) != '0') {
                    for (int j=2; j<number.length(); j++) if (!Character.isDigit(number.charAt(j))) return false;
                }
                else if (number.charAt(0) == '*' || Character.isDigit(number.charAt(0))) {
                    for (int j=1; j<number.length(); j++) if (!Character.isDigit(number.charAt(j))) return false;
                }
                return endOfInstruction(words, wordPos+1); //There also can't be anything not commented out after the number
            }
        }

        for (int i=0; i<stringKeywords.length; i++) { //Loops through all keywords, which need to be followed by a string
            if (words[wordPos].toLowerCase().equals(stringKeywords[i])) { //if a match is found
                if (words.length-1>wordPos && words[wordPos+1] != "-") return endOfInstruction(words, wordPos+1); //it is verified, that only one string follows the instruction
            }
        }

        return false; //If none of the previous checks were successfull, the instruction is not in the set of allowed instructions and thus invalid
    }

    /**
     * If any content (meaning another instruction-like string) is positioned after the instruction for the line has finished, the method returns false, otherwise true
     *
     * @param words contains all words in the currently checked line
     * @param lastInstruct the position of the last word used to make the instruction whole
     */
    public boolean endOfInstruction(String[] words, int lastInstruct) {
        //An instruction is valid if:
        // - the last used part of the instruction is the last word in the line
        if (words.length-1==lastInstruct) return true;
        if (words.length-1>lastInstruct) {
            // - it is only followed by empty spaces
            if (words[lastInstruct+1].equals("-")) return true;
            // - it is followed by a comment
            if (words[lastInstruct+1].contains("//")) return true;
        }
        return false; //If neither codition applied, the instruction is invalid, as the part after it can't be interpreted
    }

    /**
     * Creates a new Parser and prints out the result of whether or not the code in the file is valid
    */
    public static void main(String[] args) {
        Parser p = new Parser(new File("Beispiele\\Fakultät.txt"));
        print(p.test());
    }

    /**
     * Makes the result of the parser more accessible
     *
     * @param result Represents, whether the parser found valid code or not
     * @return No return value, because the method uses the input to display a message on the console
     */
    public static void print(boolean result) {
        if (result) System.out.println("File contains valid code");
        else System.out.println("File does not contain valid code");
    }

}