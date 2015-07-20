package eu.dapaas.dao;

import org.apache.commons.lang.StringEscapeUtils;

import com.sirmamobile.commlib.annotations.MappedObject;
import com.sirmamobile.commlib.annotations.Oneway;

@Oneway
@MappedObject(recursive = true, forced = true)
public class PortalContent {
  private String query;      // user set query
  private String chart;      // chart type
  private String id;         // auto generate id
  private String datasetId;
  private String title;
  private String datePattern;
  private String accessURL;
  private String description;
  private String summary;
  private String queryHtml;
  private String descriptionHtml;
  private String summaryHtml;
  private String chartLabel;
  private Poligon poligon;
  

  // private String chartlabel;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getChart() {
    return chart;
  }

  public void setChart(String chart) {
    this.chart = chart;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDatePattern() {
    return datePattern;
  }

  public void setDatePattern(String datePattern) {
    this.datePattern = datePattern;
  }

  public String getAccessURL() {
    return accessURL;
  }

  public void setAccessURL(String accessURL) {
    this.accessURL = accessURL;
  }

  public String getQueryHtml() {
    if (query == null){
      return "";
    }
    String queryHtml = StringEscapeUtils.escapeHtml(query);
    queryHtml = query.replaceAll("(\r\n)", "<br>");
    queryHtml = queryHtml.replaceAll("(\n)", "<br>");
    return queryHtml;
  }
  
  public String getDescriptionHtml() {
    if (description == null){
      return "";
    }
    String descriptionHtml = StringEscapeUtils.escapeHtml(description);
    descriptionHtml = description.replaceAll("(\r\n)", "<br>");
    descriptionHtml = descriptionHtml.replaceAll("(\n)", "<br>");
    return descriptionHtml;
  }
  
  public String getSummaryHtml() {
    if (summary == null){
      return "";
    }
    String summaryHtml = StringEscapeUtils.escapeHtml(summary);
    summaryHtml = summary.replaceAll("(\r\n)", "<br>");
    summaryHtml = summaryHtml.replaceAll("(\n)", "<br>");
    return summaryHtml;
  }

  public String getChartLabel() {
    switch (chart) {
    case "drawTable":
      return "Tabular View";
    case "drawLineChart":
      return "Line Chart";
    case "drawBarChart":
      return "Bar Chart";
    case "drawPieChart":
      return "Pie Chart";
    case "drawMaps":
      return "Map";
    case "drawScatterChart":
      return "Scatter Chart";
    case "drawBubbleChart":
      return "Bubble Chart";
    case "drawTimeLine":
      return "Time Line";
    case "googleMaps":
      return "Google Map";
    case "drawPoligonChart":
      return "Poligon Chart";
    default:
      return "";
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setQueryHtml(String queryHtml) {
    this.queryHtml = queryHtml;
  }

  public void setDescriptionHtml(String descriptionHtml) {
    this.descriptionHtml = descriptionHtml;
  }

  public void setSummaryHtml(String summaryHtml) {
    this.summaryHtml = summaryHtml;
  }
  
  public void setChartLabel(String chartLabel){
    this.chartLabel = chartLabel;
  }
//
//  public String getPoligonId() {
//    return poligonId;
//  }
//
//  public void setPoligonId(String poligonId) {
//    this.poligonId = poligonId;
//  }

  public Poligon getPoligon() {
    return poligon;
  }

  public void setPoligon(Poligon poligon) {
    this.poligon = poligon;
  }
}
