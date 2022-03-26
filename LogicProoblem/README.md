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
For the example above, we can easily see that the sentence (**p or q)** staisifies our taks. In two dimenions, that are 16 possibles outcomes,
so this is not quite challenging for us. But if we generalize this to n dimensions, the problem becomes extremely difficult for the human brain 
(there are 2^(2^n) possible outcomes) and that is where this program comes in. 
