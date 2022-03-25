package enigma;

import java.util.Collection;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Lucas Salim
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new HashMap<>(numRotors());
        storeAllRotors(allRotors);
        quickcheck();
    }

    void quickcheck() {
        if (_allRotors == null) {
            throw new EnigmaException("Collection of Rotors must be non empty");
        }
        if (_numRotors <= 1) {
            throw new EnigmaException("Number "
                    + "of rotors should be greater than 1");
        }
        if (_pawls < 0 || _pawls >= _numRotors) {
            throw new EnigmaException("Number "
                    + "of pawls should be in range [1-number of rotors)");
        }
    }

    void storeAllRotors(Collection<Rotor> allRotors) {
        for (Rotor r : allRotors) {
            _allRotors.put(r.name(), r);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return all Rotors. */
    public HashMap<String, Rotor> allRotors() {
        return _allRotors;
    }

    public HashMap<String, Rotor> myRotors() {
        return _myRotors;
    }

    public String[] myNames() {
        return _rotorsNames;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        if (k < 0 || k >= numRotors()) {
            throw new EnigmaException("index "
                    + "should be in range 0 to numRotors");
        }
        return myRotors().get(myNames()[k]);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        checkRotors(rotors);
        _myRotors = new HashMap<>(rotors.length);
        _rotorsNames = rotors;
        for (String rotorName: rotors) {
            myRotors().put(rotorName, _allRotors.get(rotorName));
            myRotors().get(rotorName).set(0);
        }
    }

    void checkRotors(String[] rotors) {
        if (!allRotors().get(rotors[0]).reflecting()) {
            throw new EnigmaException("First "
                    + "rotor must be a reflector");
        }
        for (String r: rotors) {
            if (!allRotors().containsKey(r)) {
                throw new EnigmaException("No rotor named '"
                        + r + "' in given configuration");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("setting must "
                    + "be a string of numRotors - 1");
        }
        for (int i = 1; i < myNames().length; i++) {
            char c = setting.charAt(i - 1);
            if (!alphabet().contains(c)) {
                throw new EnigmaException("character '"
                        + c + "' from setting '" + setting
                        + "' does not belong to alphabet");
            }
            myRotors().get(myNames()[i]).set(c);
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (c >= alphabet().size() || c < 0) {
            throw new EnigmaException("integer "
                    + c + " is not in range 0...size()-1 ");
        }
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    public void advanceRotors() {
        int len = myNames().length;
        boolean[] advances = new boolean[len];
        for (int i = len - numPawls(); i < len; i++) {
            if (myRotors().get(myNames()[i]).atNotch()) {
                if (!advances[i - 1]
                        && myRotors().get(myNames()[i - 1]).rotates()) {
                    myRotors().get(myNames()[i - 1]).advance();
                    myRotors().get(myNames()[i]).advance();
                    advances[i] = true;
                }
            }
        }
        if (!advances[myNames().length - 1]) {
            myRotors().get(myNames()[myNames().length - 1]).advance();
        }
    }


    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = myNames().length - 1; i >= 0; i -= 1) {
            c = myRotors().get(myNames()[i]).convertForward(c);
        }
        for (int i = 1; i < myNames().length; i++) {
            c = myRotors().get(myNames()[i]).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replace(" ", "");
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            result += alphabet().
                    toChar(convert(alphabet().toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private final int _numRotors;

    /** Number of pawls. */
    private final int _pawls;

    /** Data Structure for all my Rotors. */
    private final HashMap<String, Rotor> _allRotors;

    /** Data Structure for desired rotors. */
    private HashMap<String, Rotor> _myRotors;

    /** My plugboard. */
    private Permutation _plugboard;

    /** Data Structure for my rotors' names. */
    private String[] _rotorsNames;

}
