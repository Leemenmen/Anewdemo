import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# 导入数据集
income = pd.read_csv(r'F:/project/发布文件/示范数据集/Salary_Data.csv')
# 绘制散点图
sns.lmplot(x='YearsExperience', y='Salary', data=income, ci=None)
# 显示图形

plt.savefig('test.png')