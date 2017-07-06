package org.knime.knip.serialization;

import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.filestore.FileStoreFactory;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.io.ScifioGateway;

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
 * Deserializing ImgPlus instances from byte stream. Used format is .tif.
 * 
 * TODO: Use a Scijava Service in the background such that one could replace the
 * way images are serialized/deserialized
 * 
 * @author Christian Dietz (University of Konstanz)
 */

public class BytesToImgPlusConvertor {
	
	/**
	 * ImgOpener to read ImgPlus from stream
	 */
	private ImgOpener m_imgOpener;
	private SCIFIOConfig m_scifioConfig;
	
	private Format m_format;
	private Parser m_parser;
		
	/**
	 * Constructor
	 */
	public BytesToImgPlusConvertor() {
		m_imgOpener = new ImgOpener(ScifioGateway.getSCIFIO().context());
		m_scifioConfig = new SCIFIOConfig();
		m_scifioConfig.groupableSetGroupFiles(false);
		m_scifioConfig.imgOpenerSetComputeMinMax(false);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataCell deserialize(final byte[] bytes, final FileStoreFactory fileStoreFactory)
			throws IOException {
		
		if(m_format == null || m_parser == null)
			createFormatAndParser();

		final ImgPlusCellFactory factory = new ImgPlusCellFactory(fileStoreFactory);

		// TODO this hack should be removed in the future, as soon as
		// filename bug is resolved
		// We have to set the filename as a NPE would be thrown
		// otherwise
		final RandomAccessInputStream stream = new RandomAccessInputStream(
				ScifioGateway.getSCIFIO().getContext(), bytes) {
			@Override
			public String getFileName() {
				return "";
			}
		};

		try {

			final Metadata metadata = m_parser.parse(stream, m_scifioConfig);
			metadata.setSource(stream);

			final Reader reader = m_format.createReader();
			reader.setMetadata(metadata);
			reader.setSource(stream, m_scifioConfig);

			return factory.createCell((ImgPlus) m_imgOpener.openImgs(reader, m_scifioConfig).get(0));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createFormatAndParser()
	{
		try {
			m_format = ScifioGateway.getSCIFIO().format().getFormat(".tif");
			m_parser = m_format.createParser();
		} catch (FormatException e) {
			throw new RuntimeException(e);
		}
	}

}
