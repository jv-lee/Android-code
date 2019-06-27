//把顶点坐标给这个变量,确定要画画的形状
attribute vec4 vPosition;
//接受纹理坐标，就收采样器采样图片的坐标
attribute vec4 vCoord;
//摄像头的矩阵
uniform mat4 vMatrix;

//传给片元着色器 像素点
varying vec2 aCoord;
void main(){
    //内置变量 gl_Position，我们把顶点数据赋值给这个变量 opengl就知道它要画什么形状了
    gl_Position = vPosition;
    //进行测试 和设备有关(有些设备直接就采集不到图像）
    aCoord = (vMatrix * vCoord).xy;
}