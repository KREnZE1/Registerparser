import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.assertTrue;

public class TestcasesParser {

    static Parser p;
    static final String PATHWINDOWS = "Beispiele\\";
    static final String PATHLINUX = "/home/krenze/Programming/Java/Registerparser/Beispiele/";

    @BeforeClass
    public static void setup() {
        p = new Parser();
    }

    @Test
    public void anbn() {
        String filename = "a_hoch_n_b_hoch_n.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void rekAdd() {
        String filename = "AdditionRekursiv.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void rekAddKurz() {
        String filename = "AdditionRekursiv-kurzVersion.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void summe() {
        String filename = "Beispiel_2-1_Summe.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void undef() {
        String filename = "Beispiel_2-2_ÜberallUndefiniert.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void wertzuweisung() {
        String filename = "Beispiel_2-6_Wertzuweisung.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void whileLoop() {
        String filename = "Beispiel_2-7_WHILE-Schleife.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void ifThen() {
        String filename = "Beispiel_2-8_If_Then_Else.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void listenEnde() {
        String filename = "Beispiel_2-9_ListenEnde-wird-gesucht.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void binbaumSuche() {
        String filename = "BinärBaum-Suche.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void fakultät() {
        String filename = "Fakultät.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void maximum() {
        String filename = "Maximum.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }

    @Test
    public void potenz() {
        String filename = "Potenz_n_hoch_m.txt";
        File f = null;
        f = new File(PATHWINDOWS + filename);
        if (!f.exists()) f = new File(PATHLINUX + filename);
        p.setFile(f);
        assertTrue(p.test());
    }



    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(TestcasesParser.class);
    }
}
