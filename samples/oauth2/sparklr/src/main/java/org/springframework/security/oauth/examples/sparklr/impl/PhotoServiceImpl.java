package org.springframework.security.oauth.examples.sparklr.impl;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth.examples.sparklr.PhotoInfo;
import org.springframework.security.oauth.examples.sparklr.PhotoService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Basic implementation for the photo service.
 * 
 * @author Ryan Heaton
 */
public class PhotoServiceImpl implements PhotoService {

	static final Logger log = Logger.getLogger(PhotoServiceImpl.class);

	private List<PhotoInfo> photos;

	private static PhotoServiceImpl photoService;
	private PhotoServiceImpl(){};
	public static PhotoServiceImpl getInstance(){
		if(photoService==null){
			photoService = new PhotoServiceImpl();
		}
		return photoService;
	}


	public Collection<PhotoInfo> getPhotosForCurrentUser(String username) {

		ArrayList<PhotoInfo> infos = new ArrayList<PhotoInfo>();
		for (PhotoInfo info : getPhotos()) {
			if (username.equals(info.getUserId())) {
				infos.add(info);
			}
		}
		return infos;

	}

	public InputStream loadPhoto(String id) {
		log.info("PhotoServiceImpl :: id: "+id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("PhotoServiceImpl :: authentication: "+authentication);
		//if (authentication.getPrincipal() instanceof UserDetails) {
			String username = (String) authentication.getPrincipal();
			log.info("PhotoServiceImpl :: details: "+username);
			//String username = details;
			//log.info("PhotoServiceImpl :: username: "+username);
			for (PhotoInfo photoInfo : getPhotos()) {
				log.info("PhotoServiceImpl :: inside loop: photoInfo: "+photoInfo);
				if (id.equals(photoInfo.getId()) && username.equals(photoInfo.getUserId())) {
					log.info("PhotoServiceImpl ::inside IF id: "+id);
					URL resourceURL = getClass().getResource(photoInfo.getResourceURL());
					log.info("PhotoServiceImpl ::inside IF resourceURL: "+resourceURL);
					if (resourceURL != null) {
						try {
							return resourceURL.openStream();
						} catch (IOException e) {
							// fall through...
						}
					}
				}
			}
		//}
		return null;
	}

	public List<PhotoInfo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoInfo> photos) {
		this.photos = photos;
	}
}
