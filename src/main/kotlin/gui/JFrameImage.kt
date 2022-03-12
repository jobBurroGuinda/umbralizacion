package gui

import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel

class JFrameImage() : JFrame() {

    private val label: JLabel = JLabel()


    constructor(imageOriginal: Image?):this(){
        label.icon = ImageIcon(imageOriginal)
        if (imageOriginal != null) {
            setSize(imageOriginal.getWidth(null), imageOriginal.getHeight(null))
        }
    }
    constructor(imageOriginal: Image?, _title:String):this(){
        label.icon = ImageIcon(imageOriginal)
        if (imageOriginal != null) {
            setSize(imageOriginal.getWidth(null), imageOriginal.getHeight(null))
        }
        title += _title
    }

    init {
        //setSize(700,600)
        add(label)
        isVisible = true
        setLocationRelativeTo(null)
        title = "Job Adolfo Salinas Hernández / 2020670044. "
    }

}