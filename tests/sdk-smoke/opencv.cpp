#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/videoio.hpp>
#include <vector>

int main()
{
    cv::Mat input(16, 16, CV_8UC3, cv::Scalar(0, 0, 0));
    cv::Mat gray;
    cv::cvtColor(input, gray, cv::COLOR_BGR2GRAY);
    std::vector<unsigned char> encoded;
    cv::VideoCapture camera;
    return cv::imencode(".png", gray, encoded) ? 0 : 1;
}
