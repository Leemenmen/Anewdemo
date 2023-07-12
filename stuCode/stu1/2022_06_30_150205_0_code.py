import sys
 
sys.stdout = open('2022_06_30_150205_result.py', mode = 'w',encoding='utf-8')
import numpy as np
a = np.array([1, 2, 3,4])
print(type(a))
print(a.shape)