代码运行有误，请检查后重新运行！！！
Traceback (most recent call last):
  File "E:\dmsp\Anewdemo\stuCode\stu1\2025_06_23_152928_0_code.py", line 6, in <module>
    pd.read_excel(io = r'C:\Users\Administrator\Desktop\data_test02.xlsx', header = None, 
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "F:\Anacoda\Lib\site-packages\pandas\io\excel\_base.py", line 495, in read_excel
    io = ExcelFile(
         ^^^^^^^^^^
  File "F:\Anacoda\Lib\site-packages\pandas\io\excel\_base.py", line 1550, in __init__
    ext = inspect_excel_format(
          ^^^^^^^^^^^^^^^^^^^^^
  File "F:\Anacoda\Lib\site-packages\pandas\io\excel\_base.py", line 1402, in inspect_excel_format
    with get_handle(
         ^^^^^^^^^^^
  File "F:\Anacoda\Lib\site-packages\pandas\io\common.py", line 882, in get_handle
    handle = open(handle, ioargs.mode)
             ^^^^^^^^^^^^^^^^^^^^^^^^^
FileNotFoundError: [Errno 2] No such file or directory: 'C:\\Users\\Administrator\\Desktop\\data_test02.xlsx'
