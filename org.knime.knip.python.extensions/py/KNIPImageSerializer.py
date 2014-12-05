# Serializing TiffFile image on byte stream

import numpy as np
from scipy.misc import toimage
from io import BytesIO
from PIL import Image

# Saves NDImage to buffer
def serialize(ndimage):
	buffer = BytesIO()
	array = ndimage.data
	toimage(array).save(buffer, 'tiff')
	return buffer.getvalue()

