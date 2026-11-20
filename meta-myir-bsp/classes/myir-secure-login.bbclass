# SPDX-License-Identifier: MIT

IMAGE_FEATURES:remove = "allow-empty-password empty-root-password serial-autologin-root"

do_rootfs[depends] += "shadow-native:do_populate_sysroot"
do_rootfs[file-checksums] += "${MYIR_ROOT_PASSWORD_HASH_FILE}:True"
ROOTFS_POSTPROCESS_COMMAND:append = " myir_set_root_password"

python myir_set_root_password () {
    import re
    import subprocess
    from pathlib import Path

    hash_file = Path(d.getVar("MYIR_ROOT_PASSWORD_HASH_FILE"))
    try:
        password_hash = hash_file.read_text(encoding="ascii").rstrip("\n")
    except (OSError, UnicodeError):
        bb.fatal("Secure login requires a readable SHA-512 password hash file: %s" % hash_file)

    if not re.fullmatch(r"\$6\$(?:rounds=[0-9]+\$)?[./A-Za-z0-9]{1,16}\$[./A-Za-z0-9]{85}[./01]", password_hash):
        bb.fatal("Secure login requires a single unescaped SHA-512 password hash in %s" % hash_file)

    chpasswd = Path(d.getVar("STAGING_SBINDIR_NATIVE")) / "chpasswd"
    subprocess.run([str(chpasswd), "--encrypted", "--root", d.getVar("IMAGE_ROOTFS")],
                   input="root:" + password_hash + "\n", text=True, check=True)
}
