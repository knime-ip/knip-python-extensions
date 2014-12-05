package org.knime.knip.python.extensions;

import net.imglib2.converter.Converter;
import net.imglib2.converter.read.ConvertedRandomAccessibleInterval;
import net.imglib2.img.ImgView;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.Unsigned12BitType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import org.knime.core.node.NodeLogger;

/**
 * Simple class for converting images if required. TODO: This code can be
 * partially replaced by ImageJ-Ops in the future
 * 
 * @author Christian Dietz (University of Konstanz)
 *
 */
public class TypeUtils {

	private static final NodeLogger LOGGER = NodeLogger
			.getLogger(TypeUtils.class);

	/**
	 * Default Constructor
	 */
	public TypeUtils() {
		// Constructor
	}

	public static ImgPlus<?> converted(final ImgPlus<?> in) {

		final Object type = in.firstElement();

		// convert is required
		if (type instanceof LongType) {
			return convertedImgPlus(in, new ShortType());
		} else if (type instanceof Unsigned12BitType) {
			return convertedImgPlus(in, new UnsignedShortType());
		} else if (type instanceof BitType) {
			return convertedImgPlus(in, new ByteType());
		}

		return in;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ImgPlus<?> convertedImgPlus(ImgPlus<?> in,
			RealType<?> outType) {
		triggerWarning(in.firstElement().getClass().getSimpleName(), outType
				.getClass().getSimpleName());
		return new ImgPlus(new ImgView(new ConvertedRandomAccessibleInterval(
				in, createConverter(), outType), in.factory()));
	}

	private static void triggerWarning(String inType, String outType) {
		LOGGER.warn("Images of type "
				+ inType
				+ " are not supported in the 'Python Script' Node.\n Automatically converting to "
				+ outType
				+ ". \n However, we suggest to use the 'Image Converter' Node prior to the 'Python Script Node' to control the type conversion manually.");
	}

	private static Converter<RealType<?>, RealType<?>> createConverter() {
		return new Converter<RealType<?>, RealType<?>>() {
			@Override
			public void convert(RealType<?> input, RealType<?> output) {
				input.setReal(output.getRealDouble());
			}
		};
	}
}
