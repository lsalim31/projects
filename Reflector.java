package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Lucas Salim
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        checkderangement();
    }

    /** Check if permutation is derangement. */
    void checkderangement() {
        if (!permutation().derangement()) {
            throw new EnigmaException("Reflector's permutation "
                    + "must be a derangement");
        }
    }

    /** Return true iff I reflect. */
    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void advance() {
        throw new EnigmaException("Reflector does not advance");
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw new EnigmaException("reflector has only one position");
        }
    }

    @Override
    void set(char posn) {
        permutation().checkChar(posn, alphabet());
        if (alphabet().toInt(posn) != 0) {
            throw new EnigmaException("reflector has only one position");
        }
    }

    @Override
    int convertBackward(int e) {
        throw new EnigmaException("reflector does not have inverses");
    }

}
