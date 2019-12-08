#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <malloc.h>
#include <jpeglib.h>

void write_jepg_file(uint8_t *temp, int w, int h, jint q, const char *path);

extern "C" JNIEXPORT jstring JNICALL
Java_com_lee_huffman_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

void write_jepg_file(uint8_t *data, int w, int h, jint q, const char *path) {
    //创建jepg压缩对象
    jpeg_compress_struct jcs;
    //错误回调
    jpeg_error_mgr error;
    jcs.err = jpeg_std_error(&error);

    //创建压缩对象
    jpeg_create_compress(&jcs);
    //指定存储文件 write binary
    FILE *f = fopen(path,"wb");
    jpeg_stdio_dest(&jcs,f);

    //设置压缩参数
    jcs.image_width = w;
    jcs.image_height = h;
    //bgr
    jcs.input_components = 3;
    jcs.in_color_space = JCS_RGB;
    jpeg_set_defaults(&jcs);
    //开启哈夫曼算法压缩功能
    jcs.optimize_coding = true;
    jpeg_set_quality(&jcs,q,1);
    //开始压缩
    jpeg_start_compress(&jcs, true);
    //循环写入每一帧数据
    int row_stride = w * 3; // 一行的字节数
    JSAMPROW row[1];
    while (jcs.next_scanline < jcs.image_height) {
        //取一行数据
        uint8_t *pixels = data + jcs.next_scanline * row_stride;
        row[0] = pixels;
        jpeg_write_scanlines(&jcs,row,1);
    }

    //压缩完成
    jpeg_finish_compress(&jcs);
    //释放文件， 释放压缩对象
    fclose(f);
    jpeg_destroy_compress(&jcs);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_huffman_MainActivity_compressImage(JNIEnv *env, jobject instance, jobject bitmap,
                                                jstring path_, jint q) {
    const char *path = env->GetStringUTFChars(path_, 0);
    AndroidBitmapInfo info;
    //获取bitmap中的信息数据
    AndroidBitmap_getInfo(env,bitmap,&info);
    //定义一个pixels  定义一个byte[]  接收图像数据
    uint8_t *pixels;

    //将bitmap中的数据 指向 pixels 中 ，
    AndroidBitmap_lockPixels(env, bitmap, (void **)&pixels); //  将bitmap 通过万能指针  指向pixels地址

    int w = info.width;
    int h = info.height;
    int color;

    //开辟内存用来存放rgb信息
    uint8_t* data = (uint8_t *)malloc(w*h*3);

    uint8_t* temp = data;//临时占位 , 获取data初始的占位 位置，  防止 data在赋值的时候， 指针指向最后。  之后再操作temp其实等于操作data， temp是一个指针。 操作赋值后的data ，直接操作temp即可
    uint8_t r,g,b;
    //循环获取图片中的每一个像素
    for (int i = 0; i <h; i++) {
        for (int j = 0; j < w; j++) {
            //每一次赋值 会将pixels 内存地址第一个位置的数据 赋值 ，就是第一个像素点， 第二次 就是第二个像素点， 指针会在pixels内存地址中指向下一个
            color = *(int *)pixels;
            //获取每一个像素点的 rgb数据
            r = (color >> 16) & 0xFF;
            g = (color >> 8) & 0xFF;
            b = color & 0xFF;

            //jpeg 存放rgb在数据中 是相反的 是从 bgr 开始存放的
            *data = b; //将b 放在 data中的第一位
            *(data + 1) = g; //将g 放在data中的第二位
            *(data + 2) = r;//将r 放在data中的第三位
            data += 3; // 将data+= 3 ->  获取 bgr的数据后，第四位 a通道不获取。

            pixels += 4;//设置内存地址 为第5位 ，  0，1，2，3已经取过值了， 下一次循环跳过a通道直接从后面开始继续获取下一个像素点。
        }
    }

    //将得到的新的图片存放到新文件中去
    write_jepg_file(temp,w,h,q,path);
    free(temp);
    //释放内存
    AndroidBitmap_unlockPixels(env, bitmap);


    env->ReleaseStringUTFChars(path_, path);
}