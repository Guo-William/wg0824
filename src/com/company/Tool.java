package com.company;

/**
 * Created by William Guo on 8/4/2024.
 */
public class Tool {
  private String code;
  private ToolType toolType;
  private String brand;

  public Tool(String code, ToolType toolType, String brand) {
    this.code = code;
    this.toolType = toolType;
    this.brand = brand;
  }

  public String getCode() {
    return code;
  }

  public ToolType getToolType() {
    return toolType;
  }

  public String getBrand() {
    return brand;
  }
}