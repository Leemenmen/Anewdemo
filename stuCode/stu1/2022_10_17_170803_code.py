import pandas as pd
data = pd.DataFrame({'a':[1, 2, 3], 'b': [4, 5, 6]}) #要保存的数据
data.to_csv("test.csv")