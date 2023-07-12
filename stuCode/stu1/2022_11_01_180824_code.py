# 导入第三方包
import requests
import time
import random
import pandas as pd
import re
import json
from bs4 import BeautifulSoup

Cookie = 'positionCityID=60009; positionCityPinyin=songjiang; lastAreaName=æ¾æ±Ÿ; Hm_lvt_a3f2879f6b3620a363bec646b7a8bcdd=1665642098; __bid_n=183d0016e59fda8bbf4207; FPTOKEN=30$PgwvCKVI7Xzo2ZRxy1DMlP1NYlux7TcqMus7e6O3s0ueOnGE1MvmDIcel3v3y8BoDcn+fFh5WqnfAn0Ct+ANN2HbKEA21RKTboAfc5K72kZduG+K7q2iJ4O+1niY4XvKNeQNXgP4bYJkgtdlW9pBkQejkjF1tl2MBH1peLWAcaQA+dwX/giOXl0/rBqNv299zTaAN0GDFJRpzk/oP7svY7uerImrCxq3qp4SPLhJ2n8viYmV+26+7mT9kmF/rj9sxuL58Nji+xFuL36QO4qLdu36+6qzTgWiCgytruyS0/HgoGFB2dLLJBwsmmh8HFoXpYwbypnDxDPNWyPlmDs7VdJFeBog+kNV5vViJ7pSBEyPbCC9Bc/9VSBMhEaDl0LA|xrRZDWU0amCvvHb5RSsgN275diM6zwmGrM2/b29v1Vw=|10|313080ab8954c8a1e0938832490269b3; lastCountyId=58238; lastCountyPinyin=nanjing; lastProvinceId=25; lastCityId=58238; Hm_lpvt_a3f2879f6b3620a363bec646b7a8bcdd=1665642604; lastCountyTime=1665642604'
headers = {
    # Accept:表示客户端可接受的资源类型
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,'
              'application/signed-exchange;v=b3;q=0.9',
    # 表示客户端可接受的内容编码
    'Accept-Encoding': 'gzip, deflate,br',
    # 表示客户端可接受的语言类型
    'Accept-Language': 'zh-CN,zh;q=0.9',
    'Connection': 'keep-alive',
    # cookies用'utf-8'编码防止乱码
    'Cookie': Cookie.encode('utf-8'),
    # 客户端的操作系统和浏览器
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 '
                  'Safari/537.36 '
}
# 将年份和月份存入数组，便于后续遍历
months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
years = [2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022]
# 针对2011到2015年，没有空气质量数据时对应索引，生成一个空的表data1
index_1 = ['date', 'MaxTemp', 'MinTemp', 'Weather', 'Wind']
data1 = pd.DataFrame(columns=index_1)
# 针对2016到2022年，有空气质量数据时对应索引,生成一个空的表data2
index_2 = ['date', 'MaxTemp', 'MinTemp', 'Weather', 'Wind', 'aqi']
data2 = pd.DataFrame(columns=index_2)

for y in years:
    for m in months:
        seconds = random.randint(3, 6)
        # 生成所有抓取需要的链接
        url = 'https://tianqi.2345.com/Pc/GetHistory?areaInfo[areaId]=58238&areaInfo[areaType]=2&date[year]=' + str(
            y) + '&date[month]=' + str(m)
        response = requests.get(url, headers=headers)
        response.encoding = 'utf-8'
        if response.status_code == 200:  # 防止url请求无响应
            html_str = json.loads(response.text)['data']  # 读取json文件
            soup = BeautifulSoup(html_str, 'lxml')  # 用BeautifulSoup解析
            tr = soup.select("tr")  # 通过标签名查找
            for i in tr[1:]:  # 遍历查找
                td = i.select('td')
                tmp = []
                for j in td:
                    tmp.append(re.sub('<.*?>', "", str(j)))  # 正则匹配获取值，加入tmp列表
                # 针对2011至2015年爬取数据，完成数据拼接，并且将aqi列设置为空值0
                if y in range(2011, 2016):
                    data_spider1 = pd.DataFrame(tmp).T
                    data_spider1.columns = index_1  # 修改列名
                    data1 = pd.concat((data1, data_spider1), axis=0)  # 数据拼接
                    data1['aqi'] = 0
                # 针对2016至2022年爬取数据，完成数据拼接
                else:
                    data_spider2 = pd.DataFrame(tmp).T
                    data_spider2.columns = index_2  # 修改列名
                    data2 = pd.concat((data2, data_spider2), axis=0)  # 数据拼接
    # 设置睡眠时间，防止被封ip
    time.sleep(seconds)
# 拼接data1和data2两张表
data = pd.concat((data1, data2), axis=0)
# 将数据导成csv存取
data.to_csv('weatherdata2011_2022_ny.csv')
