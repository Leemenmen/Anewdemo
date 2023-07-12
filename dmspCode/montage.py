from moviepy.editor import *

# 剪辑50-60秒的音乐 00:00:50 - 00:00:60
# print(VideoFileClip("D:\Summary\工程中心\备课系统\视频\\4qingwen1.mp4").duration/60)
video = CompositeVideoClip([VideoFileClip("D:\Summary\工程中心\备课系统\视频\\4qingwen1.mp4").subclip(80, 130)])

# 写入剪辑完成的音乐
video.write_videofile("50.mp4")