#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <android/native_window_jni.h>
#include "macro.h"

using namespace cv;

DetectionBasedTracker *tracker = 0;
ANativeWindow *window = 0;
int i = 0;

class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector) :
            IDetector(),
            Detector(detector) {
    }

    /**
     * 检测人脸调用
     * @param Image Mat = Map
     * @param objects
     */
    void detect(const cv::Mat &Image, std::vector<cv::Rect> &objects) {
        Detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize,
                                   maxObjSize);
    }

    virtual ~CascadeDetectorAdapter() {
    }

private:
    CascadeDetectorAdapter();

    cv::Ptr<cv::CascadeClassifier> Detector;
};

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_opencv_face_OpenCvJni_init(JNIEnv *env, jobject instance, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);

    //openCv智能指针
    Ptr<CascadeClassifier> classifier = makePtr<CascadeClassifier>(path);
    //创建检测器
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(classifier);

    //创建跟踪器
    Ptr<CascadeClassifier> classifier1 = makePtr<CascadeClassifier>(path);
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(classifier1);

    DetectionBasedTracker::Parameters DetectionParameters;
    //tracker =  封装了 检测器和跟踪器的对象
    tracker = new DetectionBasedTracker(mainDetector, trackingDetector, DetectionParameters);
    tracker->run();
    env->ReleaseStringUTFChars(path_, path);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_opencv_face_OpenCvJni_postData(JNIEnv *env, jobject instance, jbyteArray data_,
                                            jint width, jint height, jint cameraId) {

    // NV21 -> Bitmap -> Mat矩阵
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    //src == Bitmap
    Mat src(height + height / 2, width, CV_8UC1, data);
    // nv21 -> RGBA  (转换类型 原始数据NV21 )  (把原来的src 重新设置 所以 第二个也是src ，无需声明2个 Mat)
    cvtColor(src, src, COLOR_YUV2RGBA_NV21);
//    imwrite("/storage/emulated/0/src.jpg", src);

    //使用Camera1工具时 判断前后摄像头 设置旋转图像
//    if (cameraId == 1) {
//        //前置摄像头  逆时针旋转90度
//        rotate(src, src, ROTATE_90_COUNTERCLOCKWISE);
//        //镜像操作 1=水平翻转  0=垂直翻转
//        flip(src, src, 1);
//    } else {
//        //后置摄像头  顺时针旋转90度
//        rotate(src, src, ROTATE_90_CLOCKWISE);
//    }

    //转换为灰度图
    Mat gray;
    cvtColor(src, gray, COLOR_RGBA2GRAY);
//    imwrite("/storage/emulated/0/src4.jpg", gray);

    //对比度 二值化
    equalizeHist(gray, gray);
//    imwrite("/storage/emulated/0/src5.jpg", gray);

    //检测人脸
    std::vector<Rect> faces;
    tracker->process(gray);
    tracker->getObjects(faces);


    if (faces.size()) {
        LOGE("检测到人脸");
    }

    for (Rect face:faces) {
        rectangle(src, face, Scalar(255, 0, 255));

        //训练数据模型 存储图片特征
//        Mat m;
//        src(face).copyTo(m);
//        resize(m, m, Size(24, 24));
//        //转换为灰度图 输出
//        cvtColor(m, m, COLOR_BGR2GRAY);
//        char p[100];
//        //写入文件中
//        sprintf(p, "/storage/emulated/0/openCV/%d.jpg", i++);
    }

    //将图像拷贝至window中
    if (window) {
        ANativeWindow_setBuffersGeometry(window, src.cols, src.rows,WINDOW_FORMAT_RGBA_8888);
        ANativeWindow_Buffer window_buffer;
        //通知HardwareLayer解除锁定
        ANativeWindow_acquire(window);
        do {
            //锁定失败 直接退出
            if (ANativeWindow_lock(window, &window_buffer, NULL)) {
                ANativeWindow_release(window);
                window = NULL;
                LOGE("Window锁定失败");
                break;
            }


            LOGE("开始拷贝");
            uint8_t *dst_data = static_cast<uint8_t *>(window_buffer.bits);
            int dst_linesize = window_buffer.stride * 4;

            //一行行拷贝
            for (int i = 0; i < window_buffer.height; ++i) {
                memcpy(dst_data + i * dst_linesize, src.data + i * src.cols * 4, dst_linesize);
            }
            //提交刷新 解锁通知
            LOGE("拷贝完成");
            ANativeWindow_unlockAndPost(window);

        } while (0);
    }
    src.release();
    gray.release();

    env->ReleaseByteArrayElements(data_, data, 0);
}

/**
 * 传入surface 初始化window对象
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_lee_opencv_face_OpenCvJni_setSurface(JNIEnv *env, jobject instance, jobject surface) {
    if (window) {
        LOGE("清空window");
        ANativeWindow_release(window);
        window = 0;
    }
    LOGE("创建window");
    window = ANativeWindow_fromSurface(env, surface);
}