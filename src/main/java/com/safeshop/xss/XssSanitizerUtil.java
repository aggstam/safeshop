package com.safeshop.xss;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class XssSanitizerUtil {

    private static final Logger logger = Logger.getLogger(XssSanitizerUtil.class.getName());
    private static final List<Pattern> XSS_INPUT_PATTERNS = new ArrayList<>();

    static {
        XSS_INPUT_PATTERNS.add(Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE));
        XSS_INPUT_PATTERNS.add(Pattern.compile("<iframe(.*?)>(.*?)</iframe>", Pattern.CASE_INSENSITIVE));
        XSS_INPUT_PATTERNS.add(Pattern.compile("<input(.*?)>(.*?)</input>", Pattern.CASE_INSENSITIVE));
        XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*([^>]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("</script>", Pattern.CASE_INSENSITIVE));
        XSS_INPUT_PATTERNS.add(Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("<input(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE));
        XSS_INPUT_PATTERNS.add(Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE));
        XSS_INPUT_PATTERNS.add(Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("onfocus(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        XSS_INPUT_PATTERNS.add(Pattern.compile("<(.*?)form(.*?)>(.*?)</(.*?)form(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    }

    public static String stripXSS(String value) {
        try {
            if (value != null) {
                value = ESAPI.encoder().canonicalize(value);
                value = value.replaceAll("\0", "");
                for (Pattern xssInputPattern : XSS_INPUT_PATTERNS) {
                    value = xssInputPattern.matcher(value).replaceAll("");
                }
                if (StringUtils.isBlank(value)) {
                    return null;
                }
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Could not strip XSS from value = " + value + ". Exception: " + e.getMessage());
        }
        return null;
    }

}
