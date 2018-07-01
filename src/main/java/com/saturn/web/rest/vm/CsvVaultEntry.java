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
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
