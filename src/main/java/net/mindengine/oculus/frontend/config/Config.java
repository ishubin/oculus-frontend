package net.mindengine.oculus.frontend.config;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is an entry point to Oculus configuration file. It could also be
 * used to obtain the remote instance of the {@link TRMServer}
 * 
 * @author Ivan Shubin
 * 
 */
public class Config {
	private Log logger = LogFactory.getLog(getClass());

	/**
	 * Used as a Java bean property for specifiying the oculus path from
	 * <b>applicationContext.xml</b>
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
	 * Searches for the remote instance of the {@link TRMServer}.
	 * 
	 * @return Remote instance of the {@link TRMServer}
	 * @throws Exception
	 */
	public ClientServerRemoteInterface getTRMServer() throws Exception {
		String serverAddress = "rmi://" + getTrmServerHost() + "/" + getTrmServerName();
		logger.info("Locating registry " + getTrmServerHost() + " port " + getTrmServerPort());
		Registry registry = LocateRegistry.getRegistry(getTrmServerHost(), Integer.parseInt(getTrmServerPort()));
		logger.info("Looking for " + serverAddress);

		ClientServerRemoteInterface server = (ClientServerRemoteInterface) registry.lookup(serverAddress);
		if (server != null) {
			logger.info("Server was found");
		}
		else {
			logger.error("Server wasn't found");
		}
		return server;
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
