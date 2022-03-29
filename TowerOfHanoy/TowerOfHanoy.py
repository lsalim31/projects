from Towers import Tower

# Return the list of moves needed to move n disks from start rod to end rod.
# Each move is represented as a pair (a, b) that tells us to move top disk 
# from rod a to rod b. 
def f(n, start, end):
    if n == 1:
        return [(start, end)]
    return f(n-1, start, 6 - (start + end)) + f(1, start, end) + f(n-1, 6 - (start + end), end)


# Output for N disks in initial position. 
def output(n):
    sequence = f(n, 1, 3)
    T = Tower([i for i in range(n, 0, -1)], [], [])
    print(T)
    print()
    for pair in sequence:
        print("Move top disk from rod " + str(pair[0]) + " to rod " + str(pair[1]) + ":")
        myTower = [T.rod1, T.rod2, T.rod3]
        el = myTower[pair[0] - 1].pop()
        myTower[pair[1] - 1].append(el)
        T = Tower(myTower[0], myTower[1], myTower[2])
        print(T)
        print()
        
    print("Number of moves: " + str(len(sequence))) # This should always be 
