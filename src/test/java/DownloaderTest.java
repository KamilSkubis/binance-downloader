

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DownloaderTest {

	@Test
	public void prepareListOfTickers() {
		Binance b = new Binance();
		assertTrue(b.getTickers().size() > 0);
	}
	
	

}
