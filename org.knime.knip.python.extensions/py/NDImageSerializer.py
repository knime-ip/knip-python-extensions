# Serializing TiffFile image on byte stream

from scipy.misc import imsave
import numpy
import NDImage
from io import BytesIO

# Saves NDImage to buffer
def serialize(item):
	buffer = BytesIO()
	array = numpy.array(item.data.getdata(),numpy.uint8).reshape(item.data.size[1], item.data.size[0], 3)
	imsave(buffer,array,'tif')
	return buffer.getvalue()
