# Enigma Machine
## What it is?
A generalized version of the famous [Enigma machine](https://en.wikipedia.org/wiki/Enigma_machine), used in Wolrd War II. 
The purpose is to be able to encode (and decode) any message using the logic from Enigma. 

## How to use it?
After compiling, you can run the program using 
```
java -ea enigma.Main [configuration file] [input file] [output file]
```
The program takes in (respectively) two (or three) files: **.conf**, **.in**, and **.out** (optional). 
The **.conf** file contains the configuration of our machine, and the available rotors to be used.
The **.in** file consists of a sequence of messages to decode, each preceded by a line that gives us the initial settings to our machine.
The **.out** file is where the output is printed. That is where we can see our decoded message given in the input file. If there is no **.out** file, the output appears in the command line.

## Content

### Alphabet
Class that defines an alphabet for our machine.

### Permutation
Class that defines the permutations for our machines given an **Alphabet**. We use [cycle notation](https://en.wikipedia.org/wiki/Permutation#Cycle_notation) to represent such permutations.

### Rotor
Class that defines the [rotors](https://en.wikipedia.org/wiki/Enigma_machine#Rotors) for our machine. A rotor can be a **Fixed Rotor**, a **MovingRotor**, or a **Reflector**. Each having its own **Permutation**, with an common **Alphabet**.

### Machine
Class that defines our machine. This takes in a set of **Rotors**, plus a **Plugboard** (which acts as a non-moving, configurable **Rotor**).

## Example
We can run the program with the following files:

#### default.conf
```
ABCDEFGHIJKLMNOPQRSTUVWXYZ
 5 3
 I MQ      (AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)
 II ME     (FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)
 III MV    (ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)
 IV MJ     (AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)
 V MZ      (AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)
 VI MZM    (AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK) 
 VII MZM   (ANOUPFRIMBZTLWKSVEGCJYDHXQ) 
 VIII MZM  (AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)
 Beta N    (ALBEVFCYODJWUGNMQTZSKPR) (HIX)
 Gamma N   (AFNIRLBSQWVXGUZDKMTPCOYJHE)
 B R       (AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP)
           (RX) (SZ) (TV)
 C R       (AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW)
           (QZ) (SX) (UY)
```

#### 00-trivial.in
```
* B Beta I II III AAAA
HELLO WORLD
* B Beta I II III AAAA
ILBDA AMTAZ
```

After running the program with 
```
java -ea enigma.Main default.conf 00-trivial.in
```
We get the following output:
```
ILBDA AMTAZ
HELLO WORLD
```
Notice that "HELLO WORLD" is translated to "ILBDA AMTAZ" and vice-versa ("ILBDA AMTAZ" is trasnlated to "HELLO WORLD"). This is because the Engima algorithms were reciprocal, meaning that encryption is its own inverse operation.



