package com.jadlsoft.taglib.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.bean.WriteTag;
import org.apache.struts.util.MessageResources;

public class WriteExcel extends WriteTag {

	/**
	 * Format value according to specified format string (as tag attribute or as
	 * string from message resources) or to current user locale.
	 * 
	 * When a format string is retrieved from the message resources,
	 * <code>applyLocalizedPattern</code> is used. For more about localized
	 * patterns, see
	 * <http://www.dei.unipd.it/corsi/fi2ae-docs/source/jdk1.1.7/src/java/text/resources/>.
	 * (To obtain the correct value for some characters, you may need to view
	 * the file in a hex editor and then use the Unicode escape form in the
	 * property resources file.)
	 * 
	 * @param valueToFormat
	 *            value to process and convert to String
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	protected String formatValue(Object valueToFormat) throws JspException {
		Format format = null;
		Object value = valueToFormat;
		Locale locale = TagUtils.getInstance().getUserLocale(pageContext,
				this.localeKey);
		boolean formatStrFromResources = false;
		String formatString = formatStr;

		// Return String object as is.
		if (value instanceof java.lang.String) {
			String val = (String) value;
			return val.replaceAll("<br>", "  ");
		} else {

			// Try to retrieve format string from resources by the key from
			// formatKey.
			if ((formatString == null) && (formatKey != null)) {
				formatString = retrieveFormatString(this.formatKey);
				if (formatString != null) {
					formatStrFromResources = true;
				}
			}

			// Prepare format object for numeric values.
			if (value instanceof Number) {

				if (formatString == null) {
					if ((value instanceof Byte) || (value instanceof Short)
							|| (value instanceof Integer)
							|| (value instanceof Long)
							|| (value instanceof BigInteger)) {

						formatString = retrieveFormatString(INT_FORMAT_KEY);

					} else if ((value instanceof Float)
							|| (value instanceof Double)
							|| (value instanceof BigDecimal)) {

						formatString = retrieveFormatString(FLOAT_FORMAT_KEY);
					}

					if (formatString != null) {
						formatStrFromResources = true;
					}
				}

				if (formatString != null) {
					try {
						format = NumberFormat.getNumberInstance(locale);
						if (formatStrFromResources) {
							((DecimalFormat) format)
									.applyLocalizedPattern(formatString);
						} else {
							((DecimalFormat) format).applyPattern(formatString);
						}

					} catch (IllegalArgumentException e) {
						JspException ex = new JspException(messages.getMessage(
								"write.format", formatString));
						TagUtils.getInstance().saveException(pageContext, ex);
						throw ex;
					}
				}

			} else if (value instanceof java.util.Date) {

				if (formatString == null) {

					if (value instanceof java.sql.Timestamp) {
						formatString = retrieveFormatString(SQL_TIMESTAMP_FORMAT_KEY);

					} else if (value instanceof java.sql.Date) {
						formatString = retrieveFormatString(SQL_DATE_FORMAT_KEY);

					} else if (value instanceof java.sql.Time) {
						formatString = retrieveFormatString(SQL_TIME_FORMAT_KEY);

					} else if (value instanceof java.util.Date) {
						formatString = retrieveFormatString(DATE_FORMAT_KEY);
					}

				}

				if (formatString != null) {
					format = new SimpleDateFormat(formatString, locale);
				}
			}
		}
		if (value instanceof Number) {
			return value.toString();
		}
		if (format != null) {
			return format.format(value);
		} else {
			return value.toString();
		}

	}
}
