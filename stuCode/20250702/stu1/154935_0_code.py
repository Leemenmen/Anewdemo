#在这里编写代码
import csv

# 数据
data = [
    ["Name", "Age", "City"],  # 表头
    ["Alice", 25, "New York"],
    ["Bob", 30, "Los Angeles"],
    ["Charlie", 35, "Chicago"]
]

# 写入CSV文件
with open("output.csv", mode="w", newline="", encoding="utf-8") as file:
    writer = csv.writer(file)
    writer.writerows(data)

print("CSV文件已生成：output.csv")