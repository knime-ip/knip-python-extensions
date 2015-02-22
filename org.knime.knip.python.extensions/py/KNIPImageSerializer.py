# Serializing TiffFile image on byte stream

import numpy as np
from scipy.misc import imsave
from io import BytesIO

# Saves NDImage to buffer
def serialize(knipimage):
	buffer = BytesIO()
	imsave(buffer, knipimage.array, 'tiff')
	return buffer.getvalue()

