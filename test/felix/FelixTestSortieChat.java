package felix;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTextPaneOperator;

import camix.Camix;
import camix.communication.ConnexionClient;
import camix.communication.ConnexionServeur;
import camix.service.CanalChat;
import camix.service.ClientChat;
import camix.service.ServiceChat;
import felix.communication.Connexion;
import felix.controleur.ControleurFelix;
import felix.vue.VueConnexion;

public class FelixTestSortieChat {

	public static ClientChat clientChat;
	public static ConnexionClient connexionClient;
	public static CanalChat canalChat;
	public static ServiceChat serviceChat;

	public FelixTestSortieChat(){

	}

	@BeforeClass
	public static void setUp() throws UnknownHostException, IOException{
		connexionClient = EasyMock.partialMockBuilder(ConnexionClient.class)
				.withConstructor(new Socket("127.0.0.1",12345))
				.addMockedMethod("lire")
				.createMock();

		canalChat = new CanalChat("canal");
		serviceChat = new ServiceChat("test");

	}

	@Test
	public void testEnleverCanal(){
		clientChat = EasyMock.createMock(ClientChat.class);
		EasyMock.expect(clientChat.donneId()).andReturn("test").times(4);
		EasyMock.replay(clientChat);

		CanalChat canalChat = new CanalChat("test");
		canalChat.ajouteClient(clientChat);
		assertTrue(1 == canalChat.donneNombreClients());
		canalChat.enleveClient(clientChat);
		assertTrue(0 == canalChat.donneNombreClients());
	}

	/**
	 * Test la suppression d'un client après l'envoi de la commande /q
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	@Test
	public void testQuitterChat() throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		EasyMock.expect(connexionClient.lire())
			.andReturn("/q")
			.andReturn(null);
		EasyMock.replay(connexionClient);

		/**
		 * Création de deux clients qui seront ajoutés au canal
		 */
		ClientChat clientChat = new ClientChat(serviceChat, new Socket("127.0.0.1",12345), "client", canalChat);
		ClientChat clientChat2 = new ClientChat(serviceChat, new Socket("127.0.0.1",12345), "client2", canalChat);

		canalChat.ajouteClient(clientChat);
		canalChat.ajouteClient(clientChat2);

		assertTrue(canalChat.donneNombreClients() == 2);

		/**
		 * Récupération de la propriété privé "connexion" avec l'objet mocké connexionClient
		 */
		Field field = clientChat.getClass().getDeclaredField("connexion");
		field.setAccessible(true);
		field.set(clientChat, connexionClient);

		/**
		 * Simule l'appel à la méthode run qui va executer la commande /q
		 */
		clientChat.run();

		/**
		 * On vérifie qu'après la commande /q, l'utilisateur a bien été retiré du canal
		 */
		assertTrue(canalChat.donneNombreClients() == 1);
	}

	@Test
	public void test(){

	}
}
