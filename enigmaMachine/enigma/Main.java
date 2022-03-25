package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;


import static enigma.EnigmaException.error;

/** Enigma simulator.
 *  @author Lucas Salim
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Returns _config. */
    public Scanner config() {
        return _config;
    }

    /** Returns _input. */
    public Scanner input() {
        return _input;
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        ArrayList<String> lines = new ArrayList<>();
        String message;
        Machine machine = readConfig();
        try {
            String line = "";
            if (!input().hasNext("\\*")) {
                throw new EnigmaException("input "
                        + "file must start with '*', i.e., a setting");
            }
            while (input().hasNextLine()) {
                applySettingLine(machine, line);
                while (!input().hasNext("\\*") && input().hasNextLine()) {
                    message = input().nextLine();
                    lines.add(message);
                }
                for (String msg: lines) {
                    printMessageLine(machine.convert(msg));
                }
                if (input().hasNextLine()) {
                    line = input().nextLine();
                    if (line.equals("")) {
                        _output.println();
                    }
                }
                lines = new ArrayList<>();
            }
        } catch (EnigmaException e) {
            throw e;
        }
    }


    private void applySettingLine(Machine mach, String line) {
        String settingLine;
        if (line.equals("")) {
            input().next();
            settingLine = input().nextLine();
        } else {
            settingLine = line.substring(1, line.length());
        }
        ArrayList<String> info = new ArrayList<>();
        Scanner lineScanner = new Scanner(settingLine);
        for (int i = 0; i < mach.numRotors(); i++) {
            if (!lineScanner.hasNext()) {
                throw new EnigmaException("bad "
                        + "configuration on setting line: "
                        + "*" + settingLine);
            }
            info.add(lineScanner.next());
        }
        if (!lineScanner.hasNext()) {
            throw new EnigmaException("bad "
                    + "configuration on setting line: "
                    + "*" + settingLine);
        }
        String setting = lineScanner.next();
        String[] holder = new String[info.size()];
        String[] names = info.subList(0, info.size()).toArray(holder);
        if (Arrays.stream(names).distinct().count() != names.length) {
            throw new EnigmaException("duplicated "
                    + "rotor from setting line: " + "*" + settingLine);
        }
        mach.insertRotors(names);
        setUp(mach, setting);
        if (settingLine.indexOf('(') != -1) {
            Permutation plugboard = new Permutation(lineScanner.nextLine(),
                    _alphabet);
            mach.setPlugboard(plugboard);
        } else {
            Permutation plugboard = new Permutation("", _alphabet);
            mach.setPlugboard(plugboard);
        }
    }









    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(config().next());
            int numRotors = config().nextInt();
            int numPawls = config().nextInt();
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (config().hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = config().next();
            String info = config().next();
            char type = info.charAt(0);
            Permutation perm = new Permutation(config().nextLine(), _alphabet);
            while (config().hasNext("\\(.*\\)")) {
                perm.addCycle(config().nextLine());
            }
            switch (type) {
            case 'M':
                String notches = info.substring(1);
                return new MovingRotor(name, perm, notches);
            case 'N':
                return new FixedRotor(name, perm);
            case 'R':
                return new Reflector(name, perm);
            default:
                throw new EnigmaException("Wrong type of Rotor: " + type);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int groupsNum = msg.length() / 5;
        int remainder = msg.length() % 5;
        for (int i = 0; i < msg.length() + 1; i += 5) {
            if (i < 5 * groupsNum) {
                String out = msg.substring(i, i + 5) + " ";
                _output.print(out);
            }
            if (i == 5 * groupsNum) {
                String out = msg.substring(i, i + remainder) + "\n";
                _output.print(out);
            }
            if (i > 5 * groupsNum) {
                String out = "\n";
                _output.print(out);
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private final Scanner _input;

    /** Source of machine configuration. */
    private final Scanner _config;

    /** File for encoded/decoded messages. */
    private final PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
