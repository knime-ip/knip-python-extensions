# Deserializing ndarray as tiff stream

import KNIPImage
from io import BytesIO
from PIL import Image

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return Image.open(BytesIO(bytes))
