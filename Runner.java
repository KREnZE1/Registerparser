import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

public class Runner {

    static char WHITESPACE = (char) 26;
    ArrayList<String> lines; // Speichert alle Zeilen des Programms
    ArrayList<Integer> register; // Beinhaltet das Register, auf dem das Registermaschinenprogramm operiert
    HashMap<String, Integer> marks; // Beinhaltet alle Marken, zu denen das Registermaschinenprogramm springen kann

    public Runner() {
        this.lines = new ArrayList<String>();
        this.register = new ArrayList<Integer>();
        // this.register.add(0);
        this.marks = new HashMap<String, Integer>();
    }

    public Runner(File file) {
        this.lines = new ArrayList<String>();
        this.register = new ArrayList<Integer>();
        // this.register.add(0);
        translateFile(file);
    }

    public void setFile(File file) {
        translateFile(file);
    }

    private void translateFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                this.lines.add(line);
            }

            br.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            System.err.println(ioe.getStackTrace() + "\n");
        }
    }

    public void run() {
        removeUnnecessaryLines();
        createMarkList();
        clean();
        execute();
    }

    private void removeUnnecessaryLines() {
        for (int i=0; i<lines.size(); i++) {
            if (lines.get(i).length() < 3) {
                lines.remove(lines.get(i)); // Zeilen, die weniger als 3 Zeichen beinhalten werden gelöscht
                i--;
            }
            else {
                // Zeilen, die mit Kommentaren anfangen, werden gelöscht
                String[] words = lines.get(i).split("( |\t)");
                for (String word : words) {
                    if (word.length()>0) {
                        if (!word.startsWith("//")) break; // Wenn das Wort kein Leerzeichen ist und kein Kommentar ist wird die Zeile akzeptiert
                        else { // Wenn das Wort stattdessen ein Kommentar ist wird die Zeile entfernt
                            lines.remove(lines.get(i));
                            i--;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void createMarkList() {
        for (int i = 0; i < this.lines.size(); i++) {
            String[] words = this.lines.get(i).split("( |\t)");
            if (words[0].contains(":")) {
                this.marks.put(words[0].substring(0, words[0].length()-1), i);

                //Wenn eine Marke gefunden wird soll diese entfernt werden
                String noMarkLine = "";
                for (int j=1; j<words.length; j++) noMarkLine += words[j]+" "; //Alle Wörter nach dem Namen der Marke werden aneinandergehängt
                this.lines = insertItemAtPos(i, this.lines, noMarkLine); //Und an der Originalposition der Zeile der Marke gespeichert
            }
        }
    }

    private void clean() {
        for (int i=0; i<this.lines.size(); i++) {
            char[] letters = this.lines.get(i).toCharArray();
            boolean allowed = false; //Nach jedem Buchstaben darf einmal ein Leerzeichen vorkommen, danach nicht mehr
            for (int j=0; j<letters.length; j++) {
                if (letters[j] == WHITESPACE) break; //Internes Repräsentationssymbol für Whitespace-Charaktere
                if (!allowed && Character.isWhitespace(letters[j])) {
                    letters[j] = WHITESPACE; //Ändert den Whitespace-Charakter zum internen Symbol
                    for (int k=j; k<letters.length-1; k++) { //Und tauscht den Whitespace-Charakter bis zum Ende der Eingabe
                        if (letters[k+1] != WHITESPACE) {
                            char temp = letters[k+1];
                            letters[k+1] = letters[k];
                            letters[k] = temp;
                        } else break;
                    }
                    j--; //Das Array wurde verändert, die momentane Zeile muss erneut überprüft werden
                }
                else if (!Character.isWhitespace(letters[j])) allowed = true; //Auf einen Buchstaben darf wieder ein Leerzeichen folgen
                else if (allowed) allowed = false; //Nach einem Leerzeichen ist kein weiteres erlaubt
            }

            //Fügt den gesäuberten String wieder an der Originalposition hinzu
            String str = "";
            for (int j=0; j<letters.length; j++) {
                if (letters[j] == WHITESPACE) break;
                str += letters[j];
            }
            this.lines = insertItemAtPos(i, this.lines, str);
        }
    }

    private void execute() {
        // An dieser Stelle sollten nur noch die Anweisungen mit ihren Parametern in lines gespeichert sein
        // Kommentare befinden sich lediglich hinter den Anweisungen
        // Zwischen jedem Wort befindet sich genau 1x ' '
        for (int i=0; i<this.lines.size(); i++) {
            String[] words = this.lines.get(i).split("( |\t)");
            // words[0] == Anweisung
            switch (words[0].toLowerCase()) {
                case "end":
                    return; // end beendet das Registermaschinenprogramm
                case "store":
                    storeInstruction(words[1]);
                    break;
                case "load":
                    loadInstruction(words[1]);
                    break;
                case "add":
                    addInstruction(words[1]);
                    break;
                case "sub":
                    subtractInstruction(words[1]);
                    break;
                case "mul":
                    multiplyInstruction(words[1]);
                    break;
                case "div":
                    divisionInstruction(words[1]);
                    break;
                case "goto":
                    i = goToInstruction(words[1], i)-1;
                    break;
                case "jzero":
                    i = jumpIfZeroInstruction(words[1], i)-1;
                    break;
                case "jnzero":
                    i = jumpIfNotZeroInstruction(words[1], i)-1;
                    break;

                default:
                    System.err.println("Der Befehl "+words[0].toLowerCase()+" wurde nicht erkannt.");
                    return;
            }
        }
    }

    private <T> ArrayList<T> insertItemAtPos(int pos, ArrayList<T> list, T val) {
        list.add(pos, val);
        list.remove(pos+1);
        return list;
    }

    private int fetch(int index) {
        while (this.register.size()<=index) {
            this.register.add(0);
        }
        return this.register.get(index);

    }

    //#region Instructions

    private void storeInstruction(String param) {
        //TODO: targetReg darf nicht 0 sein
        int targetReg = 0;
        if (Character.isDigit(param.charAt(0))) targetReg = Integer.parseInt(param);
        else if (param.charAt(0) == '*') targetReg = fetch(Integer.parseInt(param.substring(1)));

        fetch(targetReg);
        this.register = insertItemAtPos(targetReg, this.register, fetch(0));
    }

    private void loadInstruction(String param) {
        int loadedNum = 0;
        if (Character.isDigit(param.charAt(0))) loadedNum = fetch(Integer.parseInt(param));
        else if (param.charAt(0) == '#') loadedNum = Integer.parseInt(param.substring(1));
        else if (param.charAt(0) == '*') loadedNum = fetch(fetch(Integer.parseInt(param.substring(1))));

        fetch(0);
        this.register = insertItemAtPos(0, this.register, loadedNum);
    }

    private void addInstruction(String param) {
        int addedNum = 0;
        if (Character.isDigit(param.charAt(0))) addedNum = fetch(Integer.parseInt(param));
        else if (param.charAt(0) == '#') addedNum = Integer.parseInt(param.substring(1));
        else if (param.charAt(0) == '*') addedNum = fetch(fetch(Integer.parseInt(param.substring(1))));

        addedNum += fetch(0);
        this.register = insertItemAtPos(0, this.register, addedNum);
    }

    private void subtractInstruction(String param) {
        int subtractedNum = 0;
        if (Character.isDigit(param.charAt(0))) subtractedNum = fetch(Integer.parseInt(param));
        else if (param.charAt(0) == '#') subtractedNum = Integer.parseInt(param.substring(1));
        else if (param.charAt(0) == '*') subtractedNum = fetch(fetch(Integer.parseInt(param.substring(1))));

        subtractedNum = fetch(0) - subtractedNum;
        subtractedNum = (subtractedNum<0 ? 0 : subtractedNum); // Registermaschinen arbeiten ausschließlich mit natürlichen Zahlen
        this.register = insertItemAtPos(0, this.register, subtractedNum);
    }

    private void multiplyInstruction(String param) {
        int multipliedNum = 0;
        if (Character.isDigit(param.charAt(0))) multipliedNum = fetch(Integer.parseInt(param));
        else if (param.charAt(0) == '#') multipliedNum = Integer.parseInt(param.substring(1));
        else if (param.charAt(0) == '*') multipliedNum = fetch(fetch(Integer.parseInt(param.substring(1))));

        multipliedNum *= fetch(0);
        this.register = insertItemAtPos(0, this.register, multipliedNum);
    }

    private void divisionInstruction(String param) {
        int dividedNum = 0;
        if (Character.isDigit(param.charAt(0))) dividedNum = fetch(Integer.parseInt(param));
        else if (param.charAt(0) == '#') dividedNum = Integer.parseInt(param.substring(1));
        else if (param.charAt(0) == '*') dividedNum = fetch(fetch(Integer.parseInt(param.substring(1))));

        dividedNum = (int) Math.floor(fetch(0)/dividedNum);
        this.register = insertItemAtPos(0, this.register, dividedNum);
    }

    private int goToInstruction(String param, int currLine) {
        for (String key : this.marks.keySet()) {
            if (key.equals(param)) return this.marks.get(key);
        }
        return currLine+1;
    }

    private int jumpIfZeroInstruction(String param, int currLine) {
        if (fetch(0)==0) {
            return goToInstruction(param, currLine);
        }
        return currLine+1;
    }

    private int jumpIfNotZeroInstruction(String param, int currLine) {
        if (fetch(0) > 0) {
            return goToInstruction(param, currLine);
        }
        return currLine+1;
    }

    //#endregion

    public void printCode() {
        for (String line : this.lines) {
            System.out.println(line);
        }
    }

    public void printMarkList() {
        for (String key : this.marks.keySet()) {
            System.out.println("The mark "+key+" is in line "+this.marks.get(key));
        }
    }

    private void printRegister() {
        for (int i=0; i<this.register.size(); i++) {
            System.out.println("R("+i+") = "+this.register.get(i));
        }
    }

    public ArrayList<Integer> returnRegister() {
        return this.register;
    }

    public static void main(String[] args) {
        Runner r = new Runner();
        r.setFile(new File("Beispiele\\a_hoch_n_b_hoch_n.txt"));
        ArrayList<Integer> startReg = new ArrayList<Integer>();
        r.run();
        // r.printCode();
        // System.out.print("\n");
        // r.printMarkList();
        // System.out.print("\n");
        r.printRegister();
    }
}

// TODO: Alle Beispielprogramme durchgehen, um Fehler zu finden
// TODO: Entweder eine Möglichkeit hinzufügen, das Startregister anzugeben oder bei allen Beispielen das Startregister ergänzen
