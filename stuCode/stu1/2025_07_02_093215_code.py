import matplotlib.pyplot as plt

# 数据
x = [1, 2, 3, 4, 5]
y = [2, 3, 5, 7, 11]

# 创建图形
plt.plot(x, y, label='Line 1')  # 绘制线图
plt.title('Simple Line Plot')  # 添加标题
plt.xlabel('X-axis')           # 添加X轴标签
plt.ylabel('Y-axis')           # 添加Y轴标签
plt.legend()                   # 添加图例
plt.show()                     # 显示图形