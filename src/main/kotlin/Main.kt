import gui.JFrameImage
import open.OpenImage
import thresholdingMethods.OtsuBinaryFilter

fun main(){

    val imageOriginal = OpenImage.openImage()
    val auxOriginal = imageOriginal?.let { JFrameImage(it, "Imagen original") }
    val auxBuffer = imageOriginal?.let { OpenImage.toBufferedImage(it) }
    val auxBufferR = auxBuffer?.let { OtsuBinaryFilter.filter(it, null) }
    val imageResult = auxBufferR?.let { OpenImage.toImage(it) }
    val auxResult = imageResult.let { JFrameImage(it, "Imagen umbralizada") }

}