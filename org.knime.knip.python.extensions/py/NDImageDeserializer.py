# Deserializing ndarray as tiff stream

import tifffile
import NDImage

from io import BytesIO

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return NDImage.NDImage(tifffile.TiffFile(BytesIO(bytes)).asarray())
