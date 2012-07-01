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
package net.mindengine.oculus.frontend.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import net.mindengine.jeremy.registry.Lookup;
import net.mindengine.oculus.experior.utils.PropertyUtils;
import net.mindengine.oculus.grid.GridUtils;
import net.mindengine.oculus.grid.server.Server;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

/**
 * This class is an entry point to Oculus configuration file. It could also be
 * used to obtain the remote instance of the {@link TRMServer}
 * 
 * @author Ivan Shubin
 * 
 */
public class Config {
	
    
    private String apiSuperToken;
	private String dataFolder;
	private String documentsFolder;
	private String trmFolder;
	private String trmServerHost;
	private String trmServerPort;
	private String trmServerName;
	private String mailSmtpHost;
	private String mailSenderName;
	private Integer mailSmtpPort;
	private String oculusServerUrl;
	private int oculusServerPort;

	private String gridServerName;
	private int gridServerPort;
	private String gridServerStorage;
	private String gridServerHost;
	private Boolean gridEmbedded = true;
	private static Config _config = null;
	
	private String dbHost;
	private Integer dbPort;
	private String dbUsername;
	private String dbPassword;
	private String dbScheme;
	
	private Config() {
	    Properties properties = loadPropertiesFile(new File("oculus.properties"));
	    PropertyUtils.overridePropertiesWithSystemProperties(properties);
	    readProperties(properties);
	}
	
	
    private void readProperties(Properties properties) {
        setApiSuperToken(properties.getProperty("api.token"));
        
	    setDataFolder(properties.getProperty("data.folder"));
        setDocumentsFolder(properties.getProperty("documents.folder"));
        setTrmFolder(properties.getProperty("trm.folder"));

        setTrmServerHost(properties.getProperty("trm.server.host"));
        setTrmServerPort(properties.getProperty("trm.server.port"));
        setTrmServerName(properties.getProperty("trm.server.name"));

        setMailSmtpHost(properties.getProperty("mail.smtp.host"));
        setMailSmtpPort(Integer.parseInt(properties.getProperty("mail.smtp.port", "25")));
        setMailSenderName(properties.getProperty("mail.sender.name"));

        setOculusServerUrl(properties.getProperty("oculus.offline.report.server.url"));
        setOculusServerPort(Integer.parseInt(properties.getProperty("oculus.server.port", "8080")));
        
        setGridServerName(properties.getProperty("grid.server.name","grid"));
        setGridServerHost(properties.getProperty("grid.server.host","localhost"));
        setGridServerPort(Integer.parseInt(properties.getProperty("grid.server.port", "8081")));
        setGridServerStorage(properties.getProperty("grid.server.storage"));
        setGridEmbedded(Boolean.parseBoolean(properties.getProperty("grid.embedded", "true")));
        
        setDbHost(properties.getProperty("db.host"));
        setDbPort(Integer.parseInt(properties.getProperty("db.port", "3306")));
        setDbUsername(properties.getProperty("db.username"));
        setDbPassword(properties.getProperty("db.password"));
        setDbScheme(properties.getProperty("db.scheme"));
    }

    public synchronized static Config getInstance() {
	    if ( _config == null) {
	        _config = new Config();
	    }   
	    return _config;
	}

	/**
	 * Searches for the remote instance of the {@link Server}.
	 * 
	 * @return Remote instance of the {@link Server}
	 * @throws Exception
	 */
	public ClientServerRemoteInterface lookupGridServer() throws Exception {
	    Lookup lookup = GridUtils.createDefaultLookup();
        lookup.setUrl("http://" + getGridServerHost() + ":" + getGridServerPort());
        return lookup.getRemoteObject(getGridServerName(), ClientServerRemoteInterface.class);
	}

	public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }
	
	public String getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}

	private Properties loadPropertiesFile(File file) {
		Properties props = new Properties();
		
		try {
    		FileInputStream fis = new FileInputStream(file);
    		props.load(fis);
    		fis.close();
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return props;
	}

	public void setDocumentsFolder(String documentsFolder) {
		this.documentsFolder = documentsFolder;
	}

	public String getDocumentsFolder() {
		return documentsFolder;
	}

	public void setTrmFolder(String trmFolder) {
		this.trmFolder = trmFolder;
	}

	public String getTrmFolder() {
		return trmFolder;
	}

	public String getTrmServerHost() {
		return trmServerHost;
	}

	public void setTrmServerHost(String trmServerHost) {
		this.trmServerHost = trmServerHost;
	}

	public String getTrmServerPort() {
		return trmServerPort;
	}

	public void setTrmServerPort(String trmServerPort) {
		this.trmServerPort = trmServerPort;
	}

	public String getTrmServerName() {
		return trmServerName;
	}

	public void setTrmServerName(String trmServerName) {
		this.trmServerName = trmServerName;
	}

	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}

	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public void setOculusServerUrl(String oculusServerUrl) {
		this.oculusServerUrl = oculusServerUrl;
	}

	public String getOculusServerUrl() {
		return oculusServerUrl;
	}

    public String getGridServerName() {
        return gridServerName;
    }

    public void setGridServerName(String gridServerName) {
        this.gridServerName = gridServerName;
    }

    public int getGridServerPort() {
        return gridServerPort;
    }

    public void setGridServerPort(int gridServerPort) {
        this.gridServerPort = gridServerPort;
    }

    public String getGridServerStorage() {
        return gridServerStorage;
    }

    public void setGridServerStorage(String gridServerStorage) {
        this.gridServerStorage = gridServerStorage;
    }

    public int getOculusServerPort() {
        return oculusServerPort;
    }

    public void setOculusServerPort(int oculusServerPort) {
        this.oculusServerPort = oculusServerPort;
    }

    public String getGridServerHost() {
        return gridServerHost;
    }

    public void setGridServerHost(String gridServerHost) {
        this.gridServerHost = gridServerHost;
    }

    public Boolean getGridEmbedded() {
        return gridEmbedded;
    }

    public void setGridEmbedded(Boolean gridEmbedded) {
        this.gridEmbedded = gridEmbedded;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbScheme() {
        return dbScheme;
    }

    public void setDbScheme(String dbScheme) {
        this.dbScheme = dbScheme;
    }

    public Integer getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(Integer mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    public String getMailSenderName() {
        return mailSenderName;
    }

    public void setMailSenderName(String mailSenderName) {
        this.mailSenderName = mailSenderName;
    }


    public String getApiSuperToken() {
        return apiSuperToken;
    }


    public void setApiSuperToken(String apiSuperToken) {
        this.apiSuperToken = apiSuperToken;
    }

}
