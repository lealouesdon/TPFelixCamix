package camix.service;

import java.io.IOException;
import java.net.Socket;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(EasyMockRunner.class)
public class CanalChatTestEx1EasyMock {
	
	@Mock
	private ClientChat clientChat;	
	
	
	/**
	 * Crée, avec EasyMock, un simulacre de stock nécessaire aux tests.
	 *
	 * <p>Code exécuté avant les tests.</p>
	 *
	 * @throws Exception toute exception.
	 *
	 * @see org.easymock.EasyMock
	 *
	 */
	@Before
	public void setUp() throws Exception 
	{
		/* utilisation alternative de @Mock */
		//this.clientChat = EasyMock.createMock(ClientChat.class);
	}

	/**
	 * Non implanté.
	 *
	 * <p>Code exécuté après les tests.</p>
	 *
	 * @throws Exception toute exception.
	 *
	 */
	@After
	public void tearDown() throws Exception 
	{
		/* rien faire */
	}
	
	@Test
	public void testAjouterClient_nonpresent_v1() throws IOException {
		
		final CanalChat canal = new CanalChat("MyCanal");
		EasyMock.expect(
			this.clientChat.donneId()
		).andReturn(
			"12"
		).times(3);
		
		EasyMock.replay(this.clientChat);
		
		canal.ajouteClient(this.clientChat);
	
		
		Assert.assertEquals("Nombre de clients : ", (Integer)1, canal.donneNombreClients());
		Assert.assertEquals("Client bien présent : ", true, canal.estPresent(this.clientChat));
			
	}
	
	@Test
	public void testAjouterClient_present_v1() throws IOException {
		
		final CanalChat canal = new CanalChat("MyCanal");		
		EasyMock.expect(
			this.clientChat.donneId()
		).andReturn(
			"12"
		).times(4);
		
		EasyMock.replay(this.clientChat);
		
		canal.ajouteClient(this.clientChat);
		
		canal.ajouteClient(this.clientChat);
				
		Assert.assertEquals("Nombre de clients : ", (Integer)1, canal.donneNombreClients());
			
	}
	
	@Test
	public void testAjouterClient_nonpresent_v2() throws IOException {
		
		final CanalChat canal = new CanalChat("MyCanal");
		EasyMock.expect(
			this.clientChat.donneId()
		).andReturn(
			"12"
		).times(3);
		
		EasyMock.replay(this.clientChat);
		
		canal.ajouteClient(this.clientChat);
	
		
		Assert.assertEquals("Nombre de clients : ", (Integer)1, canal.donneNombreClients());
		Assert.assertEquals("Client bien présent : ", true, canal.estPresent(this.clientChat));
			
	}
	
	@Test
	public void testAjouterClient_present_v2() throws IOException {
		
		final CanalChat canal = new CanalChat("MyCanal");		
		EasyMock.expect(
			this.clientChat.donneId()
		).andReturn(
			"12"
		).times(4);
		
		EasyMock.replay(this.clientChat);
		
		canal.ajouteClient(this.clientChat);
		
		canal.ajouteClient(this.clientChat);
				
		Assert.assertEquals("Nombre de clients : ", (Integer)1, canal.donneNombreClients());
			
	}
}
