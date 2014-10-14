package io.github.avatarhurden.tribalwarsengine.ferramentas.distância;

import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.components.TimeFormattedJLabel;
import io.github.avatarhurden.tribalwarsengine.enums.Cores;
import io.github.avatarhurden.tribalwarsengine.enums.Imagens;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PlanejadorHorárioPanel extends JPanel {

    private JSpinner dateSpinner, hourSpinner;

    private TimeFormattedJLabel respostaLabel;

    private JPanel topPanel, bottomPanel;
    private boolean inputTop = true;
    
    private JPanel topButton, bottomButton;
    
    private OnChange onChange;

    public PlanejadorHorárioPanel(OnChange onChange) {
        this.onChange = onChange;

        setOpaque(false);

        setBorder(new LineBorder(Cores.SEPARAR_ESCURO));
        
        makeGUI();
       
        changeDate(0);
    }

    private void makeGUI() {
    	 GridBagLayout gridBagLayout = new GridBagLayout();
         gridBagLayout.columnWidths = new int[]{0};
         gridBagLayout.rowHeights = new int[]{0};
         gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
         gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
         setLayout(gridBagLayout);

         GridBagConstraints c = new GridBagConstraints();
         c.insets = new Insets(10, 10, 10, 10);
         c.anchor = GridBagConstraints.CENTER;
         c.gridy = 0;
         c.gridx = 0;
         
         add(new JLabel("Planejador de Ataque"), c);

         c.gridy++;
         c.fill = GridBagConstraints.HORIZONTAL;
         add(makeInputPanel(), c);
         
         JButton invert = new TWSimpleButton(new ImageIcon(Imagens.getImage("switch_arrow.png")));
         
         invert.setPreferredSize(new Dimension(54,26));
         
         invert.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent arg0) {
                 invertInOut();
             }
         });

         c.gridy++;
         c.fill = GridBagConstraints.NONE;
         add(invert, c);

         c.gridy++;
         c.fill = GridBagConstraints.HORIZONTAL;
         add(makeOutputPanel(), c);
    }
    
    private JPanel makeInputPanel() {

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setOpaque(false);

        // Add label de último ataque
        JPanel ataquePanel = new JPanel();
        ataquePanel.add(new JLabel("Horário de Chegada"));

        ataquePanel.setBackground(Cores.FUNDO_ESCURO);
        ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1, Cores.SEPARAR_ESCURO));

        panel.add(ataquePanel);

        // Add panel de horário

        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                onChange.run();
            }
        });

        hourSpinner = new JSpinner(new SpinnerDateModel());
        hourSpinner.setEditor(new JSpinner.DateEditor(hourSpinner, "HH:mm:ss"));
        hourSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
            	onChange.run();
            }
        });

        topPanel = new JPanel();
        topPanel.add(dateSpinner);
        topPanel.add(hourSpinner);

        topPanel.setBackground(Cores.ALTERNAR_ESCURO);
        topPanel.setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));

        panel.add(topPanel);

        return panel;

    }

    private JPanel makeOutputPanel() {

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setOpaque(false);

        // Add panel de último ataque
        JPanel ataquePanel = new JPanel();
        ataquePanel.add(new JLabel("Enviar ataque em"));

        ataquePanel.setBackground(Cores.FUNDO_ESCURO);
        ataquePanel.setBorder(new MatteBorder(1, 1, 1, 1, Cores.SEPARAR_ESCURO));

        panel.add(ataquePanel);

        // Add panel de horário

        respostaLabel = new TimeFormattedJLabel(false);

        bottomPanel = new JPanel();
        bottomPanel.add(respostaLabel);

        bottomPanel.setBackground(Cores.ALTERNAR_ESCURO);
        bottomPanel.setBorder(new MatteBorder(0, 1, 1, 1, Cores.SEPARAR_ESCURO));

        panel.add(bottomPanel);
        
        return panel;
    }

    private void invertInOut() {

        long input, output;

        // Gets the dateSpinner part of the time
        input = TimeUnit.MILLISECONDS.toDays(((Date) dateSpinner.getModel().getValue()).getTime());
        input = TimeUnit.DAYS.toMillis(input);

        // Gets the hours part of the time                  days*minutes*millis
        input += ((Date) hourSpinner.getModel().getValue()).getTime() % (24 * 3600 * 1000);

        output = respostaLabel.getDate().getTime();

        dateSpinner.getModel().setValue(new Date(output));
        hourSpinner.getModel().setValue(new Date(output));

        respostaLabel.setDate(new Date(input));

        // Sabemos que o input é a data a ser enviado
        if (inputTop) {

            topPanel.remove(dateSpinner);
            topPanel.remove(hourSpinner);

            bottomPanel.remove(respostaLabel);

            topPanel.add(respostaLabel);

            bottomPanel.add(dateSpinner);
            bottomPanel.add(hourSpinner);

            inputTop = false;
            topButton.setVisible(true);
            bottomButton.setVisible(false);
            
        } else {

            bottomPanel.remove(dateSpinner);
            bottomPanel.remove(hourSpinner);

            topPanel.remove(respostaLabel);

            topPanel.add(dateSpinner);
            topPanel.add(hourSpinner);

            bottomPanel.add(respostaLabel);

            inputTop = true;
            topButton.setVisible(false);
            bottomButton.setVisible(true);

        }

        repaint();
    }

    protected void changeDate(long tempo) {

        long input;

        // Gets the dateSpinner part of the time
        input = TimeUnit.MILLISECONDS.toDays(((Date) dateSpinner.getModel().getValue()).getTime());
        input = TimeUnit.DAYS.toMillis(input);

        // Gets the hours part of the time                  days*minutes*millis
        input += ((Date) hourSpinner.getModel().getValue()).getTime() % (24 * 3600 * 1000);

        if (inputTop)
            respostaLabel.setDate(new Date(input - tempo));
        else
            respostaLabel.setDate(new Date(input + tempo));
    }
    
    protected TimeFormattedJLabel getDateLabel() {
    	return respostaLabel;
    }
    
    protected void setButtons(JPanel top, JPanel bottom) {
    	topButton = top;
    	bottomButton = bottom;
    	
    	if (inputTop)
    		topButton.setVisible(false);
    	else
    		bottomButton.setVisible(false);
    }

}
