package com.jadlsoft.taglib.page;

import javax.servlet.jsp.tagext.*;

/**
 * 用于page标记的变量预设
 */
public class PageTEI extends TagExtraInfo {
  public VariableInfo[] getVariableInfo(TagData data) {
    return new VariableInfo[] {
      new VariableInfo("pageSize","java.lang.Integer",true,VariableInfo.NESTED),
      new VariableInfo("pageNo","java.lang.Integer",true,VariableInfo.NESTED),
      new VariableInfo("pages","java.lang.Integer",true,VariableInfo.NESTED),
      new VariableInfo("index","java.lang.Integer",true,VariableInfo.NESTED),
      new VariableInfo("total","java.lang.Integer",true,VariableInfo.NESTED)
    };
  }
}