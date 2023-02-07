import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class TestcasesRunner {

    //TODO: Add the correct endregister and maybe another, customisable startregister?

    static Runner r;

    @BeforeClass
    public void setup() {
        r = new Runner();
    }

    @Test
    public void anbn() {
        r.setFile(new File("Beispiele\\a_hoch_n_b_hoch_n.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void rekAdd() {
        r.setFile(new File("Beispiele\\AdditionRekursiv.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void rekAddKurz() {
        r.setFile(new File("Beispiele\\AdditionRekursiv-kurzVersion.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void summe() {
        r.setFile(new File("Beispiele\\Beispiel_2-1_Summe.txt"));
    }

    @Test
    public void undef() {
        r.setFile(new File("Beispiele\\Beispiel_2-2_ÜberallUndefiniert.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void wertzuweisung() {
        r.setFile(new File("Beispiele\\Beispiel_2-6_Wertzuweisung.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void whileLoop() {
        r.setFile(new File("Beispiele\\Beispiel_2-7_WHILE-Schleife.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void ifThen() {
        r.setFile(new File("Beispiele\\Beispiel_2-8_If_Then_Else.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void listenEnde() {
        r.setFile(new File("Beispiele\\Beispiel_2-9_ListenEnde-wird-gesucht.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void binbaumSuche() {
        r.setFile(new File("Beispiele\\BinärBaum-Suche.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void fakultät() {
        r.setFile(new File("Beispiele\\Fakultät.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void maximum() {
        r.setFile(new File("Beispiele\\Maximum.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }

    @Test
    public void potenz() {
        r.setFile(new File("Beispiele\\Potenz_n_hoch_m.txt"));
        ArrayList<Integer> correctEndReg = new ArrayList<Integer>();

        assertEquals(r.returnRegister(), correctEndReg);
    }




    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(TestcasesParser.class);
    }
}
