package org.knime.knip.python.extensions;

import java.io.IOException;

import org.knime.core.data.image.png.PNGImageValue;
import org.knime.python.typeextension.Serializer;
import org.knime.python.typeextension.SerializerFactory;

public class PNGSerializer extends SerializerFactory<PNGImageValue> {

	public PNGSerializer() {
		super(PNGImageValue.class);
	}

	@Override
	public Serializer<? extends PNGImageValue> createSerializer() {
		return new Serializer<PNGImageValue>() {

			@Override
			public byte[] serialize(PNGImageValue value) throws IOException {
				return value.getImageContent().getByteArray();
			}
		};
	}
}
