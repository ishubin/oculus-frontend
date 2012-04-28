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
import java.util.Properties;

import net.mindengine.jeremy.registry.Lookup;
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
	
	/**
	 * Used as a Java bean property for specifiying the oculus path from
	 * applicationContext.xml
	 */
	private File file;
	private String dataFolder;
	private String documentsFolder;
	private String trmFolder;
	private String trmServerHost;
	private String trmServerPort;
	private String trmServerName;
	private String mailSmtpHost;
	private String mailSender;
	private String oculusServerUrl;

	public Config() {

	}

	/**
	 * Searches for the remote instance of the {@link Server}.
	 * 
	 * @return Remote instance of the {@link Server}
	 * @throws Exception
	 */
	public ClientServerRemoteInterface getGridServer() throws Exception {
	    Lookup lookup = GridUtils.createDefaultLookup();
        lookup.setUrl("http://localhost:8081");
        return lookup.getRemoteObject("grid", ClientServerRemoteInterface.class);
	}

	public String getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}

	public File getFile() {
		return file;
	}

	/**
	 * This method is mainly used to specify the Oculus configuration file from
	 * <b>applicationContext.xml</b>. Once the method was invoked the
	 * configuration file will be pick up and parsed for all existent
	 * properties.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void setFile(File file) throws Exception {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(file);
		props.load(fis);
		fis.close();

		setDataFolder(props.getProperty("data.folder"));
		setDocumentsFolder(props.getProperty("documents.folder"));
		setTrmFolder(props.getProperty("trm.folder"));

		setTrmServerHost(props.getProperty("trm.server.host"));
		setTrmServerPort(props.getProperty("trm.server.port"));
		setTrmServerName(props.getProperty("trm.server.name"));

		setMailSmtpHost(props.getProperty("mail.smtp.host"));
		setMailSender(props.getProperty("mail.sender"));

		setOculusServerUrl(props.getProperty("oculus.server.url"));
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

	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}

	public String getMailSender() {
		return mailSender;
	}

	public void setOculusServerUrl(String oculusServerUrl) {
		this.oculusServerUrl = oculusServerUrl;
	}

	public String getOculusServerUrl() {
		return oculusServerUrl;
	}

}
