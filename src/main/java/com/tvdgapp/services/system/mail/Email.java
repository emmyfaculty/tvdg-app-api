package com.tvdgapp.services.system.mail;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Email implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6481794982612826257L;
	@Nullable
	private String from;
	@Nullable
	private String fromEmail;
	@Nullable
	private String to;
	@Nullable
	private String subject;
	@Nullable
	private String templateName;
	@Nullable
	private Map<String,String> templateTokens = new HashMap<String,String>();
	@Nullable
	private String body;
	@Nullable
	private FileAttachement attachement;

	@Data
	@AllArgsConstructor
	public static class FileAttachement {
		private String fileName;
		private InputStream inputStream;
	}
}
