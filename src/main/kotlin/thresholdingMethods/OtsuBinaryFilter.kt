package thresholdingMethods

import java.awt.image.BufferedImage


object OtsuBinaryFilter : AbstractBufferedImageOp() {
	override fun filter(src: BufferedImage, dest: BufferedImage?): BufferedImage {
		var destF = dest
		val width = src.width
		val height = src.height
		if (destF == null) destF = createCompatibleDestImage(src, null)
		// Imagen en escala de grises
		val inPixels = IntArray(width * height)
		val outPixels = IntArray(width * height)
		getRGB(src, 0, 0, width, height, inPixels)
		var index = 0
		for (row in 0 until height) {
			var ta: Int
			var tr: Int
			var tg: Int
			var tb: Int
			for (col in 0 until width) {
				index = row * width + col
				ta = inPixels[index] shr 24 and 0xff
				tr = inPixels[index] shr 16 and 0xff
				tg = inPixels[index] shr 8 and 0xff
				tb = inPixels[index] and 0xff
				val gray = (0.299 * tr + 0.587 * tg + 0.114 * tb).toInt()
				inPixels[index] = ta shl 24 or (gray shl 16) or (gray shl 8) or gray
			}
		}
		// Obtener el histograma
		val histogram = IntArray(256)
		for (row in 0 until height) {
			var tr = 0
			for (col in 0 until width) {
				index = row * width + col
				tr = inPixels[index] shr 16 and 0xff
				histogram[tr]++
			}
		}
		// Método de umbralización de binarización de imagen-OTSU
		val total = (width * height).toDouble()
		val variances = DoubleArray(256)
		for (i in variances.indices) {
			var bw = 0.0
			var bmeans = 0.0
			var bvariance = 0.0
			var count = 0.0
			for (t in 0 until i) {
				count += histogram[t]
				bmeans += (histogram[t] * t).toDouble()
			}
			bw = count / total
			bmeans = if (count == 0.0) 0.0 else bmeans / count
			for (t in 0 until i) {
				bvariance += Math.pow(t - bmeans, 2.0) * histogram[t]
			}
			bvariance = if (count == 0.0) 0.0 else bvariance / count
			var fw: Double
			var fmeans = 0.0
			var fvariance = 0.0
			count = 0.0
			for (t in i until histogram.size) {
				count += histogram[t]
				fmeans += (histogram[t] * t).toDouble()
			}
			fw = count / total
			fmeans = if (count == 0.0) 0.0 else fmeans / count
			for (t in i until histogram.size) {
				fvariance += Math.pow(t - fmeans, 2.0) * histogram[t]
			}
			fvariance = if (count == 0.0) 0.0 else fvariance / count
			variances[i] = bw * bvariance + fw * fvariance
		}

		// find the minimum within class variance
		var min = variances[0]
		var threshold = 0
		for (m in 1 until variances.size) {
			if (min > variances[m]) {
				threshold = m
				min = variances[m]
			}
		}
		// Binarización
		println("Valor final del histograma: $threshold")
		for (row in 0 until height) {
			for (col in 0 until width) {
				index = row * width + col
				var gray = inPixels[index] shr 8 and 0xff
				if (gray > threshold) {
					gray = 255
					outPixels[index] = 0xff shl 24 or (gray shl 16) or (gray shl 8) or gray
				} else {
					gray = 0
					outPixels[index] = 0xff shl 24 or (gray shl 16) or (gray shl 8) or gray
				}
			}
		}
		setRGB(destF, 0, 0, width, height, outPixels)
		return destF
	}

	init {
		println("Umbralización binaria por el método Otsu...")
	}
}