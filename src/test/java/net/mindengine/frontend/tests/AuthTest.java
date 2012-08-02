/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.frontend.tests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.web.Auth;

import org.junit.Test;

public class AuthTest {

    
    @Test
    public void canEncodeDecodeUser() throws Exception {
        User user = createTestUser();
        String encoded = Auth.encodeUser(user);
        
        User decodedUser = Auth.decodeUser(encoded);
        
        Assert.assertNotNull(decodedUser);
        Assert.assertEquals(user.getEmail(), decodedUser.getEmail());
        Assert.assertEquals(user.getLogin(), decodedUser.getLogin());
        Assert.assertEquals(user.getName(), decodedUser.getName());
        Assert.assertEquals(user.getPassword(), decodedUser.getPassword());
        Assert.assertEquals(user.getPermissions(), decodedUser.getPermissions());
        Assert.assertEquals(2, decodedUser.getHasPermissions().size());
        Assert.assertEquals(Boolean.TRUE, decodedUser.getHasPermissions().get("key1"));
        Assert.assertEquals(Boolean.FALSE, decodedUser.getHasPermissions().get("key2"));
        
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail("email@email.com");
        Map<String, Boolean> hasPermissions = new HashMap<String, Boolean>();
        hasPermissions.put("key1", true);
        hasPermissions.put("key2", false);
        user.setHasPermissions(hasPermissions);
        user.setId(123L);
        user.setLogin("login");
        user.setName("Some name");
        user.setPassword("123456");
        user.setPermissions("ffffff");
        
        return user;
    }
}
