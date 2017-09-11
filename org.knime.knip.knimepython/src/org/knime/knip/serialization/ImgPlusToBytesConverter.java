package org.knime.knip.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.io.ScifioGateway;
import org.knime.knip.knimepython.TypeUtils;

import io.scif.DefaultImageMetadata;
import io.scif.DefaultMetadata;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.Metadata;
import io.scif.Translator;
import io.scif.Writer;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgIOException;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import io.scif.io.ByteArrayHandle;
import io.scif.io.RandomAccessOutputStream;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.CalibratedAxis;

/**
 * Serializing ImgPlus instances to byte stream. Used format is .tif.
 *
 * TODO: Use a Scijava Service in the background such that one could replace the
 * way images are serialized/deserialized
 * 
 * @author Christian Dietz (University of Konstanz)
 *
 */
public class ImgPlusToBytesConverter {

	/**
	 * ImgSaver to write ImgPlus to stream as tif
	 */
	private ImgSaver m_saver;

	/**
	 * SCIFIO config to read/write images
	 */
	private SCIFIOConfig m_scifioConfig;
	
	private Writer m_writer;

	public ImgPlusToBytesConverter() {
		m_saver = new ImgSaver(ScifioGateway.getSCIFIO().getContext());
		m_scifioConfig = new SCIFIOConfig();
		m_scifioConfig.groupableSetGroupFiles(false);
		m_scifioConfig.imgOpenerSetComputeMinMax(false);
	}
	
	public byte[] serialize(final ImgPlusValue<?> value) throws IOException {
		if(m_writer == null) {
			m_writer = createWriter();
		}
		
		final ImgPlus<?> imgPlus = TypeUtils.converted(value.getImgPlus());

		try {
			final ByteArrayHandle handle = new ByteArrayHandle();
			populateMeta(m_writer, imgPlus, m_scifioConfig, 0);
			// HACK Corresponds to filename
			m_writer.getMetadata().setDatasetName("");
			m_writer.setDest(new RandomAccessOutputStream(handle), 0);

			m_saver.saveImg(m_writer, imgPlus.getImg(), m_scifioConfig);

			m_writer.close();

			return handle.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Could not serialize image. Possible reasons: Unsupported image type, dimensionality of the image,...");
		}
	}
	
	private Writer createWriter()
	{
		try {
			return ScifioGateway.format().getWriterByExtension(".tif");
		} catch (FormatException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method is copied from SCIFIO
	 * 
	 * FIXME/TODO: Remove when method available in SCIFIO (see
	 * https://github.com/scifio/scifio/issues/233)
	 * 
	 * Uses the provided {@link SCIFIOImgPlus} to populate the minimum metadata
	 * fields necessary for writing.
	 * 
	 * @param imageIndex
	 */
	private void populateMeta(final Writer w, final ImgPlus<?> img, final SCIFIOConfig config, final int imageIndex)
			throws FormatException, IOException, ImgIOException {

		final Metadata meta = w.getFormat().createMetadata();

		// Get format-specific metadata
		Metadata imgMeta = ScifioGateway.getSCIFIO().imgUtil().makeSCIFIOImgPlus(img).getMetadata();

		final List<ImageMetadata> imageMeta = new ArrayList<ImageMetadata>();

		if (imgMeta == null) {
			imgMeta = new DefaultMetadata();
			imgMeta.createImageMetadata(1);
			imageMeta.add(imgMeta.get(0));
		} else {
			for (int i = 0; i < imgMeta.getImageCount(); i++) {
				imageMeta.add(new DefaultImageMetadata());
			}
		}

		// Create Img-specific ImageMetadatas
		final int pixelType = ScifioGateway.getSCIFIO().imgUtil().makeType(img.firstElement());

		// TODO is there some way to consolidate this with the isCompressible
		// method?
		final CalibratedAxis[] axes = new CalibratedAxis[img.numDimensions()];
		img.axes(axes);

		final long[] axisLengths = new long[img.numDimensions()];
		img.dimensions(axisLengths);

		for (int i = 0; i < imageMeta.size(); i++) {
			final ImageMetadata iMeta = imageMeta.get(i);
			iMeta.populate(img.getName(), Arrays.asList(axes), axisLengths, pixelType, true, false, false, false, true);

			// Adjust for RGB information
			if (img.getCompositeChannelCount() > 1) {
				if (config.imgSaverGetWriteRGB()) {
					iMeta.setPlanarAxisCount(3);
				}
				iMeta.setAxisType(2, Axes.CHANNEL);
				// Split Axes.CHANNEL if necessary
				if (iMeta.getAxisLength(Axes.CHANNEL) > img.getCompositeChannelCount()) {
					iMeta.addAxis(Axes.get("Channel-planes", false),
							iMeta.getAxisLength(Axes.CHANNEL) / img.getCompositeChannelCount());
					iMeta.setAxisLength(Axes.CHANNEL, img.getCompositeChannelCount());
				}
			}
		}

		// Translate to the output metadata
		final Translator t = ScifioGateway.getSCIFIO().translator().findTranslator(imgMeta, meta, false);

		t.translate(imgMeta, imageMeta, meta);

		w.setMetadata(meta);
	}

}
