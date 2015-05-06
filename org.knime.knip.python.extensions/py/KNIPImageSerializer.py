# Serializing TiffFile image on byte stream

import sys
import os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

import numpy as np
from tifffile import imsave
from io import BytesIO
from fakefile import FakeFile

# Saves NDImage to buffer
def serialize(knipimage):
	buffer = BytesIO()
	imsave(buffer, knipimage.array,close_file=False)
	return buffer.getvalue()

