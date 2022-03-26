## The problem. 
Given the truth table below, what is the sentence that gives us the desired output in the last column?
```
|  p  |  q  |  ?  |  
| --- | --- | --- |  
|  T  |  T  |  T  |  
|  T  |  F  |  T  |  
|  F  |  T  |  F  |  
|  F  |  F  |  T  |  
```
For this example, we can easily see that the sentence **(p or q)** staisifies our needs. In two dimenions (two propositional variables),
that are 16 possibles outcomes, which does not seem challenging whatsoever. But if we generalize this to n dimensions, the problem becomes extremely difficult for our human brain (there are 2^(2^n) possible outcomes) and that is where this program comes in. Given a truth table output, 
for intance (say in 3 dimensions):
```
|  p1  |  p2  |  p3  |  ?  |
| ---- | ---- | ---- | --- |
|  T   |  T   |  T   |  T  |
|  T   |  T   |  F   |  F  |
|  T   |  F   |  T   |  T  |
|  T   |  F   |  F   |  F  |
|  F   |  T   |  T   |  F  |
|  F   |  T   |  F   |  T  |
|  F   |  F   |  T   |  T  |
|  F   |  F   |  F   |  T  |
```
what is the propositional sentence that would gives us the output in the last column?

## Execution
To solve this, we can run our program with 
```
python3 main.py TFTFFTTT
```
where **TFTFFTTT** is the desired output in the last column of our truth table. Our program outputs
```
(¬p1v¬p2vp3)∧(¬p1vp2vp3)∧(p1v¬p2v¬p3)
```
which correclty matches the truth table.  








