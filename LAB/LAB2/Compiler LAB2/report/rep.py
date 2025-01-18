import re

def extract_and_format(filename):
    # 读取文件内容
    with open(filename, 'r') as file:
        content = file.read()
    
    # 使用正则表达式提取所有的数字
    numbers = re.findall(r"平均运行时间: ([0-9.]+) 秒", content)
    
    # 初始化结果字符串和计数器
    result = ""
    count = 0
    
    # 遍历所有数字，进行格式化
    for number in numbers:
        result += number + " & "
        count += 1
        # 每5个数字换行并添加\hline
        if count % 5 == 0:
            result = result[:-2] + " \\\\ \\hline\n        "
    
    # 去除最后的 & 和多余的空格
    if result.endswith(" & "):
        result = result[:-2]
    if result.endswith(" \\\\ \\hline\n        "):
        result = result[:-len(" \\\\ \\hline\n        ")]
    
    return result

# 使用示例
filename = "your_file.txt"  # 替换为你的文件名
formatted_text = extract_and_format(filename)
print(formatted_text)
