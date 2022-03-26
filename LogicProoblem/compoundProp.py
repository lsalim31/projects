
import math

# Get the positions of False from list L.
def getFpositions(l):
	i = []
	for v in range(0,len(l)):
		if l[v] == 'F':
			i.append(v+1)
		else:
			continue
	return i

# Return the negation or the empty symbol based on I, J, N.
def N(i,j,n):
	for k in range(0,(2**n)):
		if (2**(n-j)*k + 1) <= i and (2**(n-j)*(k+1)) >= i:
			if k%2 == 0:
				return "¬"
				break
			elif k%2 == 1:
				return ""
				break
		else:
			continue


# Return the desired sentence in propositional logic language
# given the input L as the list of Trues and Falses.
def getSentence(l):
	if len(getFpositions(l)) == 0:
		return "p1v¬p1"
	result = ""
	n = int(math.log2(len(l)))
	for i in getFpositions(l):
		result += "("
		for j in range(1,n+1):
			result += str(N(i,j,n))
			result += "p"
			result += str(j)
			if j != n:
				result += "v"
			else:
				continue
		result += ")"
		if i != getFpositions(l)[len(getFpositions(l))-1]: 
			result += "∧"
		else:
			continue
	return result


# If we are dealing with a 2 dimensional universe 
# of propositions, this is an option to display the truth table. 
def display(comb):
    assert len(comb) == 4
    result = f"p1 | p2| {getSentence(comb)} \n"
    result += f"T  | T | {comb[0]} \n"
    result += f"T  | F | {comb[1]} \n"
    result += f"F  | T | {comb[2]} \n"
    result += f"F  | F | {comb[3]} \n"
    return result