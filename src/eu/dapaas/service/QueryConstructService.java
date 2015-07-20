package eu.dapaas.service;

import java.util.ArrayList;

import com.sirmamobile.commlib.annotations.WebMethod;
import com.sirmamobile.commlib.annotations.WebParam;
import com.sirmamobile.commlib.annotations.WebService;

@WebService
public class QueryConstructService {
/*PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX fn: <http://www.w3.org/2005/xpath-functions#>
PREFIX csv: <http://rdf.dapaas.eu/cvsimport/>

SELECT ?col1 ?col2
WHERE {
?a csv:col1 ?col1.
?a csv:col2 ?col2.
}*/
	@WebMethod
	public String constructQuery(@WebParam(name = "values") ArrayList<String> values){
	  String sqlPrefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
	                     "PREFIX fn: <http://www.w3.org/2005/xpath-functions#> \n"+
	                     "PREFIX csv: <http://rdf.dapaas.eu/cvsimport/>\n\n";
  
		String sqlSelect = "SELECT ";
		String selectProp = "?%1$s ";
		String newLine = "\r\n";
		String sqlWhere = "WHERE { " + newLine; /*+ "?a csv:City ?title. "*/
		String whereProp = "?a csv:%1$s ?%1$s. ";
		String closingBracket = "}";
		String sqlQuery = new String();
		StringBuilder selectValue = new StringBuilder();
		StringBuilder wherevalue = new StringBuilder();
		for(String prop : values) {
			selectValue.append(String.format(selectProp, prop));
			wherevalue.append(String.format(whereProp, prop)+newLine);
		}
		sqlQuery = sqlPrefix + sqlSelect + selectValue + newLine + sqlWhere + newLine + wherevalue + closingBracket;
		return sqlQuery;
	}	
}
