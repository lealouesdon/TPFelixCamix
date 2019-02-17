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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTextPaneOperator;
import org.netbeans.jemmy.util.NameComponentChooser;

import felix.Felix;
import felix.controleur.ControleurFelix;
import felix.vue.VueConnexion;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FelixTestConnexionPossible {

	private static final int NBINSTANCES = 2;

	private static ClassReference application;

	private static String[] parametres;

	private static JFrameOperator[] fenetre = new JFrameOperator[NBINSTANCES];

	private static JFrameOperator[] fenetreChat = new JFrameOperator[NBINSTANCES];

	private static VueConnexion vueConnexion;

	private static JTextFieldOperator[] adresseTextField = new JTextFieldOperator[NBINSTANCES];

	private static JTextFieldOperator[] portTextField= new JTextFieldOperator[NBINSTANCES];

	private static JTextFieldOperator[] messageTextField= new JTextFieldOperator[NBINSTANCES];

	private static JTextFieldOperator[] saisieTextField= new JTextFieldOperator[NBINSTANCES];

	private static JTextPaneOperator[] messagesTextPan= new JTextPaneOperator[NBINSTANCES];

	private static JButtonOperator[] connexionButton= new JButtonOperator[NBINSTANCES];

	private static String ip[] = {"127.0.0.1","127.0.0.1"};

	private static Integer port[] = {12345,12345};

	private static ControleurFelix[] controleurFelix;


	public FelixTestConnexionPossible() {
//	    FelixTestConnexionPossible.ip = "127.0.0.1";
//	    FelixTestConnexionPossible.port = 12345;
	}

	@BeforeClass
	public static void setUp() throws Exception{

		// Fixe les timeouts de Jemmy (http://wiki.netbeans.org/Jemmy_Operators_Environment#Timeouts),
		// ici : 3s pour l'affichage d'une frame ou une attente de changement d'�tat (waitText par exemple).
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", timeout);

		// D�marrage de l'instance de Felix n�cessaire aux tests.
		try {
			FelixTestConnexionPossible.application = new ClassReference("felix.Felix");
			FelixTestConnexionPossible.parametres = new String[1];
			FelixTestConnexionPossible.parametres[0] = ""; //"-b" en mode bouchonn�, "" en mode collaboration avec Camix;

			lanceInstancesFelix();
		}
		catch (ClassNotFoundException e) {
			Assert.fail("Probl�me d'acc�s � la classe invoqu�e : " + e.getMessage());
			throw e;
		}
	}

	/*@After
	public void clean(){
		for(int i = 0; i<NBINSTANCES; i++ ){
			saisieTextField[i].enterText("/q");
		}
	}*/

	private static void lanceInstancesFelix() throws Exception{
		for(int i = 0; i<NBINSTANCES; i++ ){
			lanceInstance(i);
			final Long timeout = Long.valueOf(2000);
			Thread.sleep(timeout);
		}
	}

	private static void lanceInstance(int index) throws Exception
    {
        try {
            FelixTestConnexionPossible.application.startApplication(FelixTestConnexionPossible.parametres);
        }
        catch (InvocationTargetException e) {
            Assert.fail("Probl�me d'invocation de l'application : " + e.getMessage());
            throw e;
        }
        catch (NoSuchMethodException e) {
            Assert.fail("Probl�me d'acc�s � la m�thode invoqu�e : " + e.getMessage());
            throw e;
        }
        recuperationVue(index);
        connexionCamix(index);
        recuperationVueChat(index);
    }

	private static void connexionCamix(int index){
		// Connexion � camix
        portTextField[index].enterText(port[index].toString());
		adresseTextField[index].enterText(ip[index]);
		connexionButton[index].clickMouse();
	}

	private static void recuperationVue(int index){

		// R�cup�ration de la fen�tre de la vue de connexion (par son titre).
		FelixTestConnexionPossible.fenetre[index] = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"));
		Assert.assertNotNull("La fen�tre de connexion n'est pas accessible.", FelixTestConnexionPossible.fenetre);

		FelixTestConnexionPossible.adresseTextField[index] = new JTextFieldOperator(FelixTestConnexionPossible.fenetre[index],new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_IP")));
		Assert.assertNotNull("Le champ de saisie de l'adresse n'est pas accessible.", FelixTestConnexionPossible.adresseTextField);

		FelixTestConnexionPossible.portTextField[index] = new JTextFieldOperator(FelixTestConnexionPossible.fenetre[index], new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_SAISIE_PORT")));
		Assert.assertNotNull("Le champ de saisie du port n'est pas accessible.", FelixTestConnexionPossible.portTextField);

		FelixTestConnexionPossible.connexionButton[index] = new JButtonOperator(FelixTestConnexionPossible.fenetre[index], Felix.CONFIGURATION.getString("FENETRE_CONNEXION_BOUTON_CONNECTER"));
		Assert.assertNotNull("Le bouton de connexion n'est pas accessible.", FelixTestConnexionPossible.connexionButton);

		FelixTestConnexionPossible.messageTextField[index] = new JTextFieldOperator(FelixTestConnexionPossible.fenetre[index], new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_MESSAGE_NOM")));
		Assert.assertNotNull("Le champ de message n'est pas accessible.", FelixTestConnexionPossible.messageTextField);
	}

	private static void recuperationVueChat(int index){

		FelixTestConnexionPossible.fenetreChat[index] = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CHAT_TITRE"));
		Assert.assertNotNull("La fen�tre de chat n'est pas accessible.", FelixTestConnexionPossible.fenetreChat[index]);

		FelixTestConnexionPossible.saisieTextField[index] = new JTextFieldOperator(FelixTestConnexionPossible.fenetreChat[index],new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CHAT_SAISIE_MESSAGE")));
		Assert.assertNotNull("Le champ de saisie de message n'est pas accessible.", FelixTestConnexionPossible.saisieTextField);

		FelixTestConnexionPossible.messagesTextPan[index] = new JTextPaneOperator(FelixTestConnexionPossible.fenetreChat[index],new NameComponentChooser(Felix.CONFIGURATION.getString("FENETRE_CHAT_AFFICHAGE_MESSAGE")));
		Assert.assertNotNull("L'affichage des messages n'est pas accessible.", FelixTestConnexionPossible.messagesTextPan);

	}

	/**
	 * Test si les fenetres de chat ont bien �t� ouvertes pour toutes les instances et donc que la connexion a �t� ouverte avec Camix.
	 * V�rifie �galement que les fenetres de connexions ont �t� ferm�es
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test
	public void test001Connexion() throws InterruptedException, IOException {
		for(int i = 0; i<NBINSTANCES; i++){
			assertFalse(fenetre[i].isVisible());
			assertTrue(fenetreChat[i].isVisible());
		}
	}

	/**
	 * Recup�re le contenu des messages des instances pour v�rifier que l'arriv�e d'un nouvel utilisateur a bien �t� notifi�
	 */
	@Test
	public void test002NotificationArriveUtilisateur(){
		for(int i = 0; i<NBINSTANCES-1; i++){
			String message = messagesTextPan[i].getText().toString();
			assertTrue(message.toLowerCase().contains("* Un nouvel utilisateur est dans le chat (place publique).".toLowerCase()));
		}
	}

	@Test
	public void test003NotificationDeconnexionUtilisateur(){
		saisieTextField[1].enterText("/q");
		for(int i = 0; i<NBINSTANCES-1; i++){
			String message = messagesTextPan[i].getText();
			assertTrue(message.toLowerCase().contains("* ? quitte le chat.".toLowerCase()));
		}
	}


	private static void sleep(int second){
		try {
			TimeUnit.SECONDS.sleep(second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
