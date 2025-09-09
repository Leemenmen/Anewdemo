import matplotlib.pyplot as plt

# 数据
x = list(range(1, 11))  # X轴数据，从1到10
y = [i**2 for i in x]   # Y轴数据，x的平方

# 绘图
plt.plot(x, y, marker='o', linestyle='-', color='b', label='y = x^2')
plt.title("Simple Line Plot")  # 图表标题
plt.xlabel("X-axis")           # X轴标签
plt.ylabel("Y-axis")           # Y轴标签
plt.legend()                   # 显示图例
plt.grid(True)                 # 显示网格
plt.savefig('E:/dmsp/Anewdemo/stuCode/20250702/stu1/162719_fig_0.png')                     # 显示图表