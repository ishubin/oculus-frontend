package net.mindengine.oculus.frontend.domain.document;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload extends Document {
	private MultipartFile file;

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}

}
