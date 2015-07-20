package eu.dapaas.bean;

import eu.dapaas.dao.Poligon;
import eu.dapaas.db.LocalDBProvider;

public class PoligonBean {
  
 
  
  public void deletePoligon(String poligonId){
    LocalDBProvider.deletePoligon(poligonId);
  }
  
  public Poligon getPoligon(String id){
    Poligon poligon = LocalDBProvider.getPoligon(id);
    return poligon;
  }
  
 
}
