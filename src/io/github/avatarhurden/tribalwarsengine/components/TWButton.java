package io.github.avatarhurden.tribalwarsengine.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

/**
 *  Botão customizado com o estilo do jogo
 *
 * @author Wesley Nascimento
 */
public class TWButton extends JButton implements MouseListener {
    /**
     * Cores em degrade do botão "ataque" localizado na praça,
     *
     * #947A62 0%, #7B5C3D 22%, #6C4824 30%, #6C4824 100%
     *
     */
    private Color backgroundOver = hex2Rgb("#947A62");
    private Color backgroundNormal = hex2Rgb("#6C4824");
    private Color backgroundUnable = Color.lightGray;
    private float[] fractions = {0.0f, 0.22f, 0.3f, 1.0f};
    private Color[] colors = {hex2Rgb("#947A62"), hex2Rgb("#7B5C3D"), hex2Rgb("#6C4824"), hex2Rgb("#6C4824")};

    private LinearGradientPaint paintColor = new LinearGradientPaint( new Point2D.Double(0, 0), new Point2D.Double(0, 100),fractions, colors);

    private Color foregroundNormal = Color.WHITE;
    private Color foregroundUnable = Color.darkGray;
    private boolean isOver = false;

    public TWButton(String label) {
        setText(label);
        setBackground(new Color(220, 220, 220));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 0);
        setBorder(border);
        addMouseListener(this);
        setContentAreaFilled(false);

        /*
        font-family: Verdana,Arial;
        font-size: 12px;
        font-weight: bold;
         */
        Font font = new Font("VErdana,Arial", Font.BOLD, 12);
        this.setFont( font );
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //Desenha o interior do botão

        //Se estiver desativo
        if( !isEnabled() ){
            g2d.setColor( backgroundUnable );
        }
        //Se o mouse estiver encima
        else if( isOver ){
            g2d.setColor( backgroundOver );
        }
        //Se estiver normal!
        else {
            g2d.setPaint(paintColor);
        }
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);

        //Desenha a borda do botão
        g2d.setColor( getBackground().darker().darker().darker() );
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);

        if(!isEnabled()) {
            g2d.setColor( foregroundUnable );
        } else {
            g2d.setColor( foregroundNormal );
        }

        //Escreve o texto centralizado
        Font f = getFont();
        if (f != null) {
            FontMetrics fm = getFontMetrics(getFont());
            g2d.drawString( getText(), getWidth() / 2 - fm.stringWidth( getText()) / 2, getHeight() / 2 + fm.getMaxDescent());
        }
    }

    /**
     * O tamanho preferido do botão.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(80, 30);
    }

    /**
     * O tamanho minimo para o botão,
     * Previne que o botão fique apertado...
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(80, 30);
    }

    /**
     * Converte uma cor em hexadecimal para RGB
     * O local adequado para colocar esse metodo não seria aqui. Depois eu movo para outro lugar
     *
     * @param colorStr - Uma cor em hex, exemplo: #FFFFFF
     * @return color - Color object
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
        isOver = true;
    }

    public void mouseExited(MouseEvent e) {
        isOver = false;
    }
}
