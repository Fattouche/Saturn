package com.saturn.web.rest.vm;

import com.opencsv.bean.CsvBindByName;

/**
 * View Model object for storing the user's key and password.
 */
public class CsvVaultEntry {

    @CsvBindByName
    private String url;
    
    @CsvBindByName
    private String name;

    @CsvBindByName
	private String username;

    @CsvBindByName
    private String password;

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

	/**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
