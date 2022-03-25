package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTS ***** */

    private static final Alphabet AZ = new Alphabet(TestUtils.UPPER_STRING);

    private static final HashMap<String, Rotor> ROTORS = new HashMap<>();

    static {
        HashMap<String, String> nav = TestUtils.NAVALA;
        ROTORS.put("B", new Reflector("B", new Permutation(nav.get("B"), AZ)));
        ROTORS.put("Beta",
                new FixedRotor("Beta",
                        new Permutation(nav.get("Beta"), AZ)));
        ROTORS.put("III",
                new MovingRotor("III",
                        new Permutation(nav.get("III"), AZ), "V"));
        ROTORS.put("IV",
                new MovingRotor("IV", new Permutation(nav.get("IV"), AZ),
                        "J"));
        ROTORS.put("I",
                new MovingRotor("I", new Permutation(nav.get("I"), AZ),
                        "Q"));
    }

    private static final String[] ROTORS1 = { "B", "Beta", "III", "IV", "I" };
    private static final String SETTING1 = "AXLE";

    private Machine mach1() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        mach.insertRotors(ROTORS1);
        mach.setRotors(SETTING1);
        return mach;
    }

    @Test
    public void testInsertRotors() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        mach.insertRotors(ROTORS1);
        assertEquals(5, mach.numRotors());
        assertEquals(3, mach.numPawls());
        assertEquals(AZ, mach.alphabet());
        assertEquals(ROTORS.get("B"), mach.getRotor(0));
        assertEquals(ROTORS.get("Beta"), mach.getRotor(1));
        assertEquals(ROTORS.get("III"), mach.getRotor(2));
        assertEquals(ROTORS.get("IV"), mach.getRotor(3));
        assertEquals(ROTORS.get("I"), mach.getRotor(4));
    }

    @Test
    public void testConvertChar() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(YF) (HZ)", AZ));
        assertEquals(25, mach.convert(24));
    }

    @Test
    public void testConvertMsg() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", AZ));
        assertEquals("QVPQSOKOILPUBKJZPISFXDW",
                mach.convert("FROMHISSHOULDERHIAWATHA"));
    }


    /** MY TESTS */
    private Machine myMach() {
        Alphabet alpha = new Alphabet("ABC");
        Rotor rotor0 = new Reflector("Rotor 0",
                new Permutation("(CBA)", alpha));
        Rotor rotor1 = new MovingRotor("Rotor 1",
                new Permutation("(AB)", alpha), "C");
        Rotor rotor2 = new MovingRotor("Rotor 2",
                new Permutation("(CB)", alpha), "C");
        Rotor rotor3 = new MovingRotor("Rotor 3",
                new Permutation("(BAC)", alpha), "C");
        ArrayList<Rotor> rotors = new ArrayList<Rotor>(4);
        rotors.add(rotor0);
        rotors.add(rotor1);
        rotors.add(rotor2);
        rotors.add(rotor3);
        Machine mach = new Machine(alpha, 4, 3, rotors);
        String[] arr = new String[]{"Rotor 0", "Rotor 1", "Rotor 2", "Rotor 3"};
        mach.insertRotors(arr);
        return mach;
    }

    private Machine myMach2() {
        Alphabet alpha = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Rotor rotor0 = new Reflector("B",
                new Permutation("(az) (by) (cx) (dw) (ev) "
                        + "(fu) (gt) (hs) (ir) (jq) (kp) (lo) (mn)", alpha));
        Rotor rotor1 = new MovingRotor("III",
                new Permutation("(quack) (froze) "
                        + "(twins) (glyph)", alpha), "m");
        Rotor rotor2 = new MovingRotor("II",
                new Permutation("(tears) (boing) (lucky)", alpha),
                "b");
        Rotor rotor3 = new MovingRotor("I",
                new Permutation("(wordle) (is) (fun)", alpha),
                "a");
        ArrayList<Rotor> rotors = new ArrayList<Rotor>(4);
        rotors.add(rotor0);
        rotors.add(rotor1);
        rotors.add(rotor2);
        rotors.add(rotor3);
        Machine mach = new Machine(alpha, 4, 3, rotors);
        mach.insertRotors(new String[]{"B", "III", "II", "I"});
        mach.setRotors("maa");
        mach.setPlugboard(new Permutation("(az) (mn)", alpha));
        return mach;
    }


    @Test
    public void advanceAllTest1() {
        Machine mach = mach1();
        assertEquals(0, mach.getRotor(1).setting());
        assertEquals(23, mach.getRotor(2).setting());
        assertEquals(11, mach.getRotor(3).setting());
        assertEquals(4, mach.getRotor(4).setting());
        mach.advanceRotors();
        assertEquals(0, mach.getRotor(1).setting());
        assertEquals(23, mach.getRotor(2).setting());
        assertEquals(11, mach.getRotor(3).setting());
        assertEquals(5, mach.getRotor(4).setting());
        for (int i = 0; i < 12; i++) {
            mach.advanceRotors();
        }
        assertEquals(0, mach.getRotor(1).setting());
        assertEquals(23, mach.getRotor(2).setting());
        assertEquals(12, mach.getRotor(3).setting());
        assertEquals(17, mach.getRotor(4).setting());
    }

    @Test
    public void advanceALLTest2() {
        Machine myMachine = myMach();
        assertEquals(0, myMachine.getRotor(1).setting());
        assertEquals(0, myMachine.getRotor(2).setting());
        assertEquals(0, myMachine.getRotor(3).setting());
        for (int i = 0; i < 12; i++) {
            myMachine.advanceRotors();
        }
        assertEquals(1, myMachine.getRotor(1).setting());
        assertEquals(2, myMachine.getRotor(2).setting());
        assertEquals(0, myMachine.getRotor(3).setting());
        myMachine.advanceRotors();
        assertEquals(2, myMachine.getRotor(1).setting());
        assertEquals(0, myMachine.getRotor(2).setting());
        assertEquals(1, myMachine.getRotor(3).setting());
        myMachine.advanceRotors();
        assertEquals(2, myMachine.getRotor(1).setting());
        assertEquals(0, myMachine.getRotor(2).setting());
        assertEquals(2, myMachine.getRotor(3).setting());
        myMachine.advanceRotors();
        assertEquals(2, myMachine.getRotor(1).setting());
        assertEquals(1, myMachine.getRotor(2).setting());
        assertEquals(0, myMachine.getRotor(3).setting());
    }

    @Test
    public void convertMyIDTest() {
        Machine myMach2 = myMach2();
        assertEquals("jzt", myMach2.convert("tab"));
        myMach2 = myMach2();
        assertEquals("olr", myMach2.convert("bgm"));
    }

}
