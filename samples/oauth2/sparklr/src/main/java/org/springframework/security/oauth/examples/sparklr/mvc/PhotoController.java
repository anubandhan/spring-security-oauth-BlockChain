package org.springframework.security.oauth.examples.sparklr.mvc;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth.examples.sparklr.PhotoInfo;
import org.springframework.security.oauth.examples.sparklr.PhotoService;
import org.springframework.security.oauth.examples.sparklr.impl.PhotoServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Ryan Heaton
 * @author Dave Syer
 */
@Controller
public class PhotoController {

	private PhotoService photoService = PhotoServiceImpl.getInstance();
	static final Logger log = Logger.getLogger(PhotoController.class);

	@RequestMapping("/photos/{photoId}")
	public ResponseEntity<byte[]> getPhoto(@PathVariable("photoId") String id) throws IOException {
		InputStream photo = getPhotoService().loadPhoto(id);

		log.info("photos Received for : "+id);
		log.info("photos are : "+ photo);
		if (photo == null) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
		else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = photo.read(buffer);
			while (len >= 0) {
				out.write(buffer, 0, len);
				len = photo.read(buffer);
			}
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "image/jpeg");
			return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/photos", params = "format=json")
	public ResponseEntity<String> getJsonPhotos(Principal principal) {
		log.info("photos Called ");
		Collection<PhotoInfo> photos = getPhotoService().getPhotosForCurrentUser(principal.getName());
		StringBuilder out = new StringBuilder();
		out.append("{ \"photos\" : [ ");
		Iterator<PhotoInfo> photosIt = photos.iterator();
		while (photosIt.hasNext()) {
			PhotoInfo photo = photosIt.next();
			out.append(String.format("{ \"id\" : \"%s\" , \"name\" : \"%s\" }", photo.getId(), photo.getName()));
			if (photosIt.hasNext()) {
				out.append(" , ");
			}
		}
		out.append("] }");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/javascript");
		return new ResponseEntity<String>(out.toString(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/photos", params = "format=xml")
	public ResponseEntity<String> getXmlPhotos(Principal principal) {
		log.info("photos Called ");
		log.info("photos Called for User:"+ principal.getName());
		Collection<PhotoInfo> photos = photoService.getPhotosForCurrentUser(principal.getName());
		StringBuilder out = new StringBuilder();
		out.append("<photos>");
		for (PhotoInfo photo : photos) {
			out.append(String.format("<photo id=\"%s\" name=\"%s\"/>", photo.getId(), photo.getName()));
		}
		out.append("</photos>");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/xml");

		log.info("photos response: "+out.toString());
		return new ResponseEntity<String>(out.toString(), headers, HttpStatus.OK);
	}

	@RequestMapping("/photos/trusted/message")
	@PreAuthorize("#oauth2.clientHasRole('ROLE_CLIENT')")
	@ResponseBody
	public String getTrustedClientMessage() {
		return "Hello, Trusted Client";
	}

	@RequestMapping("/photos/user/message")
	@ResponseBody
	public String getTrustedUserMessage(Principal principal) {
		return "Hello, Trusted User" + (principal != null ? " " + principal.getName() : "");
	}

	public PhotoService getPhotoService() {
		return photoService;
	}

	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}

}
