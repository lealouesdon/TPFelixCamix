package felix;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextField;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.util.NameComponentChooser;

import felix.Felix;
import felix.controleur.ControleurFelix;
import felix.vue.VueConnexion;

@RunWith(Parameterized.class)
public class FelixTestConnectionPossible {

	private static JFrameOperator fenetre;

	private static VueConnexion vueConnexion;

	private static JTextFieldOperator adresseTextField;

	private static JTextFieldOperator portTextField;

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

	public FelixTestConnectionPossible(final String ip, final Integer port) {
		FelixTestConnectionPossible.ip = ip;
		FelixTestConnectionPossible.port = port;
	}

	@BeforeClass
	public static void setUp() throws InterruptedException{

		// Fixe les timeouts de Jemmy (http://wiki.netbeans.org/Jemmy_Operators_Environment#Timeouts),
		// ici : 3s pour l'affichage d'une frame.
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", timeout);


		// Cr�ation d'un mock de contr�leur.
		FelixTestConnectionPossible.controleurFelix = EasyMock.createMock(ControleurFelix.class);
		Assert.assertNotNull(FelixTestConnectionPossible.controleurFelix);

		// Cr�ation de la vue n�cessaire aux tests.
		// La vue s'appuie sur le mock de contr�leur.
		FelixTestConnectionPossible.vueConnexion = new VueConnexion(controleurFelix);
		Assert.assertNotNull(FelixTestConnectionPossible.vueConnexion);

		vueConnexion.affiche();
		//TimeUnit.SECONDS.sleep(2);
		FelixTestConnectionPossible.recuperationVue();
	}

	private static void recuperationVue(){
		// TODO Auto-generated method stub
		// Index pour la recuperation des widgets.
		Integer index = 0;

		// R�cup�ration de la fen�tre de la vue de la caisse (par son titre).
		FelixTestConnectionPossible.fenetre = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"));
		Assert.assertNotNull("La fenetre de la vue caisse n'est pas accessible.", FelixTestConnectionPossible.fenetre);

		FelixTestConnectionPossible.adresseTextField = new JTextFieldOperator(FelixTestConnectionPossible.fenetre,new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_IP")));
		Assert.assertNotNull("Le champ de saisie de l'adresse n'est pas accessible.", FelixTestConnectionPossible.adresseTextField);

		FelixTestConnectionPossible.portTextField = new JTextFieldOperator(FelixTestConnectionPossible.fenetre, new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_PORT")));
		Assert.assertNotNull("Le champ de saisie du port n'est pas accessible.", FelixTestConnectionPossible.portTextField);

		FelixTestConnectionPossible.connexionButton = new JButtonOperator(FelixTestConnectionPossible.fenetre, Felix.CONFIGURATION.getString("FENETRE_CONNEXION_BOUTON_CONNECTER"));
		Assert.assertNotNull("Le bouton de connexion n'est pas accessible.", FelixTestConnectionPossible.connexionButton);
	}


	@Test
	public void testOuvertureSansModification001() throws InterruptedException {
		// ATTENTION A GERER LA COHERENCE DE L AFFICHAGE DES FENETRE avec l'ouverture de tchat si ca fail
		TimeUnit.SECONDS.sleep(2);
		FelixTestConnectionPossible.adresseTextField.enterText("127.0.0.155");
		TimeUnit.SECONDS.sleep(2);
	}

	
}
