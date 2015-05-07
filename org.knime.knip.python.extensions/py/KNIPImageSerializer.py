# Serializing TiffFile image on byte stream
from io import BytesIO
import os
import sys

# make sure we are importing tifffile from this directory
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from tifffile import imsave

# Saves ndarray to buffer
def serialize(knipimage):
	buffer = BytesIO()
	imsave(buffer, knipimage.array, close_file=False)
	return buffer.getvalue()

