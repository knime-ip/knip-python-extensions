# Serializing TiffFile image on byte stream

import numpy as np
from skimage.io import imsave
from io import BytesIO

# Saves NDImage to buffer
def serialize(ndimage):
	buffer = BytesIO()
	array = ndimage.data

	imsave(buffer, array, plugin='tifffile')
	return buffer.getvalue()

