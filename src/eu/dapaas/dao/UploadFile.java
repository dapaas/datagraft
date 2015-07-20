package eu.dapaas.dao;

import java.io.File;

public class UploadFile {
  private String filetype;
  private File   file;
  private String contentType;
  private String transformation;
  private String index;

  public String getFiletype() {
    return filetype;
  }

  public void setFiletype(String filetype) {
    this.filetype = filetype;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contextType) {
    this.contentType = contextType;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public String getTransformation() {
    return transformation;
  }

  public void setTransformation(String transformation) {
    this.transformation = transformation;
  }

}
