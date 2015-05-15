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
from pandas import DataFrame
from KNIPImage import KNIPImage
from scipy import ndimage

output_table = DataFrame()
res = []

for index, item in input_table['Img'].iteritems():
	input = item.array
	filtered = ndimage.gaussian_filter(input, 3)
	output = KNIPImage(filtered)
	res.append(output)

output_table['Res'] = res
```
Notes:

- In the above script, `cell` is an instance of [KNIPImage](https://github.com/knime-ip/knip-python-extensions/blob/master/org.knime.knip.python.extensions/py/KNIPImage.py).
  Further information/metadata could be defined in this class.

- Copying `output_table` from `input_table` will keep the table
  structure that KNIME expects intact. Since we are only interested in
  manipulating the arrays, we can do so without having to worry about
  creating a new table.
  
- In the above `copy()` operation, arrays are not copied. This is good,
  because (a) we save memory and (b) we loaded the image from a
  byte-stream and could not possibly change anything in the previous node.
  
- Keep in mind that we can edit `cell.array` in-place with
  e.g. `cell.array[0,:,:] = 1`.

