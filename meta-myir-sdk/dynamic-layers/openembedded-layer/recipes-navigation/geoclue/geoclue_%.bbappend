# The cellular modem is managed by quectel-CM. Disable GeoClue modem
# sources to avoid pulling ModemManager into the target image.
PACKAGECONFIG:remove:myd-jmx95x-b0 = "3g modem-gps cdma"
