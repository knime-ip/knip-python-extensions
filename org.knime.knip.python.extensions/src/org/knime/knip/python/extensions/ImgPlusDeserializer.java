package org.knime.knip.python.extensions;

import io.scif.Format;
import io.scif.FormatException;
import io.scif.Metadata;
import io.scif.Parser;
import io.scif.Reader;
import io.scif.img.ImgOpener;
import io.scif.io.RandomAccessInputStream;

import java.io.IOException;

import net.imglib2.meta.ImgPlus;

import org.knime.core.data.DataCell;
import org.knime.core.data.filestore.FileStoreFactory;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.python.typeextension.Deserializer;
import org.knime.python.typeextension.DeserializerFactory;

/**
 * Deserializing ImgPlus instances from byte stream. Used format is .tif.
 * 
 * TODO: Use a Scijava Service in the background such that one could replace the
 * way images are serialized/deserialized
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class ImgPlusDeserializer extends DeserializerFactory {

	/**
	 * {@link KNIPPythonGateway}
	 */
	private KNIPPythonGateway m_kp = KNIPPythonGateway.instance();

	/**
	 * ImgOpener to read ImgPlus from stream
	 */
	private ImgOpener m_imgOpener;

	public ImgPlusDeserializer() {
		super(ImgPlusCell.TYPE);
		m_imgOpener = new ImgOpener(m_kp.context());
	}

	@Override
	public Deserializer createDeserializer() {

		return new Deserializer() {

			private final Format m_format;
			private final Parser m_parser;
			{
				try {
					m_format = m_kp.formatService().getFormat(".tif");
					m_parser = m_format.createParser();
				} catch (FormatException e) {
					throw new RuntimeException(e);
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public DataCell deserialize(final byte[] bytes,
					final FileStoreFactory fileStoreFactory) throws IOException {

				final ImgPlusCellFactory factory = new ImgPlusCellFactory(
						fileStoreFactory);

				// TODO this hack should be removed in the future, as soon as
				// filename bug is resolved
				// We have to set the filename as a NPE would be thrown
				// otherwise
				final RandomAccessInputStream stream = new RandomAccessInputStream(
						m_kp.context(), bytes){
					@Override
					public String getFileName() {
						return "";
					}
				};

				try {

					final Metadata metadata = m_parser.parse(stream,
							m_kp.scifioConfig());
					metadata.setSource(stream);

					final Reader reader = m_format.createReader();
					reader.setMetadata(metadata);
					reader.setSource(stream, m_kp.scifioConfig());

					return factory.createCell((ImgPlus) m_imgOpener.openImgs(
							reader, m_kp.scifioConfig()).get(0));
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
}
