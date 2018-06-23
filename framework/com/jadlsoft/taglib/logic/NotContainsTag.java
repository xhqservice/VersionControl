/**
 * <p>Title: NotContainsTag </p>
 * <p>Description: 判断列表中是否存在对象的tag </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-10-16
*/

package com.jadlsoft.taglib.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.jadlsoft.utils.DomainBeanUtils;


/**
 * Abstract base class for the various conditional evaluation tags.
 *
 * @version $Rev: 54929 $ $Date: 2007/04/05 03:14:23 $
 */

public class NotContainsTag extends TagSupport {

	private static final Map scopes = new HashMap();

    /**
     * Initialize the scope names map.
     */
    
	static {
        scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
        scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
        scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
        scopes.put("application", new Integer(PageContext.APPLICATION_SCOPE));
    }
    
    // ------------------------------------------------------------- Properties
	

    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String value = null;

    public String getValue() {
        return (this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    // --------------------------------------------------------- Public Methods


    /**
     * 进行条件比较
     */
	
    public int doStartTag() throws JspException {

        if (condition())
        	return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);

    }
	

    /**
     * 正常进行页面的其他部分
     */
	
    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }
	

    /**
     * Release all allocated resources.
     */
	
    public void release() {
    	
        super.release();
        name = null;
        property = null;
        scope = null;
        value = null;
    }
	

    // ------------------------------------------------------ Protected Methods


    /**
     * 进行条件比较
     */
    private boolean condition() throws JspException {
    	Object bean = null;
    	
    	if(scope==null) {
    		bean = pageContext.findAttribute(name);
    	} else {
    		if(scopes.containsKey(scope.toLowerCase())) {
    			bean = pageContext.getAttribute(name, ((Integer)scopes.get(scope.toLowerCase())).intValue());
    		} 
    	}
    	
    	if(property!=null) {
    		if(bean==null) {
    			throw new JspException("对应对象没有找到！");
    		}
    		bean = DomainBeanUtils.getPropertyValue(bean, property);
    	}
    	
    	if(bean instanceof List) {
    		return !((List)bean).contains(value);
    	}
    	if(bean instanceof Map) {
    		return !((Map)bean).containsKey(value);
    	}
    	
    	return false;
    }


}