class Matrix:
    # Given a matrix M, returns a squared matrix with 
    # "*" on intial empty entries.
    def fillMatrix(M):
        maxrows = max([len(M[i]) for i in range(len(M))])
        for row in M:
            size = len(row)
            if len(row) < maxrows:
                for _ in range(maxrows - size):
                    row.append("*")
        nrows = len(M)
        ncols = len(M[0])
        if nrows < ncols:
            for _ in range(ncols - nrows):
                M.append(["*" for _ in range(ncols)])
        elif nrows > ncols:
            for row in M:
                for _ in range(nrows - ncols):
                    row.append("*")
        return M
        
    # Return the transpose of matrix M.
    def transpose(M):
        result = [["*" for _ in range(len(M))] for _ in range(len(M))]
        for i in range(0, len(M)):
            for j in range(0, len(M[0])):
                result[i][j] = M[j][i]
        return result
    
    # Return the rotation of matrix M by 90deg.
    def rotateLeft(M):
        for row in M:
            row.reverse()
        return Matrix.transpose(M)