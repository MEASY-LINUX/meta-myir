#include <QApplication>
#include <QMediaPlayer>
#include <QQmlApplicationEngine>
#include <QQuickWindow>
#include <QQuick3D>
#include <QSerialPort>
#include <QSvgRenderer>
#include <QString>
#include <QWidget>
#include <vector>

int main(int argc, char **argv)
{
    QApplication app(argc, argv);
    QQmlApplicationEngine engine;
    QQuickWindow window;
    QMediaPlayer player;
    QWidget widget;
    QSerialPort serial;
    QSvgRenderer svg;
    auto format = QQuick3D::idealSurfaceFormat();
    window.setFormat(format);
    std::vector<QString> values{QStringLiteral("MYIR SDK")};
    widget.setWindowTitle(values.front());
    return 0;
}
