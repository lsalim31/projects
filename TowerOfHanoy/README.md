## A simple program to vizualize the [Tower of Hanoy](https://en.wikipedia.org/wiki/Tower_of_Hanoi) problem. 

Say we intially have 4 disks in the first rod. After running
```
python3 main.py 4
```
We have the following output: 
```
-                                       
--                                      
---                                     
----                                    
1          2          3

Move top disk from rod 1 to rod 2:
--                            
---                           
----      -                   
1          2          3

Move top disk from rod 1 to rod 3:
                              
---                           
----      -         --        
1          2          3

Move top disk from rod 2 to rod 3:
                              
---                 -         
----                --        
1          2          3

Move top disk from rod 1 to rod 2:
                              
                    -         
----      ---       --        
1          2          3

Move top disk from rod 3 to rod 1:
                              
-                             
----      ---       --        
1          2          3

Move top disk from rod 3 to rod 2:
                              
-         --                  
----      ---                 
1          2          3

Move top disk from rod 1 to rod 2:
          -                   
          --                  
----      ---                 
1          2          3

Move top disk from rod 1 to rod 3:
          -                   
          --                  
          ---       ----      
1          2          3

Move top disk from rod 2 to rod 3:
                              
          --        -         
          ---       ----      
1          2          3

Move top disk from rod 2 to rod 1:
                              
                    -         
--        ---       ----      
1          2          3

Move top disk from rod 3 to rod 1:
                              
-                             
--        ---       ----      
1          2          3

Move top disk from rod 2 to rod 3:
                              
-                   ---       
--                  ----      
1          2          3

Move top disk from rod 1 to rod 2:
                              
                    ---       
--        -         ----      
1          2          3

Move top disk from rod 1 to rod 3:
                    --        
                    ---       
          -         ----      
1          2          3

Move top disk from rod 2 to rod 3:
                    -                   
                    --                  
                    ---                 
                    ----                
1          2          3

Number of moves: 15

```
