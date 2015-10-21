# Deserializing ndarray as tiff stream
from io import BytesIO
import os
import sys

# make sure we are importing tifffile and KNIPImage from this directory
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import KNIPImage
from tifffile import TiffFile

# deserialize TiffFile from ByteStream
def deserialize(bytes):
	return KNIPImage.KNIPImage(TiffFile(BytesIO(bytes)).asarray())
