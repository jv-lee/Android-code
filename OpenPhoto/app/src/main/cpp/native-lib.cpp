#include <jni.h>
#include <string>
#include "FaceTrack.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_lee_open_photo_face_FaceJni_nativeInit(JNIEnv *env, jobject instance, jstring path_,
                                                jstring seeta_) {
    const char *path = env->GetStringUTFChars(path_, 0);
    const char *seeta = env->GetStringUTFChars(seeta_, 0);

    FaceTrack *faceTrack = new FaceTrack(path);

    env->ReleaseStringUTFChars(path_, path);
    env->ReleaseStringUTFChars(seeta_, seeta);

    return reinterpret_cast<jlong>(faceTrack);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_open_photo_face_FaceJni_nativeStart(JNIEnv *env, jobject instance, jlong self) {
    if (self == 0) {
        return;
    }
    //内存地址强转对象
    FaceTrack *me = reinterpret_cast<FaceTrack *>(self);
    me->startTracking();

}

/**
 * 检测人眼位置
 */
extern "C"
JNIEXPORT jobject JNICALL
Java_com_lee_open_photo_face_FaceJni_nativeDetector(JNIEnv *env, jobject instance, jlong self,
                                                    jbyteArray data_, jint cameraId, jint width,
                                                    jint height) {
    if (self == 0) {
        return NULL;
    }
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    FaceTrack *me = reinterpret_cast<FaceTrack *>(self);

    //人脸检测 Mat == Bitmap
    Mat src(height + height / 2, width, CV_8UC1, data);
    cvtColor(src, src, COLOR_YUV2RGBA_NV21);
    if (cameraId == 1) {
        //前置摄像头逆时针旋转90度
        rotate(src, src, ROTATE_90_COUNTERCLOCKWISE);
        //1水平翻转 0垂直翻转  （避免镜像）
        flip(src, src, 1);
    } else {
        //后置摄像头顺时针旋转90度
        rotate(src, src, ROTATE_90_CLOCKWISE);
    }

    //转换为灰度图 提高识别准确度
    Mat gray;
    cvtColor(src, gray, COLOR_RGBA2GRAY);
    //轮廓化
    equalizeHist(gray, gray);

    //这里我们开始使用openCv开始检测
    me->detector(gray);
    env->ReleaseByteArrayElements(data_, data, 0);
}