package com.twosri.dev.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.twosri.dev.bean.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Converter {

	public String getUserXml(List<User> userList) {
		StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("\n").append("<rows>").append("\n");
		for (User user : userList) {
			xml.append("\t").append("<row id=\"" + user.getId() + "\">").append("\n");
			xml.append("\t\t").append("<cell>" + user.getUserId() + "</cell>").append("\n");
			xml.append("\t\t").append("<cell>" + user.getPasscode() + "</cell>").append("\n");
			xml.append("\t\t").append("<cell>" + (user.isActive() ? "Y" : "N") + "</cell>").append("\n");
			xml.append("\t").append("</row>").append("\n");
		}
		xml.append("</rows>").append("\n");
		log.info(xml.toString());
		return xml.toString();

	}

}
