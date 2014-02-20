package selecionar_mundo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import custom_components.Ferramenta;

import database.Cores;

@SuppressWarnings("serial")
public class Informações_de_mundo extends JPanel {

	// Utilizando uma ferramenta apenas para a sua função de "getNextColor"
	private Ferramenta ferramenta_cor;

	private Properties prop;

	/**
	 * Basicamente uma tabela com as informações de um mundo específico
	 */
	public Informações_de_mundo() {

		// Only creates the panel to be added later

	}

	/**
	 * Muda as informações da tabela A lista de propriedades inserida não é
	 * exatamente igual à que será mostrada, pois ocorrem adaptações para
	 * facilitar a visualização
	 * 
	 * @param prop
	 *            Propriedades básicas
	 */
	public void changeProperties(Properties prop) {

		removeAll();

		this.prop = prop;

		ferramenta_cor = new Ferramenta();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 290 };
		gridBagLayout.rowHeights = new int[] {};
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		setOpaque(false);

		setBorder(new LineBorder(Cores.SEPARAR_ESCURO, 1, false));

		addProperties();

	}

	/**
	 * Adds the properties to the panel, in a predefined order
	 */
	private void addProperties() {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;

		gbc.gridy++;
		add(panelProperty("nome"), gbc);

		gbc.gridy++;
		add(panelProperty("velocidade"), gbc);

		gbc.gridy++;
		add(panelProperty("modificador"), gbc);

		gbc.gridy++;
		add(panelProperty("moral"), gbc);

		gbc.gridy++;
		add(panelProperty("pesquisaDeNiveis"), gbc);

		gbc.gridy++;
		add(panelProperty("igreja"), gbc);

		gbc.gridy++;
		add(panelProperty("bonusNoturno"), gbc);

		gbc.gridy++;
		add(panelProperty("bandeira"), gbc);

		gbc.gridy++;
		add(panelProperty("arqueiro"), gbc);

		gbc.gridy++;
		add(panelProperty("paladino"), gbc);

		if (Boolean.parseBoolean(prop.getProperty("paladino")) == true) {
			gbc.gridy++;
			add(panelProperty("itensAprimorados"), gbc);
		}

		gbc.gridy++;
		add(panelProperty("milicia"), gbc);

		gbc.gridy++;
		add(panelProperty("cunhagemDeMoedas"), gbc);

	}

	/**
	 * Returns the name of a property that will be displayed to the user
	 * (Displayed name is different from the input name, making this necessary)
	 */
	private String getPropertyName(String property) {

		switch (property) {
		case "nome":
			return "Nome";
		case "velocidade":
			return "Velocidade";
		case "modificador":
			return "Modificador de Unidade";
		case "moral":
			return "Moral";
		case "pesquisaDeNiveis":
			return "Sistema de Pesquisa";
		case "igreja":
			return "Igreja";
		case "bonusNoturno":
			return "Bônus Noturno";
		case "bandeira":
			return "Bandeiras";
		case "arqueiro":
			return "Arqueiros";
		case "paladino":
			return "Paladino";
		case "itensAprimorados":
			return "Itens Aprimorados";
		case "milicia":
			return "Milícia";
		case "cunhagemDeMoedas":
			return "Cunhagem de Moedas";
		default:
			return null;
		}

	}

	/**
	 * Returns the value of a property that will be displayed to the user
	 * (Displayed value is different depending on the property, and is different
	 * from the input value)
	 */
	private String getPropertyValue(String propertyName, String propertyValue) {

		switch (propertyName) {
		case "nome":
			return propertyValue;
		case "velocidade":
			return propertyValue;
		case "modificador":
			return propertyValue;
		case "moral":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "pesquisaDeNiveis":
			if (propertyValue.equals("true"))
				return "Pesquisa de 3 Níveis";
			else
				return "Pesquisa Simples";
		case "igreja":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "bonusNoturno":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "bandeira":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "arqueiro":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "paladino":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "itensAprimorados":
			if (propertyValue.equals("true"))
				return "Ativados";
			else
				return "Desativados";
		case "milicia":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		case "cunhagemDeMoedas":
			if (propertyValue.equals("true"))
				return "Ativado";
			else
				return "Desativado";
		default:
			return null;
		}
	}

	/**
	 * Cria o painel da propriedade, com o nome dela e seu valor
	 * 
	 * @param propName
	 *            Nome da propriedade
	 * @param propValue
	 *            Valor da propriedade
	 * @return Panel criado
	 */
	private JPanel panelProperty(String propName) {

		JPanel returnPanel = new JPanel();

		returnPanel.setBackground(ferramenta_cor.getNextColor());

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 150, 20, 120 };
		gbl_panel.rowHeights = new int[] { 20 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		returnPanel.setLayout(gbl_panel);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 5, 5, 5);

		JLabel lblName = new JLabel(getPropertyName(propName));

		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		returnPanel.add(lblName, gbc_panel);

		JLabel lblValue = new JLabel(getPropertyValue(propName,
				prop.getProperty(propName)));

		gbc_panel.gridx = 2;
		gbc_panel.gridy = 0;
		returnPanel.add(lblValue, gbc_panel);

		return returnPanel;

	}

}