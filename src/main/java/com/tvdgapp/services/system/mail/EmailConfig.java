package com.tvdgapp.services.system.mail;

import jakarta.annotation.Nullable;
import lombok.Data;
import net.minidev.json.JSONAware;
import net.minidev.json.JSONObject;


@Data
public class EmailConfig implements JSONAware {

	private @Nullable String host;
	private @Nullable String port;
	private @Nullable String protocol;
	private @Nullable String username;
	private @Nullable String password;
	private boolean smtpAuth = false;
	private boolean ssl = false;

	private @Nullable String emailTemplatesPath = null;


	@Override
	public String toJSONString() {
		JSONObject data = new JSONObject();
		data.put("host", this.getHost());
		data.put("port", this.getPort());
		data.put("protocol", this.getProtocol());
		data.put("username", this.getUsername());
		data.put("smtpAuth", this.isSmtpAuth());
		data.put("starttls", this.isSsl());
		data.put("password", this.getPassword());
		return data.toJSONString();	}


}
