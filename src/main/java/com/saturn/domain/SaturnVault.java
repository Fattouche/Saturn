package com.saturn.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;

/**
 * A SaturnVault.
 */
@Entity
@Table(name = "saturnpass")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SaturnVault extends AbstractDatedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Key k;

	public SaturnVault() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256);
			k = keyGen.generateKey();
		} catch(NoSuchAlgorithmException ex) {
			Logger.getLogger(SaturnVault.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 3)
	@Column(name = "site", nullable = false)
	private String site;

	@NotNull
	@Column(name = "login", nullable = false)
	private String login;

	@NotNull
	@Column(name = "password", nullable = false)
	private String password;

	@ManyToOne
	@NotNull
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public SaturnVault site(String site) {
		this.site = site;
		return this;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLogin() {
		return login;
	}

	public SaturnVault login(String login) {
		this.login = login;
		return this;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		if (password != null) {
			try {
				System.out.println("***************************HALLO!*******************************");
				Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
				c.init(Cipher.DECRYPT_MODE, k);
				return new String(c.doFinal(DatatypeConverter.parseBase64Binary(password)));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException ex ) {
				Logger.getLogger(SaturnVault.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return null;
	}

	public SaturnVault password(String password) {
		setPassword(password);
		return this;
	}

	public void setPassword(String password) {
		try {
				System.out.println("***************************HALLO!*******************************");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
			c.init(Cipher.ENCRYPT_MODE, k);
			this.password = DatatypeConverter.printBase64Binary(c.doFinal(password.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException ex) {
			Logger.getLogger(SaturnVault.class.getName()).log(Level.SEVERE, null, ex);
			this.password = null;
		}
	}

	public User getUser() {
		return user;
	}

	public SaturnVault user(User user) {
		this.user = user;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SaturnVault saturnPass = (SaturnVault) o;
		if (saturnPass.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, saturnPass.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "SaturnVault{"
			+ "id=" + id
			+ ", site='" + site + "'"
			+ ", login='" + login + "'"
			+ ", password='" + password + "'"
			+ ", createdDate='" + createdDate + "'"
			+ ", lastModifiedDate='" + lastModifiedDate + "'"
			+ '}';
	}
}
