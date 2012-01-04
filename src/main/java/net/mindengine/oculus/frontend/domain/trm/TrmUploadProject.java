package net.mindengine.oculus.frontend.domain.trm;

public class TrmUploadProject {
	private byte[] zippedFile;
	private String version;

	public byte[] getZippedFile() {
		return zippedFile;
	}

	public void setZippedFile(byte[] zippedFile) {
		this.zippedFile = zippedFile;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
