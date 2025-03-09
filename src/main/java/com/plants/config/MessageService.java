package com.plants.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, String lang) {
        Locale locale = new Locale(lang);
        try {
        	System.out.println(" code  -- "  +code + "--  lang  -- " + lang + "---dd--- " + locale);
            return messageSource.getMessage(code, null, locale);
        } catch (Exception e) {
            return messageSource.getMessage(code, null, Locale.ENGLISH); // Default to English
        }
    }
}
