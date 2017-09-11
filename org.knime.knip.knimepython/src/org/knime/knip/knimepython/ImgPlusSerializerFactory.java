package org.knime.knip.knimepython;

import java.io.IOException;

import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.serialization.ImgPlusToBytesConverter;
import org.knime.python.typeextension.Serializer;
import org.knime.python.typeextension.SerializerFactory;

/**
 * Serializer using the extension points in org.knime.python.
 * 
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */
@SuppressWarnings("rawtypes")
public class ImgPlusSerializerFactory extends SerializerFactory<ImgPlusValue> {

	/**
	 * Constructor
	 */
	public ImgPlusSerializerFactory() {
		super(ImgPlusValue.class);
	}

	@Override
	public Serializer<? extends ImgPlusValue<?>> createSerializer() {
		return new Serializer<ImgPlusValue<?>>() {

			@Override
			public byte[] serialize(final ImgPlusValue<?> value) throws IOException {
				return new ImgPlusToBytesConverter().serialize(value);
			}
		};
	}
}
