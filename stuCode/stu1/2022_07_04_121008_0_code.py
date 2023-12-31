import matplotlib.pyplot as plt

# 构造数据
edu = [0.2515, 0.3724, 0.3336, 0.0368, 0.0057]
labels = ['中专', '大专', '本科', '硕士', '其他']

# 中文乱码和坐标轴负号的处理
plt.rcParams['font.sans-serif'] = ['Microsoft YaHei']
plt.rcParams['axes.unicode_minus'] = False

# 绘制饼图
plt.pie(x=edu,  # 绘图数据
        labels=labels,  # 添加教育水平标签
        autopct='%.1f%%'  # 设置百分比的格式，这里保留一位小数
        )
# 添加图标题
plt.title('失信用户的教育水平分布')
# 显示图形
plt.savefig('F:/project/Anewdemo/stuCode/stu1/2022_07_04_121008_fig_0.png')

# 添加修饰的饼图
explode = [0, 0.1, 0, 0, 0]  # 生成数据，用于突出显示大专学历人群
colors = ['#9999ff', '#ff9999', '#7777aa', '#2442aa', '#dd5555']  # 自定义颜色

# 中文乱码和坐标轴负号的处理
# plt.rcParams['font.sans-serif'] = ['Microsoft YaHei']
# plt.rcParams['axes.unicode_minus'] = False

# 将横、纵坐标轴标准化处理，确保饼图是一个正圆，否则为椭圆
plt.axes(aspect='equal')
# 绘制饼图
plt.pie(x=edu,  # 绘图数据
        explode=explode,  # 突出显示大专人群
        labels=labels,  # 添加教育水平标签
        colors=colors,  # 设置饼图的自定义填充色
        autopct='%.1f%%',  # 设置百分比的格式，这里保留一位小数
        pctdistance=0.8,  # 设置百分比标签与圆心的距离
        labeldistance=1.1,  # 设置教育水平标签与圆心的距离
        startangle=180,  # 设置饼图的初始角度
        radius=1.2,  # 设置饼图的半径
        counterclock=False,  # 是否逆时针，这里设置为顺时针方向
        wedgeprops={'linewidth': 1.5, 'edgecolor': 'green'},  # 设置饼图内外边界的属性值
        textprops={'fontsize': 10, 'color': 'black'},  # 设置文本标签的属性值
        )

# 添加图标题
plt.title('失信用户的受教育水平分布')
# 显示图形
plt.savefig('F:/project/Anewdemo/stuCode/stu1/2022_07_04_121008_fig_1.png')