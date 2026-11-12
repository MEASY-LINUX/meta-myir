if [ "${TERM:-}" = "dumb" ] && [ "$(tty 2>/dev/null)" = "/dev/ttyLP0" ]; then
    TERM=vt220
    export TERM
fi
