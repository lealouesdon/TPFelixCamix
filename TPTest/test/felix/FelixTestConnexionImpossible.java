package felix;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.util.NameComponentChooser;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import felix.communication.Connexion;
import felix.controleur.ControleurFelix;
import felix.vue.Fenetre;
import felix.vue.VueConnexion;

@RunWith(Parameterized.class)
public class FelixTestConnexionImpossible {

	private static JFrameOperator fenetre;

	private static VueConnexion vueConnexion;

	private static JTextFieldOperator adresseTextField;

	private static JTextFieldOperator portTextField;

	private static JTextFieldOperator messageTextField;

	private static JButtonOperator connexionButton;

	private static String ip;

	private static Integer port;

	private static ControleurFelix controleurFelix;

	/**
	 * Configuration des parametres inject�s lors de l'appel aux tests
	 * @return
	 */
	@Parameters(name = "dt[{index}] : {0}, {1}")
	public static Collection<Object[]> params() {
	    return Arrays.asList(new Object[][]{
	    	{"192.168.0.0", 12345}
	    });
	}

	public FelixTestConnexionImpossible(final String ip, final Integer port) {
	    FelixTestConnexionImpossible.ip = ip;
	    FelixTestConnexionImpossible.port = port;
	}

	@BeforeClass
	public static void setUp() throws InterruptedException{

		// Fixe les timeouts de Jemmy (http://wiki.netbeans.org/Jemmy_Operators_Environment#Timeouts),
		// ici : 3s pour l'affichage d'une frame.
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", timeout);

		// Cr�ation d'un mock de contr�leur.
		FelixTestConnexionImpossible.controleurFelix = EasyMock.createMock(ControleurFelix.class);
		Assert.assertNotNull(FelixTestConnexionImpossible.controleurFelix);

		// Cr�ation de la vue n�cessaire aux tests.
		// La vue s'appuie sur le mock de contr�leur.
		FelixTestConnexionImpossible.vueConnexion = new VueConnexion(controleurFelix);
		Assert.assertNotNull(FelixTestConnexionImpossible.vueConnexion);

		vueConnexion.affiche();
		//TimeUnit.SECONDS.sleep(2);
		FelixTestConnexionImpossible.recuperationVue();
	}

	private static void recuperationVue(){
		// TODO Auto-generated method stub
		// Index pour la r�cup�ration des widgets.
		Integer index = 0;

		// R�cup�ration de la fen�tre de la vue de la caisse (par son titre).
		FelixTestConnexionImpossible.fenetre = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"));
		Assert.assertNotNull("La fen�tre de la vue caisse n'est pas accessible.", FelixTestConnexionImpossible.fenetre);

		FelixTestConnexionImpossible.adresseTextField = new JTextFieldOperator(FelixTestConnexionImpossible.fenetre,new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_IP")));
		Assert.assertNotNull("Le champ de saisie de l'adresse n'est pas accessible.", FelixTestConnexionImpossible.adresseTextField);

		FelixTestConnexionImpossible.portTextField = new JTextFieldOperator(FelixTestConnexionImpossible.fenetre, new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_PORT")));
		Assert.assertNotNull("Le champ de saisie du port n'est pas accessible.", FelixTestConnexionImpossible.portTextField);

		FelixTestConnexionImpossible.connexionButton = new JButtonOperator(FelixTestConnexionImpossible.fenetre, Felix.CONFIGURATION.getString("FENETRE_CONNEXION_BOUTON_CONNECTER"));
		Assert.assertNotNull("Le bouton de connexion n'est pas accessible.", FelixTestConnexionImpossible.connexionButton);

		FelixTestConnexionImpossible.messageTextField = new JTextFieldOperator(FelixTestConnexionImpossible.fenetre, new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_NOM")));
		Assert.assertNotNull("Le champ de message n'est pas accessible.", FelixTestConnexionImpossible.messageTextField);


	}


	@Test
	public void testConnexionSansModification() throws InterruptedException {
		// ATTENTION A GERER LA COHERENCE DE L AFFICHAGE DES FENETRE avec l'ouverture de tchat si ca fail
		connexionButton.clickMouse();
		sleep(2);
		messageTextField.waitText(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), Felix.CONFIGURATION.getString("ADRESSE_CHAT"), Felix.CONFIGURATION.getString("PORT_CHAT")));
		//assertTrue(messageTextField.getText().equals(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), Felix.CONFIGURATION.getString("ADRESSE_CHAT"), Felix.CONFIGURATION.getString("PORT_CHAT"))));
	}

	@Test
	public void testConnexionModificationIp() throws InterruptedException {
		// ATTENTION A GERER LA COHERENCE DE L AFFICHAGE DES FENETRE avec l'ouverture de tchat si ca fail
		adresseTextField.enterText(ip);
		//adresseTextField.waitText(ip);
		connexionButton.clickMouse();
		sleep(2);
		messageTextField.waitText(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), ip, Felix.CONFIGURATION.getString("PORT_CHAT")));
		//assertTrue(messageTextField.getText().equals(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), ip, port)));
	}

	/*@Test
	public void testConnexionModificationPort() throws InterruptedException {
		// ATTENTION A GERER LA COHERENCE DE L AFFICHAGE DES FENETRE avec l'ouverture de tchat si ca fail
		adresseTextField.enterText(port);
		//adresseTextField.waitText(ip);
		connexionButton.clickMouse();
		sleep(2);
		messageTextField.waitText(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), ip, Felix.CONFIGURATION.getString("PORT_CHAT")));
		//assertTrue(messageTextField.getText().equals(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), ip, port)));
	}*/

	private void sleep(int second){
		try {
			TimeUnit.SECONDS.sleep(second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
