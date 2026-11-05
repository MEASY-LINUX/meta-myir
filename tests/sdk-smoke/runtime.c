#include <alsa/asoundlib.h>
#include <curl/curl.h>
#include <gst/gst.h>
#include <modbus/modbus.h>
#include <openssl/crypto.h>
#include <sqlite3.h>
#include <xf86drm.h>
#include <zlib.h>

int main(int argc, char **argv)
{
    gst_init(&argc, &argv);
    modbus_t *ctx = modbus_new_tcp("127.0.0.1", 502);
    modbus_free(ctx);
    drmVersionPtr version = drmGetVersion(-1);
    drmFreeVersion(version);
    return !(curl_version() && OpenSSL_version(OPENSSL_VERSION) &&
             sqlite3_libversion() && zlibVersion() && snd_asoundlib_version());
}
