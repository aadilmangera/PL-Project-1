Function logic:
1) Add the first item to a new bin directly;
2) From the second item to the last one:
   a. Search the list of bins to find the one whose load is maximum and the remaining capacity is enough for the current item;
   b. If a such bin exists, put the current item into it; otherwise, put the current item into a new bin;
   C. Repeat step a and b until there is no item left.


Running Process (From my own laptop):
Input: 
1) An integer list with 10000 randomly generated integers ranging from 1 to 20
2) Bin Capacity = 20

Output information:
Before task:
Total Memory (MB): 252
Free Memory (MB): 249
Used Memory (MB): 3

After task:
Total Memory (MB): 252
Free Memory (MB): 243
Used Memory (MB): 9

(1 12 7 ) (15 4 1 ) (15 5 ) (19 1 ) (19 1 ) (17 2 1 ) (16 4 ) (20 ) (6 11 2 1 ) (19 1 ) (15 5 ) (14 3 3 ) ........
Number of bins used: 5296
Total time of cost: 345ms


Suggestion:
It is more accurate that we compare the results of Best-fit and Fisrt-fit Decreasing from the same computer

