#define CL_TARGET_OPENCL_VERSION 120
#include <CL/cl.h>
#include <EGL/egl.h>
#include <GLES2/gl2.h>
#include <gbm.h>

int main(void)
{
    cl_uint platforms = 0;
    cl_int result = clGetPlatformIDs(0, NULL, &platforms);
    EGLDisplay display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    GLuint shader = glCreateShader(GL_VERTEX_SHADER);
    glDeleteShader(shader);
    struct gbm_device *device = gbm_create_device(-1);
    if (device)
        gbm_device_destroy(device);
    return result != CL_SUCCESS || display == EGL_NO_DISPLAY;
}
