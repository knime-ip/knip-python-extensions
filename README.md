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

The result is an increased memory footprint and additional processing time.


### Requirements
- Python2.7 or Python3.x (not tested)
- NumPy


### Example Python Script Node
In order to access the image data from e.g. an "Image Reader" node, a
"Python Script" node must contain the following lines:

```python
# optional, for numpy operations
import numpy

# output table contains the image data that will be streamed back to
# KNIME/Java
output_table = input_table.copy()

# The image data is saved in a pandas table
for column in input_table.columns:
    # column is the name of the column in the "Table Cell View"
    for index in input_table.index:
        # index is the row index of the "Table Cell View"
        
        # this is the content if the cell in the "Table Cell View":
        cell = input_table[column][index]
        
        # The n-dimensional numpy array representing the image
        # can be accessed via
        array = cell.array
```

In the above script, `cell` is an instance of [KNIPImage](https://github.com/knime-ip/knip-python-extensions/blob/master/org.knime.knip.python.extensions/py/KNIPImage.py).
Further information/metadata could be defined in this class.
