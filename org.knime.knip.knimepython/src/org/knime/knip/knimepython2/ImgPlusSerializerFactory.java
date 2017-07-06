package org.knime.knip.knimepython2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.io.ScifioGateway;
import org.knime.knip.knimepython.TypeUtils;
import org.knime.knip.serialization.ImgPlusToBytesConvertor;
import org.knime.python2.typeextension.Serializer;
import org.knime.python2.typeextension.SerializerFactory;

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
 * Serializer using the extension points in org.knime.python2.
 * 
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */
public class ImgPlusSerializerFactory extends SerializerFactory<ImgPlusValue>{
	
	private final ImgPlusToBytesConvertor m_convertor;
	/**
	 * Constructor
	 */
	public ImgPlusSerializerFactory() {
		super(ImgPlusValue.class);
		m_convertor = new ImgPlusToBytesConvertor();
	}

	@Override
	public Serializer<? extends ImgPlusValue<?>> createSerializer() {

		return new Serializer<ImgPlusValue<?>>() {

			@Override
			public byte[] serialize(final ImgPlusValue<?> value) throws IOException {
					return m_convertor.serialize(value);
			}
		};
	}
}
