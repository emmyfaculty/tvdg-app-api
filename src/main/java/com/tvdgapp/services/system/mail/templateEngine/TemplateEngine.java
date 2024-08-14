package com.tvdgapp.services.system.mail.templateEngine;


import com.tvdgapp.exceptions.TemplateEngineException;

import java.util.Map;

public interface TemplateEngine {

    public String processTemplateIntoString(String templateName, Map<String, Object> templateTokens) throws TemplateEngineException;

}
