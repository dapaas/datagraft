package eu.dapaas.dao;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * RESULT:
 {

 }
 */
public class User {
  private String username;
  private String email;
  private String name;
  private String role;
  private String phone;
  private String address;
  private String apiKey;
  private String apiSecret;
  private List<Cookie> cookies;

  public User() {

  }

  public void setUser(JSONObject user) {
    try {
      this.username = user.get("username").toString();
      this.email = user.get("email").toString();
      this.name = user.get("name").toString();
      this.role = user.getString("role");
      this.phone = user.getString("phone_num");
      this.address = user.getString("addr_1");
    } catch (JSONException je) {
      je.printStackTrace();
    }
  }

  public void setUserApi(JSONObject api) {
    try {
      this.apiKey = api.get("api_key").toString();
      this.apiSecret = api.get("secret").toString();
    } catch (JSONException je) {
      je.printStackTrace();
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiSecret() {
    return apiSecret;
  }

  public void setApiSecret(String apiSecret) {
    this.apiSecret = apiSecret;
  }

  public List<Cookie> getCookies() {
    return cookies;
  }

  public void setCookies(List<Cookie> cookies) {
    this.cookies = cookies;
  }

}
