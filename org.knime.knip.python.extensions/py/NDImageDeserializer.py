# Deserializing ndarray as tiff stream

import NDImage
from io import BytesIO
from PIL import Image

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return NDImage.NDImage(Image.open(BytesIO(bytes)))
