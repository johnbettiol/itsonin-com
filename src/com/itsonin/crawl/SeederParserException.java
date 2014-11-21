package com.itsonin.crawl;

public class SeederParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url, content, step;
	
	public SeederParserException(String url, String content, String step, String message, Throwable cause) {
		super(message, cause);
		this.url = url;
		this.content = content;
		this.step = step;
	}

}
