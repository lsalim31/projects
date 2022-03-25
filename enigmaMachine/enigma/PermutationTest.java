package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Lucas Salim
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkperm2Transform() {
        alpha = "MYNAPEISLUCRZ";
        perm = new Permutation("(APUCM) (YIS) (EZ)", new Alphabet(alpha));
        checkPerm("perm2", "MYNAMEISLUCAS", "AINPAZSYLCMPY");
    }

    @Test
    public void checkperm3Transform() {
        alpha = "qwertyui";
        perm = new Permutation("(qwertyui)", new Alphabet(alpha));
        checkPerm("perm3", "qwertyui",
                "wertyuiq");
    }


    @Test
    public void charPermuteTest() {
        perm = new Permutation("(ATBFG) (HIM) (LZ) (DX)", UPPER);
        assertEquals("wrong inverse translation of Y", 'Y', perm.permute('Y'));
        assertEquals("wrong inverse translation of I", 'M', perm.permute('I'));
        assertEquals("wrong inverse translation of L", 'Z', perm.permute('L'));
        assertEquals("wrong inverse translation of A", 'T', perm.permute('A'));
        assertEquals("wrong inverse translation of X", 'D', perm.permute('X'));
        assertEquals("wrong inverse translation of G", 'A', perm.permute('G'));
        assertEquals("wrong inverse translation of M", 'H', perm.permute('M'));
    }

    @Test
    public void charINVERSEPermuteTest() {
        perm = new Permutation("(ATBFG) (HIM) (LZ) (DX)", UPPER);
        assertEquals("wrong inverse translation of Y", 'Y', perm.invert('Y'));
        assertEquals("wrong inverse translation of M", 'I', perm.invert('M'));
        assertEquals("wrong inverse translation of Z", 'L', perm.invert('Z'));
        assertEquals("wrong inverse translation of T", 'A', perm.invert('T'));
        assertEquals("wrong inverse translation of D", 'X', perm.invert('D'));
        assertEquals("wrong inverse translation of A", 'G', perm.invert('A'));
        assertEquals("wrong inverse translation of H", 'M', perm.invert('H'));
    }

    @Test
    public void intINVERSEPermuteTest() {
        perm = new Permutation("(ATBFG) (HIM) (LZ) (DX)", UPPER);
        assertEquals("wrong translation of 3", 3, perm.invert(23));
        assertEquals("wrong translation of 0", 0, perm.invert(19));
        assertEquals("wrong translation of 2", 2, perm.invert(2));
        assertEquals("wrong translation of 27", 1, perm.invert(5));
        assertEquals("wrong translation of 27", 25, perm.invert(11));
    }

    @Test
    public void intPermuteTest() {
        perm = new Permutation("(ATBFG) (HIM) (LZ) (DX)", UPPER);
        assertEquals("wrong translation of 3", 23, perm.permute(3));
        assertEquals("wrong translation of 0", 19, perm.permute(0));
        assertEquals("wrong translation of 2", 2, perm.permute(2));
        assertEquals("wrong translation of 27", 5, perm.permute(27));
        assertEquals("wrong translation of 27", 11, perm.permute(-1));
    }

    @Test
    public void permuteInvertTest() {
        perm = new Permutation("(ATBFG) (HIM) (LZ) (DX)", UPPER);
        for (int index = 0; index < perm.size(); index += 1) {
            assertTrue("InversePermute should be identity",
                    index == perm.invert(perm.permute(index)));
            assertTrue("PermuteInverse should be identity",
                    index == perm.permute(perm.invert(index)));
            char c = UPPER.toChar(index);
            assertTrue("InversePermute should be identity",
                    c == perm.invert(perm.permute(c)));
            assertTrue("PermuteInverse should be identity",
                    c == perm.permute(perm.invert(c)));
        }
    }

    @Test
    public void derangementTest() {
        perm = new Permutation("(ATBFG) (HIM) (LZ) (DX)", UPPER);
        Permutation perm2 = new
                Permutation("(ACBEDGFIHKJMLONQPSRUTWVXYZ)", UPPER);
        Permutation perm3 = new
                Permutation("(ACBEDGFIHKJRU) (MLONQPS) (WVXY) (TZ)",
                UPPER);
        assertFalse(perm.derangement());
        assertTrue(perm2.derangement());
        assertTrue(perm3.derangement());
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        perm.invert('F');
        perm.invert('Z');
        perm.invert('a');
        perm.invert('b');
    }

    @Test(expected = EnigmaException.class)
    public void alphabetDuplicates() {
        perm = new Permutation("(BACD)", new Alphabet("ABCDD"));
        perm = new Permutation("(BACD)", new Alphabet("ABCDww"));
        perm = new Permutation("(BACD)", new Alphabet("xx"));
        perm = new Permutation("(BACD)",
                new Alphabet("aovnoeavnoentvapeithnvcpiahn"));
        perm = new Permutation("(BACD)", new Alphabet("1324rfqff"));
    }

    @Test(expected = EnigmaException.class)
    public void cycleNotinAlphabetTest() {
        perm = new Permutation("(BcACD)", new Alphabet("ABCD"));
        perm = new Permutation("(BAClD)", new Alphabet("ABCD"));
        perm = new Permutation("(BACD)", new Alphabet("x"));
        perm = new Permutation("(BACD)", new Alphabet("dvra"));
        perm = new Permutation("(BACD)", new Alphabet("132"));
    }

    @Test
    public void addCycleTest() {
        perm = new Permutation("(AB)", new Alphabet("ABCD"));
        assertNotEquals('D', perm.permute('C'));
        perm.addCycle("(CD)");
        assertEquals('D', perm.permute('C'));
    }


}
