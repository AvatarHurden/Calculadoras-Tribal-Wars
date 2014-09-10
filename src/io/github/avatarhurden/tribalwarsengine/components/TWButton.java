package io.github.avatarhurden.tribalwarsengine.components;

import io.github.avatarhurden.tribalwarsengine.enums.Cores;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

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

    protected float[] fractions = {0.0f, 0.22f, 0.3f, 1.0f};
    protected Color[] colors = {Cores.hex2Rgb("#947A62"), Cores.hex2Rgb("#7B5C3D"), Cores.hex2Rgb("#6C4824"), Cores.hex2Rgb("#6C4824")};

    protected LinearGradientPaint paintColor = new LinearGradientPaint(new Point2D.Double(0, 0), new Point2D.Double(0, 100), fractions, colors);

    protected Color backgroundUnable = Color.lightGray;
    protected Color backgroundPressed = Cores.hex2Rgb("#947A62");

    protected Color foregroundNormal = Color.WHITE;
    protected Color foregroundUnable = Color.darkGray;
    protected Color foregroundOver = null;


    protected Color borderNormal = Color.black;
    protected Color borderUnable = Color.darkGray;

    private boolean isOver = false;
    private boolean isPressed = false;
    
    public TWButton() {
    	   	
    	setBackground(new Color(220, 220, 220));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 0);
        setBorder(border);
        addMouseListener(this);
        setContentAreaFilled(false);
    }
    
    public TWButton(String label) {
    	
    	this();
        setText(label);
     
        /*
        font-family: Verdana,Arial;
        font-size: 12px;
        font-weight: bold;
         */
        Font font = new Font("VErdana,Arial", Font.BOLD, 12);
        this.setFont( font );
    }
    
    public TWButton(Icon icon) {
    	
    	this();
    	setIcon(icon);
    	
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        //Se estiver desativo
        if( !isEnabled() ){
            g2d.setColor( backgroundUnable );
        }
        //Se estiver sendo pressionado
        else if (isPressed && backgroundPressed != null){
        	g2d.setColor( backgroundPressed );
        }
        //Se estiver normal!
        else {
            g2d.setPaint(paintColor);
        }
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);

        //Desenha a borda do botão
        if (!isEnabled()) {
            g2d.setColor(borderUnable);
        } else {
            g2d.setColor(borderNormal);
        }
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
        
        //Se o mouse estiver em cima
        if (isOver || isDefaultButton()) {
        	g2d.setColor(borderUnable);
        	g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 5, 5);
        }

        //Escreve o texto centralizado
        if(!isEnabled()) {
            g2d.setColor( foregroundUnable );
        } else if (isOver && foregroundOver != null) {
            g2d.setColor(foregroundOver);
        } else {
            g2d.setColor( foregroundNormal );
        }

        Font f = getFont();
        if (f != null) {
            FontMetrics fm = getFontMetrics(getFont());
            g2d.drawString( getText(), getWidth() / 2 - fm.stringWidth( getText()) / 2, getHeight() / 2 + (fm.getMaxAscent() - fm.getMaxDescent()) / 2);
        }
        
        if (getIcon() != null) {
        	getIcon().paintIcon(this, g2d, getWidth() / 2 - getIcon().getIconWidth()/ 2, 
        			getHeight() / 2 - getIcon().getIconHeight()/ 2);
        }
    }

    /**
     * O tamanho preferido do botão.
     */
    @Override
    public Dimension getPreferredSize() {
    	// Permite a edição de tamanho apenas para botões com ícones
    	if (getIcon() != null)
    		return super.getPreferredSize();
    	
    	// Caso tenha texto maior do que o tamanho padrão, ajusta com 10 de cada lado
    	if (super.getPreferredSize().width > 60)
    		return new Dimension(super.getPreferredSize().width + 20, 25);
    	else
    		return new Dimension(80, 25);
    
    }

    /**
     * O tamanho minimo para o botão,
     * Previne que o botão fique apertado...
     */
    @Override
    public Dimension getMinimumSize() {
    	// Permite a edição de tamanho apenas para botões com ícones
    	if (getIcon() == null)
    		return new Dimension(80, 25);
    	else
    		return super.getMinimumSize();
    }
    
    @Override 
    public void doClick() {
    	isPressed = true;
    	super.doClick();
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
    	isPressed = true;
    	isOver = false;
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
    	if (!isPressed)
    		isOver = true;
        isPressed = false;
    }

    public void mouseExited(MouseEvent e) {
        isOver = false;
        isPressed = false;
    }
}
