//SurfaceTexture 特殊性
//float数据是什么精度
precision mediump float;

//采样点的坐标
varying mediump vec2 aCoord;
uniform int width;
uniform int height;


//采样器 不是从android的surfaceTexture中的纹理 采数据了，所以不再需要android的扩展纹理采样了
//正常使用的 sampler2D
uniform sampler2D vTexture;
//20个坐标
vec2 blurCoordinates[20];
void main(){

    //实现高斯模糊
    vec2 sing10StepOffset=vec2(1.0f/float(width), 1.0/float(height));
    blurCoordinates[0] = aCoord.xy+sing10StepOffset*vec2(0.0, -10.0);
    blurCoordinates[1] = aCoord.xy+sing10StepOffset*vec2(0.0, 10.0);
    blurCoordinates[2] = aCoord.xy+sing10StepOffset*vec2(-10.0, 0.0);
    blurCoordinates[3] = aCoord.xy+sing10StepOffset*vec2(10.0, 0.0);
    blurCoordinates[4] = aCoord.xy+sing10StepOffset*vec2(5.0, -8.0);
    blurCoordinates[5] = aCoord.xy+sing10StepOffset*vec2(5.0, 8.0);
    blurCoordinates[6] = aCoord.xy+sing10StepOffset*vec2(-5.0, 8.0);
    blurCoordinates[7] = aCoord.xy+sing10StepOffset*vec2(-5.0, -8.0);
    blurCoordinates[8] = aCoord.xy+sing10StepOffset*vec2(8.0, -5.0);
    blurCoordinates[9] = aCoord.xy+sing10StepOffset*vec2(8.0, 5.0);
    blurCoordinates[10] = aCoord.xy+sing10StepOffset*vec2(-8.0, 5.0);
    blurCoordinates[11] = aCoord.xy+sing10StepOffset*vec2(-8.0, -5.0);
    blurCoordinates[12] = aCoord.xy+sing10StepOffset*vec2(0.0, -6.0);
    blurCoordinates[13] = aCoord.xy+sing10StepOffset*vec2(0.0, 6.0);
    blurCoordinates[14] = aCoord.xy+sing10StepOffset*vec2(6.0, 0.0);
    blurCoordinates[15] = aCoord.xy+sing10StepOffset*vec2(-6.0, 0.0);
    blurCoordinates[16] = aCoord.xy+sing10StepOffset*vec2(-4.0, -4.0);
    blurCoordinates[17] = aCoord.xy+sing10StepOffset*vec2(-4.0, 4.0);
    blurCoordinates[18] = aCoord.xy+sing10StepOffset*vec2(4.0, -4.0);
    blurCoordinates[19] = aCoord.xy+sing10StepOffset*vec2(4.0, 4.0);

    //aCoord.xy  获取中心点的color值
    vec4 currentColor = texture2D(vTexture,aCoord);
    vec3 totalRGB = currentColor.rgb;

    for(int i = 0;i<20;i++)
    {
        //获取所有点RGB
        totalRGB+= texture2D(vTexture,blurCoordinates[i].xy).rgb;

    }
    vec4 blur = vec4(totalRGB*1.0/21.0,currentColor.a);

    vec4 heightPassColor = currentColor - blur;

    //实现高反差  原图减去模糊的图  反向颜色
    heightPassColor.r = clamp(2.0*heightPassColor.r *heightPassColor.r * 24.0,0.0,1.0);
    heightPassColor.g = clamp(2.0*heightPassColor.g *heightPassColor.g * 24.0,0.0,1.0);
    heightPassColor.b = clamp(2.0*heightPassColor.b *heightPassColor.b * 24.0,0.0,1.0);

    //图层叠加
    vec3 r = mix(currentColor.rgb,blur.rgb,0.2);
    gl_fragColor = vec4(r,1.0);

}