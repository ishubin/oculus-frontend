package net.mindengine.oculus.frontend.domain.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedRun {
	private Long id;
	private String name;
	private Date date;
	private Long userId;

	public String generateFileUrl() {
		return generateDirUrl() + File.separator + id + ".binaryobject";
	}

	public String generateDirUrl() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(date);
		String convertedDate = strDate;
		convertedDate = convertedDate.replace("-", File.separator);
		return convertedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
