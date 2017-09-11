package org.knime.knip.knimepython;

import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.filestore.FileStoreFactory;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.serialization.BytesToImgPlusConverter;
import org.knime.python.typeextension.Deserializer;
import org.knime.python.typeextension.DeserializerFactory;


/**
 * Deserializer using the extension points in org.knime.python.
 * 
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */
public class ImgPlusDeserializer extends DeserializerFactory {

	private final BytesToImgPlusConverter m_converter;

	public ImgPlusDeserializer() {
		super(ImgPlusCell.TYPE);
		m_converter = new BytesToImgPlusConverter();
	}

	@Override
	public Deserializer createDeserializer() {
		return new Deserializer() {

			@Override
			public DataCell deserialize(final byte[] bytes, final FileStoreFactory fileStoreFactory)
					throws IOException {
				return m_converter.deserialize(bytes, fileStoreFactory);
			}
		};
	}
}
