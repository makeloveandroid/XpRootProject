package wind.android.content.res;

import util.TextUtils;
import wind.v1.XmlPullParser;
import wind.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Wind
 */
public class ManifestParser {

    public static class ManifestData {
        public String packageName;
        public String applicationName;
        public String currentActivity;

        public ManifestData(String packageName, String applicationName, String currentActivity) {
            this.packageName = packageName;
            this.applicationName = applicationName;
            this.currentActivity = currentActivity;
        }
    }

    /**
     * Get the package name and the main application name from the manifest file
     * */
    public static ManifestData parseManifestFile(InputStream inputStream) {
        AXmlResourceParser parser = new AXmlResourceParser();
        String packageName = null;
        String applicationName = null;
        String currentActivity = null;
        boolean action = false;
        boolean category = false;

        try {
            parser.open(inputStream);
            while (true) {
                int type = parser.next();
                if (type == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                if (type == XmlPullParser.START_TAG) {
                    int attrCount = parser.getAttributeCount();
                    for (int i = 0; i < attrCount; i++) {
                        String attrName = parser.getAttributeName(i);

                        String name = parser.getName();

                        if ("manifest".equals(name)) {
                            if ("package".equals(attrName)) {
                                packageName = parser.getAttributeValue(i);
                            }
                        }

                        if ("application".equals(name)) {
                            if ("name".equals(attrName)) {
                                applicationName = parser.getAttributeValue(i);
                            }
                        }

                        if ("activity".equals(name)) {
                            if ("name".equals(attrName)) {
                                currentActivity = parser.getAttributeValue(i);
                                action = false;
                                category = false;
                            }
                        }

                        if ("action".equals(name)) {
                            if ("name".equals(attrName) && "android.intent.action.MAIN".equals(parser.getAttributeValue(i))) {
                                action = true;
                            }
                        }

                        if ("category".equals(name)) {
                            if ("name".equals(attrName) && "android.intent.category.LAUNCHER".equals(parser.getAttributeValue(i))) {
                                category = true;
                            }
                        }

                        if (!TextUtils.isEmpty(packageName)  && !TextUtils.isEmpty(currentActivity) && action && category) {
                            return new ManifestData(packageName, applicationName, currentActivity);
                        }
                    }
                } else if (type == XmlPullParser.END_TAG) {
                    // ignored
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            System.out.println("parseManifestFile failed, reason --> " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static class Pair {
        public String packageName;
        public String applicationName;

        public Pair(String packageName, String applicationName) {
            this.packageName = packageName;
            this.applicationName = applicationName;
        }
    }

}
