package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Lucas Salim
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        if (duplicates(chars)) {
            throw new EnigmaException("No character "
                   + "in Alphabet may be duplicated.");
        }
        if (chars.isEmpty()) {
            throw new EnigmaException("Alphabet must be non empty.");
        }
        _myAlphabet = chars;
        if (wrongCharacters()) {
            throw new EnigmaException("Wrong characters in Alphabet");
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    boolean wrongCharacters() {
        return (contains('*') || contains('(') || contains(')'));
    }

    /** Return trues if there are duplicates in CHARS. */
    boolean duplicates(String chars) {
        return chars.length() != chars.chars().distinct().count();
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _myAlphabet.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return (_myAlphabet.indexOf(ch) != -1);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Index must be "
                    + "between 0 and size of alphabet");
        }
        return _myAlphabet.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("Character '"
                    + ch + "' is not in Alphabet.");
        }
        return _myAlphabet.indexOf(ch);
    }

    /** Data Structure for Alphabet. */
    private final String _myAlphabet;
}
