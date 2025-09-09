# 导入模块
import pandas as pd
import numpy as np

# 读取电子表格数据
pd.read_excel(io = r'C:\Users\Administrator\Desktop\data_test02.xlsx', header = None, 
                           names = ['Prod_Id','Prod_Name','Prod_Color','Prod_Price'])


# 数据类型转换及描述统计
# 数据读取
sec_cars = pd.read_table(r'C:\Users\Administrator\Desktop\sec_cars.csv', sep = ',')
# 预览数据的前五行
sec_cars.head()
# 查看数据的行列数
print('数据集的行列数：\n',sec_cars.shape)
# 查看数据集每个变量的数据类型
print('各变量的数据类型：\n',sec_cars.dtypes)


