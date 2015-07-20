package eu.dapaas.dao;

import com.sirmamobile.commlib.annotations.MappedObject;
import com.sirmamobile.commlib.annotations.Oneway;
import com.sirmamobile.commlib.annotations.Result;

@Oneway
@Result(key = "error")
@MappedObject(recursive = true, forced = true)
public class ErrorService {
  
  private String code;
  private String message;
  
  public ErrorService(String message) {
      this.setCode(null);
      this.setMessage(message);
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
}
