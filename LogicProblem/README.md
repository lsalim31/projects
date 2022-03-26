# The problem. 
Given the truth table below, what is the sentence that gives us the desired output in the last column?
```
|  p  |  q  |  ?  |  
| --- | --- | --- |  
|  T  |  T  |  T  |  
|  T  |  F  |  T  |  
|  F  |  T  |  F  |  
|  F  |  F  |  T  |  
```
For this example, we can easily see that the sentence **(p ∨ ¬q)** staisifies our needs. In two dimenions (two propositional variables),
there are 16 possibles outcomes (i.e., one can form 16 different propositional sentences when working with two variables). This does not seem challenging enough, but if we generalize this to n dimensions, the problem becomes extremely difficult for us (with 2^(2^n) possible outcomes) and that is where this program comes in. 

## The question.
Given a general truth table output, for intance (say in 3 dimensions):
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

## The answer.
To solve this, we can run our program with 
```
python3 main.py TFTFFTTT
```
where **TFTFFTTT** is the desired output for the unknown sentence with 3 propositional variables. Our program outputs
```
(¬p1 ∨ ¬p2 ∨ p3) ∧ (¬p1 ∨ p2 ∨ p3) ∧ (p1 ∨ ¬p2 ∨ ¬p3)
```
which correclty matches the truth table. One can awalys check the answers using an [online truth table calculator](https://web.stanford.edu/class/cs103/tools/truth-table-tool/) (bear in mind that calculator from this website uses a different convention: 
it starts the table with _falses_, not with _trues_).
There is also the option (in 2 dimensions only) to include
a **--display** when running the program. 
```
python3 main.py TFTF --display
```
Which outputs the entire truth table:
```
|  p1 |  p2 | (¬p1 ∨ p2) ∧ (p1 ∨ p2) | 
| ––– | ––– | –––––––––––––––––––––– | 
|  T  |  T  |            T           | 
|  T  |  F  |            F           | 
|  F  |  T  |            T           | 
|  F  |  F  |            F           | 
```
Notice that we are not concerned with the most elegant sentence, we only care about a systematic way to solve this, 
and this is provided by our program. 





