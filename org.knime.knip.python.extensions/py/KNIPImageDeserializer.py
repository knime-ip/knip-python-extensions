# Deserializing ndarray as tiff stream
import sys
import os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

import KNIPImage
from io import BytesIO
import numpy as np
from tifffile import TiffFile

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	
	return KNIPImage.KNIPImage(TiffFile(BytesIO(bytes)).asarray())
