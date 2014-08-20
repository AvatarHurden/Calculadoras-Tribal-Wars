package io.github.avatarhurden.tribalwarsengine.objects;

import org.json.JSONObject;

/**
 * Objetos que podem ser editados diretamente pelo usu�rio durante a execu��o do programa.
 * 
 * @author Arthur
 *
 */
public interface EditableObject {

	/**
	 * Returns the JSONObject that contains the object's properties
	 * @return JSONObject
	 */
	public JSONObject getJson();
	
}
