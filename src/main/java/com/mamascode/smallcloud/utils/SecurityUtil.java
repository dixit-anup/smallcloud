package com.mamascode.smallcloud.utils;

public class SecurityUtil {
	
	public static String replaceScriptTag(String src, boolean useHtml) {
		String retStr = "";
		
		if(useHtml) {
			// filtering only <script> tag
			retStr = src.replaceAll("<script>", "&lt;x-script&gt;").
					replaceAll("</script>", "&lt;/xxx-script&gt;").
					replaceAll("<script", "&lt;x-script").
					replaceAll("</script", "&lt;/xxx-script");
		} else {
			// filtering all html tag
			retStr = src.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		
		return retStr;
	}
}
