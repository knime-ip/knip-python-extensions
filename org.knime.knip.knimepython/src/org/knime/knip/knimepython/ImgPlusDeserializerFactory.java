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
public class ImgPlusDeserializerFactory extends DeserializerFactory {

	public ImgPlusDeserializerFactory() {
		super(ImgPlusCell.TYPE);
	}

	@Override
	public Deserializer createDeserializer() {
		return new Deserializer() {

			@Override
			public DataCell deserialize(final byte[] bytes, final FileStoreFactory fileStoreFactory)
					throws IOException {
				return new BytesToImgPlusConverter().deserialize(bytes, fileStoreFactory);
			}
		};
	}
}
