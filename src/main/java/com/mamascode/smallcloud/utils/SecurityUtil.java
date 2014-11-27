package com.mamascode.smallcloud.utils;

public class SecurityUtil {
	
	public static String replaceScriptTag(String src, boolean useHtml, String[] allowedTags) {
		String retStr = "";
		if(useHtml) {
			retStr = src.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			
			for(String tag : allowedTags) {
				String regexLower = new StringBuilder().append("&lt;")
						.append(tag.toLowerCase()).append("&gt;").toString();
				String regexUpper = new StringBuilder().append("&lt;")
						.append(tag.toUpperCase()).append("&gt;").toString();
				String replacementLower = new StringBuilder().append("<")
						.append(tag.toLowerCase()).append(">").toString();
				String replacementUpper = new StringBuilder().append("<")
						.append(tag.toUpperCase()).append(">").toString();
				
				retStr = retStr.replaceAll(regexLower, replacementLower);
				retStr = retStr.replaceAll(regexUpper, replacementUpper);
			}	
		} else {
			retStr = src.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		
		return retStr;
	}
	
	/* replaceJavaScriptTag */
	public static String replaceJavaScriptTag(String src) {
		return src.replaceAll("<script>", "&lt;x-script&gt;").
				replaceAll("</script>", "&lt;/xxx-script&gt;").
				replaceAll("<script", "&lt;x-script").
				replaceAll("</script", "&lt;/xxx-script");
	}
}
