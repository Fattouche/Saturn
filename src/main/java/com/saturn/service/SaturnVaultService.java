package com.saturn.service;

import com.google.common.base.Optional;
import com.saturn.domain.SaturnVault;
import com.saturn.repository.SaturnVaultRepository;
import com.saturn.security.SecurityUtils;
import com.saturn.service.dto.SaturnVaultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.constraints.Null;

/**
 * Service Implementation for managing SaturnVault.
 */
@Service
@Transactional
public class SaturnVaultService {

	private final Logger log = LoggerFactory.getLogger(SaturnVaultService.class);

	@Inject
	private SaturnVaultRepository saturnPassRepository;

	@Inject
	private UserService userService;

	/**
	 * Save a saturnPass.
	 *
	 * @param dto the entity to save
	 * @return the persisted entity
	 */
	public SaturnVaultDTO save(SaturnVaultDTO dto) {
		log.debug("Request to save SaturnVault : {}", dto);

		SaturnVault saturnPass;

		if (dto.getId() != null) {
			saturnPass = saturnPassRepository.findOne(dto.getId());

			if (saturnPass == null) {
				return null;
			}
		} else {
			saturnPass = new SaturnVault();
			saturnPass.setUser(userService.getCurrentUser());
		}
		saturnPass.site(dto.getSite()).login(dto.getLogin()).password(dto.getPassword());

		return new SaturnVaultDTO(saturnPassRepository.save(saturnPass));
	}

	/**
	 * Get all the saturnPasses of current user.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<SaturnVaultDTO> findAllOfCurrentUser(Pageable pageable) {
		log.debug("Request to get all SaturnVaults of current user");
		return saturnPassRepository.findByUserIsCurrentUser(pageable).map(SaturnVaultDTO::new);
	}

	/**
	 * Get one saturnPass by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public SaturnVaultDTO findOne(Long id) {
		log.debug("Request to get SaturnVault : {}", id);
		SaturnVault temp = saturnPassRepository.findOne(id);
		SaturnVaultDTO tempDTO = new SaturnVaultDTO(temp);
		if (temp != null) {
			if (!temp.getUser().getEmail().equals(SecurityUtils.getCurrentUser())) {
				log.debug("Unauthorized Not completing transaction");
				SaturnVaultDTO newtemp = new SaturnVaultDTO();
				temp.setId((long) -401);
				return newtemp;
			}
			return tempDTO;
		}
		return tempDTO;
	}

	/**
	 * Delete the saturnPass by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		if (!saturnPassRepository.exists(id)) {
			return;
		}

		log.debug("Request to delete SaturnVault : {}", id);
		log.debug("Current user: {}", saturnPassRepository.findOne(id).getUser().getEmail());
		log.debug("What security says the current user is: {}", SecurityUtils.getCurrentUser());

		if (saturnPassRepository.findOne(id).getUser().getEmail().equals(SecurityUtils.getCurrentUser())) {
			log.debug("Authorized - deleting saturn vault : {}", id);
			saturnPassRepository.delete(id);

		}
	}
}
