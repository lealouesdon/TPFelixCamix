package felix;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.util.NameComponentChooser;

import felix.Felix;
import felix.controleur.ControleurFelix;
import felix.vue.VueConnexion;

@RunWith(Parameterized.class)
public class FelixTestConnexionPossible {

	private static ClassReference application;

	private static String[] parametres;

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
	 * Configuration des parametres injectés lors de l'appel aux tests
	 * @return
	 */
	@Parameters(name = "dt[{index}] : {0}, {1}")
	public static Collection<Object[]> params() {
		final Object[][] data = new Object[][] {
			{"127.0.0.1",12345}// L'application cible n'est pas camix
		};
    	return Arrays.asList(data);
	}

	public FelixTestConnexionPossible(final String ip, final Integer port) {
	    FelixTestConnexionPossible.ip = ip;
	    FelixTestConnexionPossible.port = port;
	}

	@BeforeClass
	public static void setUp() throws Exception{

		// Fixe les timeouts de Jemmy (http://wiki.netbeans.org/Jemmy_Operators_Environment#Timeouts),
		// ici : 3s pour l'affichage d'une frame ou une attente de changement d'état (waitText par exemple).
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", timeout);

		// Démarrage de l'instance de Felix nécessaire aux tests.
		try {
			FelixTestConnexionPossible.application = new ClassReference("felix.Felix");
			FelixTestConnexionPossible.parametres = new String[1];
			FelixTestConnexionPossible.parametres[0] = ""; //"-b" en mode bouchonné, "" en mode collaboration avec Camix;

			lanceInstance();

			// 10 secondes d'observation par suspension du thread (objectif pédagogique)
			// (pour prendre le temps de déplacer les fenêtres à l'écran).
			final Long timeoutObs = Long.valueOf(1000);
			Thread.sleep(timeoutObs);
		}
		catch (ClassNotFoundException e) {
			Assert.fail("Problème d'accès à la classe invoquée : " + e.getMessage());
			throw e;
		}
	}

	private static void lanceInstance() throws Exception
    {
        try {
            // Lancement d'une application.
            FelixTestConnexionPossible.application.startApplication(FelixTestConnexionPossible.parametres);
        }
        catch (InvocationTargetException e) {
            Assert.fail("Problème d'invocation de l'application : " + e.getMessage());
            throw e;
        }
        catch (NoSuchMethodException e) {
            Assert.fail("Problème d'accès à la méthode invoquée : " + e.getMessage());
            throw e;
        }
        recuperationVue();
}

	private static void recuperationVue(){
		// TODO Auto-generated method stub
		// Index pour la récupération des widgets.
		Integer index = 0;

		// Récupération de la fenêtre de la vue de la caisse (par son titre).
		FelixTestConnexionPossible.fenetre = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"));
		Assert.assertNotNull("La fenêtre de la vue caisse n'est pas accessible.", FelixTestConnexionPossible.fenetre);

		FelixTestConnexionPossible.adresseTextField = new JTextFieldOperator(FelixTestConnexionPossible.fenetre,new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_IP")));
		Assert.assertNotNull("Le champ de saisie de l'adresse n'est pas accessible.", FelixTestConnexionPossible.adresseTextField);

		FelixTestConnexionPossible.portTextField = new JTextFieldOperator(FelixTestConnexionPossible.fenetre, new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_PORT")));
		Assert.assertNotNull("Le champ de saisie du port n'est pas accessible.", FelixTestConnexionPossible.portTextField);

		FelixTestConnexionPossible.connexionButton = new JButtonOperator(FelixTestConnexionPossible.fenetre, Felix.CONFIGURATION.getString("FENETRE_CONNEXION_BOUTON_CONNECTER"));
		Assert.assertNotNull("Le bouton de connexion n'est pas accessible.", FelixTestConnexionPossible.connexionButton);

		FelixTestConnexionPossible.messageTextField = new JTextFieldOperator(FelixTestConnexionPossible.fenetre, new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_NOM")));
		Assert.assertNotNull("Le champ de message n'est pas accessible.", FelixTestConnexionPossible.messageTextField);
	}

	@Test
	public void testConnexionPossible() throws InterruptedException, IOException {
		// ATTENTION A GERER LA COHERENCE DE L AFFICHAGE DES FENETRE avec l'ouverture de tchat si ca fail
		portTextField.enterText(port.toString());
		adresseTextField.enterText(ip);
		connexionButton.clickMouse();
		messageTextField.waitText(String.format(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_CONNEXION"), ip, port));
		sleep(2);
	}



	private void sleep(int second){
		try {
			TimeUnit.SECONDS.sleep(second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
