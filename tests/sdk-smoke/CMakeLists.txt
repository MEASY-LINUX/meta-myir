cmake_minimum_required(VERSION 3.22)
project(myir_sdk_smoke LANGUAGES C CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(PkgConfig REQUIRED)
pkg_check_modules(RUNTIME REQUIRED IMPORTED_TARGET
    libcurl openssl zlib sqlite3 libmodbus libdrm alsa gstreamer-1.0)
pkg_check_modules(GRAPHICS REQUIRED IMPORTED_TARGET
    egl glesv1_cm glesv2 gbm OpenCL)
find_package(Qt6 REQUIRED COMPONENTS Core Gui Widgets Qml Quick Multimedia SerialPort Svg Quick3D)
find_package(OpenCV REQUIRED COMPONENTS core imgproc imgcodecs videoio)

add_executable(runtime-smoke runtime.c)
target_link_libraries(runtime-smoke PRIVATE PkgConfig::RUNTIME)
add_executable(graphics-smoke graphics.c)
target_link_libraries(graphics-smoke PRIVATE PkgConfig::GRAPHICS)
add_executable(qt-smoke qt.cpp)
target_link_libraries(qt-smoke PRIVATE
    Qt6::Core Qt6::Gui Qt6::Widgets Qt6::Qml Qt6::Quick Qt6::Multimedia
    Qt6::SerialPort Qt6::Svg Qt6::Quick3D)
add_executable(opencv-smoke opencv.cpp)
target_include_directories(opencv-smoke PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(opencv-smoke PRIVATE ${OpenCV_LIBS})
