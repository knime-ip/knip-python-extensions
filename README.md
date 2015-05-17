KNIP Python Bindings Extension (BETA)
====================

KNIME Image Processing Python Bindings to use ImgPlus in the KNIME Python extension. 


### Problem
KNIME is written in Java while NumPy is a CPython extension. 
See also:
- http://jyni.org/ 
- https://github.com/Stewori/JyNI
- http://www.jython.org/


### Current solution
We use a [patched version](https://github.com/knime-ip/knip-python-extensions/blob/master/org.knime.knip.python.extensions/py/tifffile.py)
of [tifffile.py](http://www.lfd.uci.edu/~gohlke/code/tifffile.py.html)
by [Christoph Goehlke](http://www.lfd.uci.edu/~gohlke/)
to byte-stream images from and to Python. 

This results in an increased memory footprint and additional processing time.


### Requirements
- Python2.7 or Python3.x (not tested)
- NumPy


### Example Python Script Node
In order to access the image data from e.g. an "Image Reader" node, a
"Python Script" node must contain the following lines:

```python
from KNIPImage import KNIPImage
from scipy import ndimage

# Copy structure of incoming KNIME table
output_table = input_table.copy()

# Create empty output_column
output_column = []

# Loop over every cell in the 'Img' column
for index,input_cell in input_table['Img'].iteritems():

	# get image from cell
	image = input_cell.array

	# apply some operation of image, here a Gaussian filtering
	filtered = ndimage.gaussian_filter(image, 3)

	# Write result back into a KNIPImage
	output_cell = KNIPImage(filtered)

	# Append output_cell to output array
	output_column.append(output_cell)

# Set output_column in output_table
output_table['Img'] = output_column
```
Notes:

- In the above script, `input_cell` and `output_cell` are instances of [KNIPImage](https://github.com/knime-ip/knip-python-extensions/blob/master/org.knime.knip.python.extensions/py/KNIPImage.py).
  Further information/metadata could be defined in this class.

- Copying `output_table` from `input_table` will keep the table
  structure that KNIME expects intact. Since we are only interested in
  manipulating the arrays, we can do so without having to worry about
  creating a new table.
  
- In the above `copy()` operation, arrays are not copied. This is good,
  because (a) we save memory and (b) we loaded the image from a
  byte-stream and could not possibly change anything in the previous node.
  
- Keep in mind that we can edit `input_cell.array` in-place with
  e.g. `input_cell.array[0,:,:] = 1`.

