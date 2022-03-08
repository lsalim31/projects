package enigma;


import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Lucas Salim
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        checksubANDduplicates();
    }

    /** Characters from cycles should form a subset of those from alphabet,
     * and they must not be duplicates. */
    private void checksubANDduplicates() {
        String rawCycles = "";
        for (int i = 0; i < _cycles.length(); i += 1) {
            char c = _cycles.charAt(i);
            if (c != '(' && c != ')' && c != ' ') {
                rawCycles += c;
                if (!_alphabet.contains(c)) {
                    throw new EnigmaException("Every "
                           + "character in cycles must be in alphabet.");
                }
            }
            if (rawCycles.length() != rawCycles.chars().distinct().count()) {
                throw new EnigmaException("Cycles cannot have duplicates.");
            }
        }
    }


    /** Check if character C belongs to ALPHABET. */
    public void checkChar(char c, Alphabet alphabet) {
        if (!alphabet.contains(c)) {
            throw new EnigmaException("Character must be in alphabet.");
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        _cycles = _cycles.concat(" " + cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int P = wrap(p);
        char ch = alphabet().toChar(P);
        return alphabet().toInt(permute(ch));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int curr = permute(c);
        int prev = c;
        while (curr != (wrap(c))) {
            prev = permute(prev);
            curr = permute(curr);
        }
        return prev;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        checkChar(p, alphabet());
        if (_cycles.length() == 0) {
            return p;
        }
        int index = _cycles.indexOf(p);
        if (index == -1) {
            return p;
        }
        char myChar = _cycles.charAt(index + 1);
        if (!alphabet().contains(myChar)) {
            while (myChar != '(') {
                index -= 1;
                myChar = _cycles.charAt(index);
            }
            myChar = _cycles.charAt(index + 1);
        }
        return myChar;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        checkChar(c, alphabet());
        return alphabet().toChar(invert(alphabet().toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int index = 0; index < size(); index += 1) {
            if (index == permute(index)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String _cycles;
}
