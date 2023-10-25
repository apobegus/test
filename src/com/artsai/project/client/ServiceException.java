package com.artsai.project.client;

import com.google.gwt.user.client.rpc.SerializationException;

/**
 * @author Andrey Pobegus
 */
public class ServiceException extends SerializationException {

	private static final long serialVersionUID = 1L;
	private String comment = null;

	/**
	 * Default constructor.
	 */
	public ServiceException() {
		super();
	}

	/**
	 * The constructor.
	 * 
	 * @param comment the exception comment
	 * @param message the exception message
	 */
	public ServiceException(String comment, String message) {
		super(message);
		this.comment = comment;
	}

	/**
	 * Returns the comments text.
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment text.
	 * 
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
