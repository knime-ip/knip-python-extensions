# Deserializing ndarray as tiff stream

import KNIPImage
from io import BytesIO
from PIL import Image

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return KNIPImage.KNIPImage(Image.open(BytesIO(bytes)))
