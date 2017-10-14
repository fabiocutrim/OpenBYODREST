package br.com.openbyod.resource;

import javax.json.Json;
import javax.json.JsonObject;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource {

    private String resource;
    private String token;

    public Resource() {

    }

    public Resource(String resource) {
        this.resource = resource;
    }

    public Resource(String resource, String token) {
        super();
        this.resource = resource;
        this.token = token;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        String resourceJson = this.getResource();
        String tokenJson = this.getToken();

        if (resourceJson == null) {
            resourceJson = "";
        }
        if (tokenJson == null) {
            tokenJson = "";
        }

        try {
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("resource", resourceJson)
                    .add("token", tokenJson)
                    .build();
            return jsonObject.toString();

        } catch (Exception e) {
            return "Exceção Resource.toString(): " + e.getLocalizedMessage();
        }
    }
}
