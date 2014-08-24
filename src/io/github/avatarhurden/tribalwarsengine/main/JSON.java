package io.github.avatarhurden.tribalwarsengine.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Lê e escreve arquivos ou String em json.
 * <p/>
 * Tratar as exceptions com carinho!
 *
 * @author Wesley Nascimento.
 */
public class JSON {

    /**
     * Read a string and return a json object or a throws
     *
     * @param string String path of JSON
     * @return JSONObject
     * @throws IOException
     */
    public static JSONObject getJSON(String string) throws IOException {
        if (string.startsWith("http")) {
            return getJSON(new URL(string));
        } else {
            return getJSON(new File(string));
        }
    }

    /**
     * Read and return a JSON from file
     *
     * @param file File
     * @return JSONObject
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getJSON(File file) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int cp;
        while ((cp = reader.read()) != -1) {
            sb.append((char) cp);
        }
        return new JSONObject(sb.toString());
    }
    
    /**
     * Read and return a JSON from an InputStream
     *
     * @param stream InputStream
     * @return JSONObject
     * @throws IOException
     */
    public static JSONObject getJSON(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        int cp; 
        while ((cp = reader.read()) != -1) {
            sb.append((char) cp);
        }
        return new JSONObject(sb.toString());
    }


    /**
     * Read and return a JSON from URL
     *
     * @param url URL
     * @return JSONObject
     * @throws IOException
     */
    public static JSONObject getJSON(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        int cp;
        while ((cp = reader.read()) != -1) {
            sb.append((char) cp);
        }
        return new JSONObject(sb.toString());
    }

    /**
     * Save a JSON as file!
     *
     * @param json JSONObject
     * @param file File
     * @throws IOException
     */
    public static void createJSONFile(JSONObject json, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.append(json.toString(4));
        writer.flush();
        writer.close();
    }
}
