import matplotlib.pyplot as plt

# 构造数据
edu = [0.2515, 0.3724, 0.3336, 0.0368, 0.0057]
labels = ['中专', '大专', '本科', '硕士', '其他']

# 中文乱码和坐标轴负号的处理
plt.rcParams['font.sans-serif'] = ['Microsoft YaHei']
plt.rcParams['axes.unicode_minus'] = False

# 绘制饼图
plt.pie(x=edu, # 绘图数据
labels=labels, # 添加教育水平标签
autopct='%.1f%%' # 设置百分比的格式，这里保留一位小数
)
# 添加图标题
plt.title('失信用户的教育水平分布')
# 显示图形
plt.show()

import pandas as pd   
data = pd.DataFrame({'a':[1, 2, 3], 'b': [4, 5, 6]})  #要保存的数据
data.to_csv("test.csv")
