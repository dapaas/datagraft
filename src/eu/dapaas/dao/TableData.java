package eu.dapaas.dao;

public class TableData {
  private String     col1;
  private String     col2;
  private String     col3;
  private String     col4;
  private String[][] values;

  /*
   * public TableData(String col1, String col2, String col3, String col4,
   * List<String> tBody) { this.col1 = col1; this.col2 = col2; this.col3 = col3;
   * this.col4 = col4; this.settBody(tBody); }
   */

  /*
   * static { TableData td = new TableData(); List<String> listOfString = new
   * ArrayList<String>(); listOfString.add("My data");
   * listOfString.add("Peter"); listOfString.add("Jan-2014");
   * listOfString.add("http://portal1.dapaas.eu");
   * 
   * td.setCol1("Dataset"); td.setCol2("User"); td.setCol3("Publisher");
   * td.setCol4("Portal"); td.settBody(listOfString); }
   */

  public String getCol1() {
    return col1;
  }

  public void setCol1(String col1) {
    this.col1 = col1;
  }

  public String getCol2() {
    return col2;
  }

  public void setCol2(String col2) {
    this.col2 = col2;
  }

  public String getCol3() {
    return col3;
  }

  public void setCol3(String col3) {
    this.col3 = col3;
  }

  public String getCol4() {
    return col4;
  }

  public void setCol4(String col4) {
    this.col4 = col4;
  }

  public String[][] getValues() {
    return values;
  }

  public void setValues(String[][] values) {
    this.values = values;
  }

}
