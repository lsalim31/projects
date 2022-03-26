from Matrix import Matrix

class Tower():
    dis = 10
    def __init__(self, rod1, rod2, rod3):
        self.rod1 = rod1
        self.rod2 = rod2
        self.rod3 = rod3
        self.out1 = [i*"-" for i in rod1]
        self.out2 = [i*"-" for i in rod2]
        self.out3 = [i*"-" for i in rod3]
        self.matrix = Matrix.fillMatrix([self.out1, self.out2, self.out3])

    def myPrint(self):
        rotatedMatrix = Matrix.rotateLeft(self.matrix)
        result = ""
        rowCounter = 0
        for row in rotatedMatrix:
            rowCounter += 1
            for element in row:
                if element != "*":
                    result += element + (Tower.dis- len(element))*" "
                else:
                    result += Tower.dis*" "
            if rowCounter < len(rotatedMatrix):
                result += '\n'
        result += '\n'
        result += "1" + Tower.dis*" " + "2" + Tower.dis*" " + "3"
        return result

    def __str__(self) -> str:
        return self.myPrint()

