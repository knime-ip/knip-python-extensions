package org.knime.knip.knimepython;

import java.io.IOException;

import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.serialization.ImgPlusToBytesConvertor;
import org.knime.python.typeextension.Serializer;
import org.knime.python.typeextension.SerializerFactory;

/**
 * Serializer using the extension points in org.knime.python.
 * 
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */
@SuppressWarnings("rawtypes")
public class ImgPlusSerializer extends SerializerFactory<ImgPlusValue> {

	private final ImgPlusToBytesConvertor m_convertor;
	/**
	 * Constructor
	 */
	public ImgPlusSerializer() {
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
