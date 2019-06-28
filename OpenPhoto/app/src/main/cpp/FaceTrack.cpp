//
// Created by jv.lee on 2019/6/27.
//

#include "FaceTrack.h"

FaceTrack::FaceTrack(const char *path, const char *seeta) {

    //初始化检测器
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(path));
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(path));
    DetectionBasedTracker::Parameters detectorParams;

    //追踪器
    tracker = makePtr<DetectionBasedTracker>(mainDetector, trackingDetector, detectorParams);

//    faceAlignment = makePtr(seeta);


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
vector<Rect2f> FaceTrack::detector(Mat mat) {
    vector<Rect> faces;
    vector<Rect2f> rects;
    //开始检测
    tracker->process(mat);
    //获取结果
    tracker->getObjects(faces);
    //定义5个关键点 识别人脸
    seeta::FacialLandmark points[5];
    //遍历多个人脸 对每一个人眼进行定位 放大
    if (faces.size()) {
        Rect face = faces[0];
        //人脸的区域
//        rects.push_back(Rect2f(face.x, face.y, face.width, face.height));


        seeta::ImageData imageData(mat.cols, mat.rows);
        imageData.data = mat.data;

        //检测的区域
        seeta::FaceInfo faceInfo;
        seeta::Rect bbox;
        bbox.x = face.x;
        bbox.y = face.y;
        bbox.width = face.width;
        bbox.height = face.height;
        faceInfo.bbox = bbox;

        faceAlignment->PointDetectLandmarks(imageData, faceInfo, points);
        for (int i = 0; i < 5; ++i) {
            rects.push_back(Rect2f(points[i].x, points[i].y, 0, 0));
            if (i == 0) {
                LOGE("左眼坐标 x %ld y %ld", points[i].x, points[i].y);
            }
            if (i == 1) {
                LOGE("右眼坐标 x %ld y %ld", points[i].x, points[i].y);
            }
        }
    }
    LOGE("人脸 数量%d", faces.size());
    LOGE("5个关键点 数量%d", rects.size());
    return rects;
}
