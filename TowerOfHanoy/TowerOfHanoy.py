from Towers import Tower

def f(n, start, end):
    if n == 1:
        return [(start, end)]
    return f(n-1, start, 6 - (start + end)) + f(1, start, end) + f(n-1, 6 - (start + end), end)

sequence = f(8, 1, 3)
T = Tower([8, 7, 6, 5, 4, 3, 2, 1], [], [])
print(T)
for pair in sequence:
    print("Move top disk from rod " + str(pair[0]) + " to rod " + str(pair[1]) + ":")
    myTower = [T.rod1, T.rod2, T.rod3]
    el = myTower[pair[0] - 1].pop()
    myTower[pair[1] - 1].append(el)
    T = Tower(myTower[0], myTower[1], myTower[2])
    print(T)
    
print("Number of moves: " + str(len(sequence)))
