# Deserializing ndarray as tiff stream

import KNIPImage
from io import BytesIO

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return KNIPImage.KNIPImage(scipy.ndimage.imread(BytesIO(bytes)))
