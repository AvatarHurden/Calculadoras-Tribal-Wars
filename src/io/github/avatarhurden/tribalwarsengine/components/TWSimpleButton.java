package io.github.avatarhurden.tribalwarsengine.components;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Botão simples :(
 *
 * @author Wesley Nascimento
 */
public class TWSimpleButton extends TWButton {

    public TWSimpleButton(String label) {
        super(label);
        this.foregroundNormal = this.hex2Rgb("#603000");
        this.borderNormal = this.hex2Rgb("#7d510f");
        this.foregroundOver = this.hex2Rgb("#e01f0f");
        this.backgroundPressed = this.hex2Rgb("#e8ddc2");

        float[] fractions = {0.0f, 0.22f, 1.0f};
        Color[] colors = {hex2Rgb("#FFFFFF"), hex2Rgb("#e3d5b3"), hex2Rgb("#e3d5b3")};

        this.paintColor = new LinearGradientPaint(new Point2D.Double(0, 0), new Point2D.Double(0, 100), fractions, colors);
    }
}
