package org.antonakospanos.movierama.web.support;

import org.antonakospanos.movierama.web.configuration.SecurityConfiguration;
import org.antonakospanos.movierama.web.configuration.SecurityConfiguration;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class SecurityHelper {

    /**
     * Checks if is url protected.
     *
     * @param url the url
     * @return true, if is url protected
     */
    public static boolean isPublicApiUrl(String url) {
        boolean result = false;

        for (String pattern : SecurityConfiguration.getPublicApis()) {
            if (url.matches(pattern)) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Gets the request url.
     *
     * @param request the request
     * @return the request url
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String result = request.getServletPath();

        if (StringUtils.isBlank(result)) {
            result = request.getPathInfo();
        }

        return result;
    }
}
