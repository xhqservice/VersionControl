<input type="hidden" name="queryparamter" />
<logic:present name="queryparamtername">
	<input type="hidden" name="queryparamtername"
		value="<bean:write name="queryparamtername"/>" />
</logic:present>
<logic:notPresent name="queryparamtername">
	<input type="hidden" name="queryparamtername" />
</logic:notPresent>
