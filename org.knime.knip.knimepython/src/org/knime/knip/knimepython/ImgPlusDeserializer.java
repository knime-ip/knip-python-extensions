package org.knime.knip.knimepython;

import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.filestore.FileStoreFactory;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.io.ScifioGateway;
import org.knime.knip.serialization.BytesToImgPlusConvertor;
import org.knime.python.typeextension.Deserializer;
import org.knime.python.typeextension.DeserializerFactory;

import io.scif.Format;
import io.scif.FormatException;
import io.scif.Metadata;
import io.scif.Parser;
import io.scif.Reader;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgOpener;
import io.scif.io.RandomAccessInputStream;
import net.imagej.ImgPlus;

/**
 * Deserializer using the extension points in org.knime.python.
 * 
 * @author Clemens von Schwerin, KNIME.com, Konstanz, Germany
 */
public class ImgPlusDeserializer extends DeserializerFactory {

	private BytesToImgPlusConvertor m_convertor;

	public ImgPlusDeserializer() {
		super(ImgPlusCell.TYPE);
		m_convertor = new BytesToImgPlusConvertor();
	}

	@Override
	public Deserializer createDeserializer() {

		return new Deserializer() {

			@Override
			public DataCell deserialize(final byte[] bytes, final FileStoreFactory fileStoreFactory)
					throws IOException {

				return m_convertor.deserialize(bytes, fileStoreFactory);
			}
		};
	}
}
