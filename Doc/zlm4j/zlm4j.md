## ffmpeg

下载 ffmpeg 并安装 ffmpeg 到环境变量。

## 推流

~~~cmd

ffmpeg -re -an -i test-240.mp4 -c:v libx264 -f rtsp rtsp://127.0.0.1:9758/steam/1?token=112233

~~~