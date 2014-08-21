package io.github.avatarhurden.tribalwarsengine.tools;

import io.github.avatarhurden.tribalwarsengine.components.TWButton;
import io.github.avatarhurden.tribalwarsengine.components.TWSimpleButton;
import io.github.avatarhurden.tribalwarsengine.frames.SelectWorldFrame;
import io.github.avatarhurden.tribalwarsengine.objects.EditableObject;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.EditPanelCreator;
import io.github.avatarhurden.tribalwarsengine.tools.property_classes.OnChange;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;

import config.Lang;
import database.Cores;

/**
 * Creates a dialog that enables the user to edit a list of io.github.avatarhurden.tribalwarsengine.objects (that are compatible with the EditDialog)
 * <br>It remains always on top, and can allow the user to create new io.github.avatarhurden.tribalwarsengine.objects, delete io.github.avatarhurden.tribalwarsengine.objects and
 * change the order of the io.github.avatarhurden.tribalwarsengine.objects, besides editing existing ones.
 * <br>This class edits the original <code>List<//Object></code> provided, accessing the io.github.avatarhurden.tribalwarsengine.objects variables
 * through the <variable>variableName</variable> <code>Field</code>.
 *
 * @author Arthur
 */
@SuppressWarnings({"unchecked", "serial"})
public class EditDialog extends JDialog {

    EditableObject newObject;

    List<EditableObject> objects;
    
    LinkedHashMap<String, String> names;
    
    // Used for setting the visibility of the information panels
    List<ObjectInterface> interfaceList = new ArrayList<ObjectInterface>();
    JPanel namePanel;
    JScrollPane scroll;
    int listNumber = 0;
    JPanel informationPanel = new JPanel();
    JButton saveButton, upButton, downButton;
    private ObjectInterface selectedInterface;
    // Used if the list given has 0 length, turning this into the first "selected" interface
    private ObjectInterface nullInterface;

    //TODO reduce time to run program
    // 1 object = 991 milissegundos
    // 56 io.github.avatarhurden.tribalwarsengine.objects = 2218 milissegundos
    // teoricamente, a contrução sem objetos demora 968,69 milissegundos

    /**
     * Creates a dialog that enables the user to edit a list of io.github.avatarhurden.tribalwarsengine.objects (that are compatible with the EditDialog)
     * <br>It remains always on top, and can allow the user to create new io.github.avatarhurden.tribalwarsengine.objects, delete io.github.avatarhurden.tribalwarsengine.objects and
     * change the order of the io.github.avatarhurden.tribalwarsengine.objects, besides editing existing ones.
     * <br>This class edits the original <code>List<//Object></code> provided, accessing the object's variables
     * through the <variable>variableName</variable> <code>Field</code>.
     *
     * @param objects      <br><code>List<//Object></code> with the io.github.avatarhurden.tribalwarsengine.objects to be edited.
     * @param variableName <br>The name of the <code>Field</code> that contains the object's properties.
     * @param selected     <br>The object that will be selected on start.
     * @param newObject    <br>The object that will be used when a new object is added
     * @throws NoSuchFieldException     <br>If the provided <code>String</code> does not correspond to a <code>Field</code> of the
     *                                  io.github.avatarhurden.tribalwarsengine.objects
     * @throws SecurityException        <br>If the <code>Field</code> cannot be accessed.
     * @throws IllegalAccessException   //TODO document this
     * @throws IllegalArgumentException
     * @throws InstantiationException
     */
    public EditDialog(List<? extends EditableObject> objects, LinkedHashMap<String, String> names,
                      int selected, EditableObject newObject)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException {
    	
        this.newObject = newObject;
        this.objects = (List<EditableObject>) objects;
        this.names = names;
        
        setLayout(new GridBagLayout());
        
        getContentPane().setBackground(Cores.ALTERNAR_ESCURO);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // Create and add the right-side Panel
        getContentPane().add(makeScrollPanel(), c);

        // Creates an ObjectInterface for every object, adding it to the scrollPanel
        for (EditableObject o : objects) {
            createInterface(o);
            addInterfaceToScroll(interfaceList.get(objects.indexOf(o)), listNumber++);
        }

        // Makes the information panel as thick as any given objectInformation,
        // and also makes it as tall as necessary to fit an integer number of namePanels

        nullInterface = new ObjectInterface(newObject.getClass().newInstance());

        informationPanel.setBackground(Cores.ALTERNAR_ESCURO);

        // ScrollPane to add the informationPanel. Has scroll to allow for future additions of
        // options and stuff
        JScrollPane scroll = new JScrollPane(informationPanel);
        scroll.setPreferredSize(new Dimension(
                nullInterface.objectInformation.getPreferredSize().width + 15,
                19 * 32));

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUnitIncrement(11);

        c.gridy++;
        getContentPane().add(scroll, c);

        // Makes and adds the bottom panel, with the buttons
        c.gridy++;
        c.gridwidth = 2;
        getContentPane().add(makeEditPanel(), c);

        // Selects the desired object and puts the scroll in the necessary position
        // If there are no interfaces, select the "null" one.
        if (interfaceList.size() > 0)
            interfaceList.get(selected).setSelected(true);
        else
            nullInterface.setSelected(true);

        // Puts it always on top of the program
        setModal(true);

        // Packs the dialog and solidifies its size
        pack();
        setResizable(false);

        setLocationRelativeTo(null);

        //Sets the scroll in correct position
        scroll.getVerticalScrollBar().setValue(selected * 32);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void createInterface(EditableObject o) {

        ObjectInterface oi = new ObjectInterface(o);

        interfaceList.add(oi);

    }

    private void addInterfaceToScroll(ObjectInterface oi, int position) {

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridy = position;

        namePanel.add(oi.objectName, c);

        c.gridy = 0;
        informationPanel.add(oi.objectInformation, c);

    }

    private void removeInterfaceFromScroll(ObjectInterface oi) {

        namePanel.remove(oi.objectName);
        informationPanel.remove(oi.objectInformation);

    }

    private JScrollPane makeScrollPanel() {

        namePanel = new JPanel();
        namePanel.setBackground(Cores.FUNDO_CLARO);

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{140};
        layout.rowHeights = new int[]{20};
        layout.columnWeights = new double[]{1, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        namePanel.setLayout(layout);


        scroll = new JScrollPane(namePanel);
        scroll.setPreferredSize(new Dimension(160,
                informationPanel.getPreferredSize().height));

        // Half the size of a namePanel per unit
        scroll.getVerticalScrollBar().setUnitIncrement(11);

        return scroll;
    }

    private JPanel makeEditPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(Cores.FUNDO_CLARO);
        panel.setBorder(new MatteBorder(3, 0, 0, 0, Cores.SEPARAR_ESCURO));

        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{160, informationPanel.getPreferredSize().width};
        layout.rowHeights = new int[]{20};
        layout.columnWeights = new double[]{0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = 0;
        c.gridx = 0;

        JButton newButton = new TWButton(Lang.BtnNovo.toString());

        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                try {

                    EditableObject obj = newObject;
                    
                    newObject = newObject.getClass().newInstance();

                    // Puts object in the list
                    objects.add(obj);

                    // Creates the interface for the object
                    createInterface(obj);

                    // Adds interface to the scroll
                    addInterfaceToScroll(interfaceList.get(interfaceList.size() - 1),
                            interfaceList.size() - 1);

                    revalidate();

                    scroll.getVerticalScrollBar().setValue(
                            scroll.getVerticalScrollBar().getMaximum());

                    selectedInterface.setSelected(false);

                    interfaceList.get(interfaceList.size() - 1).setSelected(true);

                    selectedInterface.saveObejct();

                } catch (IllegalAccessException
                        | IllegalArgumentException | InstantiationException e) {
                    e.printStackTrace();
                }

            }
        });

        panel.add(newButton, c);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);

        saveButton = new TWSimpleButton(Lang.BtnSalvar.toString());

        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                selectedInterface.saveObejct();

            }
        });

        c.gridx++;
        rightPanel.add(saveButton, c);

        upButton = new TWSimpleButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/up_arrow.png"))));
        
        upButton.setPreferredSize(new Dimension(50,
        		saveButton.getPreferredSize().height));

        upButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                int position = objects.indexOf(selectedInterface.object);

                if (position > 0) {

                    Collections.swap(objects, position, position - 1);

                    Collections.swap(interfaceList, position, position - 1);

                    removeInterfaceFromScroll(interfaceList.get(position));
                    removeInterfaceFromScroll(interfaceList.get(position - 1));

                    addInterfaceToScroll(selectedInterface, position - 1);
                    addInterfaceToScroll(interfaceList.get(position), position);

                    setScrollPosition(scroll.getVerticalScrollBar(), true);

                    revalidate();

                    changeButtons();

                }

            }
        });

        rightPanel.add(upButton, c);

        downButton = new TWSimpleButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/down_arrow.png"))));

        downButton.setPreferredSize(new Dimension(50,
        		upButton.getPreferredSize().height));
        
        downButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                int position = objects.indexOf(selectedInterface.object);

                if (position < interfaceList.size() - 1) {

                    Collections.swap(objects, position, position + 1);

                    Collections.swap(interfaceList, position, position + 1);

                    removeInterfaceFromScroll(interfaceList.get(position));
                    removeInterfaceFromScroll(interfaceList.get(position + 1));

                    addInterfaceToScroll(selectedInterface, position + 1);
                    addInterfaceToScroll(interfaceList.get(position), position);

                    setScrollPosition(scroll.getVerticalScrollBar(), false);

//					scroll.getVerticalScrollBar().setValue(32*2);

                    revalidate();

                    changeButtons();

                }

            }
        });

        c.gridx++;
        rightPanel.add(downButton, c);

        JButton deleteButton = new TWSimpleButton(Lang.BtnDeletar.toString());

        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                String[] options = {Lang.Sim.toString(), Lang.Nao.toString()};

                int delete = JOptionPane.showOptionDialog(null,
                        new JLabel(Lang.DeleteOptionDialog.toString()),
                        null, JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);

                if (delete == JOptionPane.YES_OPTION) {

                    int position = interfaceList.indexOf(selectedInterface);

                    removeInterfaceFromScroll(selectedInterface);

                    interfaceList.remove(selectedInterface);
                    objects.remove(selectedInterface.object);

                    for (int i = position; i < interfaceList.size(); i++) {
                        removeInterfaceFromScroll(interfaceList.get(i));
                        addInterfaceToScroll(interfaceList.get(i), i);
                    }

                    // Se o último objeto foi deletado, seleciona o
                    // anterior. Caso contrário, o seguinte

                    if (interfaceList.size() != 0)
                        if (position == interfaceList.size())
                            interfaceList.get(position - 1).setSelected(true);
                        else
                            interfaceList.get(position).setSelected(true);

                    revalidate();
                    repaint();

                    changeButtons();

                }

            }
        });

        c.gridx++;
        rightPanel.add(deleteButton, c);

        c.gridx = 1;
        panel.add(rightPanel, c);

        return panel;

    }

    private void setScrollPosition(JScrollBar scrollBar, boolean up) {

        // This is the size of a single panel in the scroll
        final int SIZE = 32;

        // If the UP button was pressed and the interface's top is on top of the scrollbar
        if (up && (selectedInterface.objectName.getLocation().y <= scrollBar.getValue()))
            // Puts the scrollbar at the position of the interface, putting it up by a
            // full "page" of the scroll and then down by a panel
            scrollBar.setValue(interfaceList.indexOf(selectedInterface) * SIZE
                    - informationPanel.getPreferredSize().height + SIZE);

        // If the DOWN button was pressed and the interface's bottom is under the
        // bottom of the scroll (value+height)
        if (!up && (selectedInterface.objectName.getLocation().y + SIZE
                >= scrollBar.getValue() + informationPanel.getPreferredSize().height))
            // Sets the scrollbar at the top of the interface
            scrollBar.setValue(interfaceList.indexOf(selectedInterface) * SIZE);

    }

    // Turns the save, up and down buttons on or off as needed
    private void changeButtons() {

        saveButton.setEnabled(!selectedInterface.isSaved());

        upButton.setEnabled(interfaceList.indexOf(selectedInterface) != 0);

        downButton.setEnabled(interfaceList.indexOf(selectedInterface)
                != interfaceList.size() - 1);

    }

    private class ObjectInterface {

        /**
         * The object that this interface references.
         * <br> All editing and saving of the object is done in the <class>Object
         * Interface</class>.
         */
        private EditableObject object;

        private JPanel objectName;

        private EditPanelCreator objectInformation;
        
        private JTextField nameTextField;
        
        // Boolean that says if the object has been saved after any changes
        private boolean isSaved = true;

        // The symbol to be added to the objectName when it is unsaved
        private JLabel unsavedSignal = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                SelectWorldFrame.class.getResource("/images/asterísco_vermelho.png"))));

        // Object with the function that will be called when any one the Property's
        // "textFields" are changed
        private OnChange onChange;

        public ObjectInterface(EditableObject object) {

            this.object = object;

            onChange = new OnChange() {
                public void run() {
                    setSaved(false);
                }
            };

            createNamePanel(object.toString());

            createInformationPanel();

        }

        private void createNamePanel(String s) {

            objectName = new JPanel();
            objectName.add(new JLabel(s));

            objectName.setBackground(Cores.FUNDO_CLARO);
            objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

            objectName.addMouseListener(new MouseListener() {

                public void mouseReleased(MouseEvent arg0) {
                }

                public void mousePressed(MouseEvent arg0) {
                }

                public void mouseExited(MouseEvent arg0) {
                }

                public void mouseEntered(MouseEvent arg0) {
                }

                public void mouseClicked(MouseEvent arg0) {

                    selectedInterface.setSelected(false);

                    setSelected(true);

                }
            });

        }

        private void createInformationPanel() {
        	
        	objectInformation = new EditPanelCreator(object.getJson(), names, onChange);
        	
        	nameTextField = objectInformation.getNameTextField();
        	
        	objectInformation.setBackground(Cores.ALTERNAR_ESCURO);
            objectInformation.setVisible(false);

        }

        public void setSelected(boolean isSelected) {

            if (isSelected) {
                objectInformation.setVisible(true);
                objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                objectName.setBackground(Cores.FUNDO_ESCURO);
                
                selectedInterface = this;
                changeButtons();

            } else {
                objectInformation.setVisible(false);
                objectName.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                objectName.setBackground(Cores.FUNDO_CLARO);
            }
        }

        private void saveObejct() {

            if (isUniqueName(nameTextField.getText())) {

                objectInformation.setValues();
                setSaved(true); 
                
            } else {

                String s = nameTextField.getText();

                do {

                    s = (String) (JOptionPane.showInputDialog(null,
                            new JLabel(Lang.NomeUsadoAviso.toString()),
                            Lang.NomeUsado.toString(), JOptionPane.ERROR_MESSAGE, null, null, s));

                } while (!isUniqueName(s));

                if (s != null) {
                    nameTextField.setText(s);
                    saveObejct();
                } else
                    setSaved(false);

            }


        }

        private boolean isUniqueName(String s) {

            for (Object o : objects)
                if (o.toString().equals(s) && o != object)
                    return false;

            return true;

        }

        private boolean isSaved() {
            return isSaved;
        }

        private void setSaved(boolean saved) {

            isSaved = saved;

            if (saved)
                objectName.remove(unsavedSignal);
            else
                objectName.add(unsavedSignal, 0);

            objectName.revalidate();
            objectName.repaint();

            changeButtons();
        }

        public String toString() {
            return object.toString();
        }

    }

}
