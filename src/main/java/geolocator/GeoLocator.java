package geolocator;

import java.net.URL;

import java.io.IOException;

import com.google.gson.Gson;

import com.google.common.net.UrlEscapers;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for obtaining geolocation information about an IP address or host
 * name. The class uses the <a href="http://ip-api.com/">IP-API.com</a>
 * service.
 */
public class GeoLocator {

    /**
     * URI of the geolocation service.
     */
    public static final String GEOLOCATOR_SERVICE_URI = "http://ip-api.com/json/";

    private static Gson GSON = new Gson();

    private static Logger logger;

    /**
     * Creates a <code>GeoLocator</code> object.
     */
    public GeoLocator() {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.trace("allocated new {} class", this.getClass().getName());
    }

    /**
     * Returns geolocation information about the JVM running the application.
     *
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation() throws IOException {
        return getGeoLocation(null);
    }

    /**
     * Returns geolocation information about the IP address or host name
     * specified. If the argument is <code>null</code>, the method returns
     * geolocation information about the JVM running the application.
     *
     * @param ipAddrOrHost the IP address or host name, may be {@code null}
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation(String ipAddrOrHost) throws IOException {
        logger.trace("invoke {} function", Thread.currentThread().getStackTrace()[1].getMethodName());
        logger.debug("getGeoLocation input string is {}", ipAddrOrHost);

        URL url;
        if (ipAddrOrHost != null) {
            ipAddrOrHost = UrlEscapers.urlPathSegmentEscaper().escape(ipAddrOrHost);
            url = new URL(GEOLOCATOR_SERVICE_URI + ipAddrOrHost);
            logger.debug("input is not null so url is {}", url.toString());
        } else {
            url = new URL(GEOLOCATOR_SERVICE_URI);
            logger.debug("input is null so url is {}", url.toString());
        }
        String s = IOUtils.toString(url, "UTF-8");
        logger.debug("Response {}", s);
        return GSON.fromJson(s, GeoLocation.class);
    }

    public static void main(String[] args) throws IOException {
        try {
            String arg = args.length > 0 ? args[0] : null;
            GeoLocation loc = new GeoLocator().getGeoLocation(arg);
            logger.info(loc.toString());
            System.out.println(loc);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
