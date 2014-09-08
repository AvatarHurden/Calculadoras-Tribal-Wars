package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Botão simples :(
 *
 * @author Wesley Nascimento
 */
public class TWSimpleButton extends TWButton {

    public TWSimpleButton() {
        super();
        this.foregroundNormal = Cores.hex2Rgb("#603000");
        this.borderNormal = Cores.hex2Rgb("#7d510f");
        this.foregroundOver = Cores.hex2Rgb("#e01f0f");
        this.backgroundPressed = Cores.hex2Rgb("#e8ddc2");

        float[] fractions = {0.0f, 0.22f, 1.0f};
        Color[] colors = {Cores.hex2Rgb("#FFFFFF"), Cores.hex2Rgb("#e3d5b3"), Cores.hex2Rgb("#e3d5b3")};

        this.paintColor = new LinearGradientPaint(new Point2D.Double(0, 0), new Point2D.Double(0, 100), fractions, colors);
    }

    public TWSimpleButton(String label) {
        this();
        setText(label);
    }

    public TWSimpleButton(Icon icon) {
        this();
        setIcon(icon);
    }
}