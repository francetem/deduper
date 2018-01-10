package org.ehu.dedupe.derive.address;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AddressTest {
    @Test
    public void testMatcher() throws Exception {
        Address address = Address.matcher("calle Fontrodona, 13");
        assertEquals("Fontrodona", address.getStreet());
        assertTrue(address.getRange().contains(13));

        address = Address.matcher("Fontrodona, 13");
        assertEquals("Fontrodona", address.getStreet());
        assertTrue(address.getRange().contains(13));

        address = Address.matcher("Fontrodona, 13 1º 1ª");
        assertEquals("Fontrodona", address.getStreet());
        assertTrue(address.getRange().contains(13));
        assertEquals("1º 1ª", address.getSuffix());

        address = Address.matcher("c/ Mexico 24/26");
        assertEquals("Mexico", address.getStreet());
        assertTrue(address.getRange().contains(24));

        address = Address.matcher("c/ Mexico 24/26");
        assertEquals("Mexico", address.getStreet());
        assertTrue(address.getRange().contains(25));

        Assert.assertNotNull(Address.matcher("Different locations"));
    }

}