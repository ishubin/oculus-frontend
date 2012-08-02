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
package net.mindengine.oculus.frontend.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;

public class Auth {

    private static final SecretKey secrectAuthKey = createKey();
    
    private static final int COOKIE_MAX_AGE = 8640000;

    private static SecretKey createKey() {
        try {
            return KeyGenerator.getInstance("DES").generateKey();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    
    public static String encodeUser(User user) throws Exception {
        if ( user == null ) {
            throw new IllegalArgumentException("User should not be null");
        }
        if ( secrectAuthKey == null ) {
            throw new IllegalArgumentException("Couldn't generate secret key");
        }
        
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secrectAuthKey);
        
        SealedObject sealedUser = new SealedObject(user, cipher);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(sealedUser);
        oos.close();
        return new String(Base64.encodeBase64(baos.toByteArray()));
    }
    
    public static void setUserCookieToResponse(HttpServletResponse response, User user) throws Exception {
        response.addCookie(createCookie("_u", encodeUser(user), COOKIE_MAX_AGE, "/"));
    }
    
    public static void removeUserCookie(HttpServletResponse response) {
        response.addCookie(createCookie("_u", "", 0, "/"));
    }
    
    public static User decodeUser(String encodedString) {
        try {
            ObjectInputStream ois = new ObjectInputStream( 
                    new ByteArrayInputStream(Base64.decodeBase64(encodedString.getBytes())));
            SealedObject sealedObject  =  (SealedObject) ois.readObject();
            ois.close();
            
            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, secrectAuthKey);

            User user = (User)sealedObject.getObject(dcipher);
            return user;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private static Cookie createCookie(String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        return cookie;
    }

    public static User getAuthorizedUser(HttpServletRequest request) throws NotAuthorizedException {
        User user = getUserFromRequest(request);
        if ( user == null) {
            throw new NotAuthorizedException();
        }
        return user;
    }

    public static User getUserFromRequest(HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        
        if ( cookies != null ) {
            for ( Cookie cookie : cookies) {
                if ( cookie.getName().equals("_u") ) {
                    User user = decodeUser(cookie.getValue());
                    return user;
                }
            }
        }
        return null;
    }
}
