# Runtime package audit

Run after `do_image_complete` using the deployed image manifest:

```sh
sh check-image.sh /path/to/myir-image-emmc-myd-jmx95x-b0.rootfs.manifest
```

The check rejects development/static-development packages, target C/C++
compilers and unwanted QEMU/Xen/ntp packages. It also checks a small set of
required MYIR applications and graphics/OP-TEE utilities. It only reports
problems; it does not remove packages or alter dependency resolution.

Do not run this check on an SDK manifest. SDK development libraries and host
tools are required. Use `../sdk-smoke/check-sdk.sh` with an installed SDK to
check C/C++, Qt, OpenCV, EGL/GLES, GBM and OpenCL compilation and linking.

Passing this package check does not prove target boot or hardware behavior.
