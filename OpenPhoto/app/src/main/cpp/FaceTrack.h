//
// Created by jv.lee on 2019/6/27.
//

#ifndef OPENPHOTO_FACETRACK_H
#define OPENPHOTO_FACETRACK_H

#include <opencv2/opencv.hpp>
#include <opencv2/objdetect.hpp>
#include <vector>
#include "opencv2/objdetect.hpp"
#include "face_alignment.h"
#include "macro.h"

using namespace std;
using namespace cv;

class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector) :
            IDetector(),
            Detector(detector) {
        CV_Assert(detector);
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

class FaceTrack {
public:
    FaceTrack(const char *path, const char *seeta);

    void startTracking();

    vector<Rect2f> detector(Mat mat);

private:
    Ptr<DetectionBasedTracker> tracker;
    Ptr<seeta::FaceAlignment> faceAlignment;
};


#endif //OPENPHOTO_FACETRACK_H
