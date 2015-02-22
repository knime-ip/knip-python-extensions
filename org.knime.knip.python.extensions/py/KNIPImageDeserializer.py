# Deserializing ndarray as tiff stream

import KNIPImage
from io import BytesIO
from scipy.ndimage import imread

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return KNIPImage.KNIPImage(imread(BytesIO(bytes)))
