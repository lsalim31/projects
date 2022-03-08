package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Rotor class and subclasses.
 *  @author Lucas Salim
 */
public class RotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }
    private void setMovingRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
    }

    private void setFixedRotor(String name, HashMap<String, String> rotors) {
        rotor = new FixedRotor(name, new Permutation(rotors.get(name), UPPER));
    }

    private void setRelflector(String name, HashMap<String, String> rotors) {
        rotor = new Reflector(name, new Permutation(rotors.get(name), UPPER));
    }

    @Test
    public void checkMovingRotor() {
        rotor = new MovingRotor("MyMovingRotor",
                new Permutation("(BDC)",
                        new Alphabet("ABCD")), "");
        rotor.set(2);
        checkRotor("Rotor MyRotor", "ABCD", "DACB");
    }

    @Test
    public void checkMovingRotor2() {
        rotor = new MovingRotor("MyFixedRotor",
                new Permutation("(BDC)",
                        new Alphabet("ABCD")), "");
        rotor.advance();
        rotor.advance();
        checkRotor("Rotor MyRotor", "ABCD", "DACB");
    }


    @Test
    public void checkFixedRotor() {
        rotor = new FixedRotor("MyFixedRotor",
                new Permutation("(BDC)",
                        new Alphabet("ABCD")));
        rotor.set(2);
        checkRotor("Rotor MyRotor", "ABCD", "DACB");
    }


    @Test(expected = EnigmaException.class)
    public void checkFixed() {
        rotor = new FixedRotor("MyMovingRotor",
                new Permutation("(BDAC)",
                        new Alphabet("ABCD")));
        rotor.advance();
    }

    @Test(expected = EnigmaException.class)
    public void checkReflector() {
        rotor = new Reflector("MyMovingRotor",
                new Permutation("(BDA)",
                        new Alphabet("ABCD")));
    }
    @Test(expected = EnigmaException.class)
    public void checkReflector2() {
        rotor = new Reflector("MyMovingRotor",
                new Permutation("(BDAC)",
                        new Alphabet("ABCD")));
        rotor.advance();
    }
    @Test(expected = EnigmaException.class)
    public void checkReflector3() {
        rotor = new Reflector("MyMovingRotor",
                new Permutation("(BDAC)",
                        new Alphabet("ABCD")));
        rotor.set(13);
    }



}
