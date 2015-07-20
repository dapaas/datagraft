package eu.dapaas.service;

import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;

import eu.dapaas.dao.Poligon;
import eu.dapaas.db.LocalDBProvider;

@WebService
public class AdminService {
  
  public Object createPoligon(@WebParam(name="filename") String filename, @WebParam(name="title") String title){
    Poligon poligon = new Poligon();
    poligon.setFilename(filename);
    poligon.setTitle(title);
    LocalDBProvider.createPoligon(poligon);
    return true;
  }

}
