//
// Created by Raytine on 2019/6/27.
//

#include "FaceTrack.h"

FaceTrack::FaceTrack(const char *path) {

    //初始化检测器
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(path));
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(path));
    DetectionBasedTracker::Parameters detectorParams;

    //追踪器
    tracker = makePtr<DetectionBasedTracker>(mainDetector, trackingDetector, detectorParams);


}

/**
 * 启动跟踪器
 */
void FaceTrack::startTracking() {
    tracker->run();
}

/**
 * 开始检测人脸
 * @param mat  图片结构体
 */
void FaceTrack::detector(Mat mat) {
    vector<Rect> faces;
    vector<Rect2f> rects;
    //开始检测
    tracker->process(mat);
    //获取结果
    tracker->getObjects(faces);
    //遍历多个人脸 对每一个人眼进行定位 放大
    if (faces.size()) {
        Rect face = faces[0];
        //人脸的区域
        rects.push_back(Rect2f(face.x, face.y, face.width, face.height));
    }
}
