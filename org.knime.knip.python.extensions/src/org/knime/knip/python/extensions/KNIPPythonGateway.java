package org.knime.knip.python.extensions;

import io.scif.Format;
import io.scif.SCIFIO;
import io.scif.Translator;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgUtilityService;
import io.scif.services.FormatService;
import io.scif.services.TranslatorService;

import org.scijava.Context;
import org.scijava.plugin.PluginIndex;
import org.scijava.plugin.PluginInfo;
import org.scijava.service.Service;

/**
 * Gateway to {@link Context} and several dedicated services used by the
 * KNIP-Python-Extensions
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class KNIPPythonGateway {

	/**
	 * Singleton
	 */
	private static KNIPPythonGateway instance;

	/**
	 * {@link FormatService} used to get .tif format
	 */
	private final FormatService m_formatService;

	/**
	 * {@link ImgUtilityService}
	 */
	private final ImgUtilityService m_utilityService;

	/**
	 * {@link TranslatorService}
	 */
	private final TranslatorService m_translatorService;

	/**
	 * {@link SCIFIOConfig} used to read/write images
	 */
	private final SCIFIOConfig m_scifioConfig;

	/**
	 * {@link Context} used in this gateway
	 */
	private final Context m_context;

	/**
	 * {@link SCIFIO} scifio instance
	 */
	private final SCIFIO m_scifio;

	/**
	 * Private Constructor
	 */
	private KNIPPythonGateway() {

		// Override services for faster execution
		final PluginIndex pluginIndex = new Context().getPluginIndex();

		pluginIndex.add(new PluginInfo<Service>(FasterLocationService.class,
				Service.class));

		pluginIndex.add(new PluginInfo<Format>(FasterTIFFFormat.class,
				Format.class));

		pluginIndex.add(new PluginInfo<Translator>(
				FasterTIFFFormat.TIFFTranslator.class, Translator.class));

		pluginIndex.add(new PluginInfo<Service>(FasterStatusService.class,
				Service.class));

		pluginIndex.add(new PluginInfo<Service>(FasterEventService.class,
				Service.class));

		m_context = new Context(pluginIndex);

		m_formatService = m_context.getService(FormatService.class);

		m_utilityService = m_context.service(ImgUtilityService.class);

		m_translatorService = m_context.service(TranslatorService.class);

		// create ScifioConfig
		m_scifioConfig = new SCIFIOConfig();

		m_scifioConfig.groupableSetGroupFiles(false);

		m_scifioConfig.imgOpenerSetComputeMinMax(false);
		
		// setup scifio
		m_scifio = new SCIFIO(m_context);

	}

	/**
	 * @return {@link SCIFIOConfig} to read images
	 */
	public SCIFIOConfig scifioConfig() {
		return m_scifioConfig;
	}

	/**
	 * @return {@link FormatService}
	 */
	public FormatService formatService() {
		return m_formatService;
	}

	/**
	 * @return {@link ImgUtilityService}
	 */
	public ImgUtilityService utilityService() {
		return m_utilityService;
	}

	/**
	 * @return {@link TranslatorService}
	 */
	public TranslatorService translatorService() {
		return m_translatorService;
	}
	
	public SCIFIO scifio(){
		return m_scifio;
	}

	/**
	 * @return {@link Context}
	 */
	public Context context() {
		return m_context;
	}

	public static KNIPPythonGateway instance() {
		if (instance == null)
			instance = new KNIPPythonGateway();

		return instance;
	}
}
