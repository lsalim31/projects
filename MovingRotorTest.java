package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Lucas Salim
 */
public class MovingRotorTest {

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

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkConvertForward() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
        assertEquals(alpha.indexOf('P'),
                rotor.convertForward(alpha.indexOf('T')));
        assertEquals(alpha.indexOf('A'),
                rotor.convertForward(alpha.indexOf('U')));
        assertEquals(alpha.indexOf('S'),
                rotor.convertForward(alpha.indexOf('S')));
    }

    @Test
    public void checkConvertBackward() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
        assertEquals(alpha.indexOf('T'),
                rotor.convertBackward(alpha.indexOf('P')));
        assertEquals(alpha.indexOf('U'),
                rotor.convertBackward(alpha.indexOf('A')));
        assertEquals(alpha.indexOf('S'),
                rotor.convertBackward(alpha.indexOf('S')));
    }


    /** My tests */

    @Test
    public void checkRotors() {
        setRotor("II", NAVALA, "");
        checkRotor("Rotor II", UPPER_STRING, NAVALA_MAP.get("II"));
        setRotor("III", NAVALA, "");
        checkRotor("Rotor III", UPPER_STRING, NAVALA_MAP.get("III"));
        setRotor("IV", NAVALA, "");
        checkRotor("Rotor IV", UPPER_STRING, NAVALA_MAP.get("IV"));
        setRotor("V", NAVALA, "");
        checkRotor("Rotor V", UPPER_STRING, NAVALA_MAP.get("V"));
        setRotor("VI", NAVALA, "");
        checkRotor("Rotor VI", UPPER_STRING, NAVALA_MAP.get("VI"));
        setRotor("VII", NAVALA, "");
        checkRotor("Rotor VII", UPPER_STRING, NAVALA_MAP.get("VII"));
        setRotor("Beta", NAVALA, "");
        checkRotor("Rotor Beta", UPPER_STRING, NAVALA_MAP.get("Beta"));
        setRotor("Gamma", NAVALA, "");
        checkRotor("Rotor Gamma", UPPER_STRING, NAVALA_MAP.get("Gamma"));
    }


    @Test
    public void checkRotorsAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
        setRotor("II", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor II advanced", UPPER_STRING, NAVALB_MAP.get("II"));
    }

    @Test
    public void checkMyConvertForward() {
        setRotor("II", NAVALA, "");
        checkRotor("Rotor II (A)", UPPER_STRING, NAVALA_MAP.get("II"));
        assertEquals(alpha.indexOf('Y'),
                rotor.convertForward(alpha.indexOf('V')));
        assertEquals(alpha.indexOf('T'),
                rotor.convertForward(alpha.indexOf('N')));
        assertEquals(alpha.indexOf('E'),
                rotor.convertForward(alpha.indexOf('Z')));
    }



}
