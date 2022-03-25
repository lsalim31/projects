package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Lucas Salim
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        checkNotches(notches);
        _notches = notches;
    }

    private void checkNotches(String notches) {
        for (int i = 0; i < notches.length(); i++) {
            if (!alphabet().contains(notches.charAt(i))) {
                throw new EnigmaException("notches must be in alphabet");
            }
        }
    }

    @Override
    boolean atNotch() {
        char notch = alphabet().toChar(setting());
        return notches().indexOf(notch) != -1;
    }

    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    @Override
    String notches() {
        return _notches;
    }

    /** Store my notches. */
    private String _notches;

}
