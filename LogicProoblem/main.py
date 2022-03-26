# The main file for our program. 
import sys
import math
from compoundProp import display, getSentence

# argv contains the list of Trues and Falses to be used as input. 
# The length of the list should awlays be of the form 2^k for some 
# integer k. For cases of two propositional variables, we have the 
# option to include --display after our input, which displays the 
# truth table as the output.  
def main(argv):
    comb = argv[1]
    assert math.log2(len(comb)) % 1 == 0
    if len(argv) > 2:
        optional = argv[2]
        if optional == "--display":
            print(display(comb))
    print(getSentence([char for char in argv[1]]))

if __name__ == "__main__":
    main(sys.argv)




