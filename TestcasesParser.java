import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.assertTrue;

public class TestcasesParser {

    static Parser p;

    @BeforeClass
    public static void setup() {
        p = new Parser();
    }

    @Test
    public void anbn() {
        p.setFile(new File("Beispiele\\a_hoch_n_b_hoch_n.txt"));
        assertTrue(p.test());
    }

    @Test
    public void rekAdd() {
        p.setFile(new File("Beispiele\\AdditionRekursiv.txt"));
        assertTrue(p.test());
    }

    @Test
    public void rekAddKurz() {
        p.setFile(new File("Beispiele\\AdditionRekursiv-kurzVersion.txt"));
        assertTrue(p.test());
    }

    @Test
    public void summe() {
        p.setFile(new File("Beispiele\\Beispiel_2-1_Summe.txt"));
    }

    @Test
    public void undef() {
        p.setFile(new File("Beispiele\\Beispiel_2-2_ÜberallUndefiniert.txt"));
        assertTrue(p.test());
    }

    @Test
    public void wertzuweisung() {
        p.setFile(new File("Beispiele\\Beispiel_2-6_Wertzuweisung.txt"));
        assertTrue(p.test());
    }

    @Test
    public void whileLoop() {
        p.setFile(new File("Beispiele\\Beispiel_2-7_WHILE-Schleife.txt"));
        assertTrue(p.test());
    }

    @Test
    public void ifThen() {
        p.setFile(new File("Beispiele\\Beispiel_2-8_If_Then_Else.txt"));
        assertTrue(p.test());
    }

    @Test
    public void listenEnde() {
        p.setFile(new File("Beispiele\\Beispiel_2-9_ListenEnde-wird-gesucht.txt"));
        assertTrue(p.test());
    }

    @Test
    public void binbaumSuche() {
        p.setFile(new File("Beispiele\\BinärBaum-Suche.txt"));
        assertTrue(p.test());
    }

    @Test
    public void fakultät() {
        p.setFile(new File("Beispiele\\Fakultät.txt"));
        assertTrue(p.test());
    }

    @Test
    public void maximum() {
        p.setFile(new File("Beispiele\\Maximum.txt"));
        assertTrue(p.test());
    }

    @Test
    public void potenz() {
        p.setFile(new File("Beispiele\\Potenz_n_hoch_m.txt"));
        assertTrue(p.test());
    }



    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(TestcasesParser.class);
    }
}
