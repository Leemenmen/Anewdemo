import pdb
import subprocess
import sys

import imageio
import os
from PIL import Image


def video2mp3(file_name):
    """
    将视频转为音频
    :param file_name: 传入视频文件的路径
    :return:
    """
    outfile_name = file_name.split('.')[0] + '.mp3'
    print(outfile_name)
    subprocess.call('ffmpeg -i ' + file_name
                    + ' -f mp3 ' + outfile_name.encode(sys.getfilesystemencoding()), shell=True)


def video_add_mp3(file_name, mp3_file):
    """
     视频添加音频
    :param file_name: 传入视频文件的路径
    :param mp3_file: 传入音频文件的路径
    :return:
    """
    outfile_name = file_name.split('.')[0] + '-txt.mp4'
    subprocess.call('ffmpeg -i ' + file_name
                    + ' -i ' + mp3_file + ' -strict -2 -f mp4 '
                    + outfile_name, shell=True)


def compose_gif(file_path):
    """
     将静态图片转为gif动图
     :param file_path: 传入图片的目录的路径
     :return:
    """
    img_paths = sorted([int(p[3:-4]) for p in os.listdir(file_path) if os.path.splitext(p)[1] == ".png"])
    img_paths = img_paths[:int(len(img_paths) / 3.6)]
    gif_images = []
    for path in img_paths:
        gif_images.append(imageio.imread('{0}/out{1}.png'.format(file_path, path)))
    imageio.mimsave("test.gif", gif_images, fps=30)


def compress_png(file_path):
    """
     将gif动图转为每张静态图片
     :param file_path: 传入gif文件的路径
     :return:
    """
    img_paths = [p for p in os.listdir(file_path) if os.path.splitext(p)[1] == ".png"]
    for filename in img_paths:
        with Image.open('{0}/{1}'.format(file_path, filename)) as im:
            width, height = im.size
            new_width = 150
            new_height = int(new_width * height * 1.0 / width)
            resized_im = im.resize((new_width, new_height))
            output_filename = filename
            resized_im.save('{0}/{1}'.format(file_path, output_filename))


if __name__ == '__main__':
    # 视频转音频
    str = sys.path[0]
    subprocess.call("ffmpeg -i F:\project/videoProcess\sample.mp4 -f mp3 -b:a 192k -ar 44100 -ac 2 -acodec libmp3lame -y F:\project/videoProcess\sample.mp3")
    subprocess.call("ffmpeg -i F:\project/videoProcess\sample.mp4".encode(sys.getfilesystemencoding()), shell=True)
    # video2mp3(file_name=str)
    # video_add_mp3(file_name='swap-data-a.mp4', mp3_file='data-a.mp3')
    # compose_gif(file_path='merged')
    # compress_png(file_path='merged')
