package com.sourcecode.web;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.text.ParseException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.sourcecode.util.NumberUtils;

import de.brendamour.jpasskit.PKLocation;

/**
 * 对google地图修正
 * 
 * @author toby
 */
public class MapOffset {
    private static Logger logger = org.apache.log4j.Logger.getLogger(MapOffset.class);

    public static String executeHttpGet(String requestUrl) throws ParseException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);
        HttpResponse response = httpclient.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        if (200 == statusLine.getStatusCode()) {
            String result = EntityUtils.toString(response.getEntity());
            return result;
        }
        else {
            logger.error("execute map abc error: " + ReflectionToStringBuilder.toString(statusLine));
        }
        return StringUtils.EMPTY;
    }

    /**
     * baidu google 真实坐标间转换 http://www.cnblogs.com/foxracle/archive/2012/03/22/2411402.html
     * 
     * @param lat
     * @param lng
     * @param from 来源坐标系 （0表示原始GPS坐标，2表示Google坐标） 0-真实 2-google 4-baidu坐标
     * @param to 转换后的坐标 (4就是百度自己啦，好像这个必须是4才行）
     * @throws IOException
     * @throws ParseException
     */
    public static void trans(String lat, String lng, String from, String to) throws ParseException, IOException {
        String url_templete = "http://api.map.baidu.com/ag/coord/convert?from={0}&to={1}&x={2}&y={3}";
        String url = MessageFormat.format(url_templete, from, to, lat, lng);
        System.out.println(url);
        String jsonStr = executeHttpGet(url);
        System.out.println(jsonStr);
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        BASE64Decoder decoder = new BASE64Decoder();
        String transLat = new String(decoder.decodeBuffer(jsonObject.getString("x")));
        String transLng = new String(decoder.decodeBuffer(jsonObject.getString("y")));
        System.out.println("x: " + transLat + "  y: " + transLng);
    }

    public static boolean getOffset(Double lat, Double lng) {
        StringBuilder url = new StringBuilder();
        url.append("http://ditu.google.com/maps/vp?");
        url.append("spn=0.0,0.0&z=18&vp=");
        url.append(lat);
        url.append(",");
        url.append(lng);
        String urlStr = url.toString();

        try {
            String js = executeHttpGet(urlStr);
            System.out.println("urlStr: " + urlStr + " resault: " + js);
            int x = js.lastIndexOf("[");
            int y = js.lastIndexOf("]");
            if (x > 0 && y > 0) {
                String text = js.substring(x + 1, y);
                int b = text.lastIndexOf(",");
                int a = text.lastIndexOf(",", b - 1);
                if (a > 0 && b > 0) {
                    String offsetPixX = text.substring(a + 2, b);
                    String offsetPixY = text.substring(b + 2);
                    System.out.println("Offset_x: " + offsetPixX + "  Offset_y: " + offsetPixY);
                    return true;
                }
            }
            else {
                logger.error("error 1：  lat:" + lat + "\tlng:" + lng);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 沙县小吃 32.075147, 118.753981 11875 3207 973 448 118.74876142125703 32.07718338470381
    // 察哈尔车站 32.075541, 118.751902 118.74668242125699 32.07757737592766
    // 金城路 32.075786, 118.749008 11874 3207 972 449 118.74379378567505 32.077826915889425
    // 回龙桥 32.07456, 118.75767 11875 3207 973 448 118.75245042125704 32.07659639777875
    // 虎踞北路 工商银行 据公司1500M 32.06473, 118.75680 11875 3206 973 447 118.75158042125702 32.06676207073333
    // 陆田家 竹林新村 32.069552, 118.766955 11876 3206 973 447 118.76173542125701 32.07158396358999
    // 德基广场 32.04317, 118.78509 11878 3204 971 449 118.77988115009305 32.04521164369418
    public static void main(String[] args) throws IOException, ParseException {
        // x y 32.076566,118.747892
        // 11874:3207:972:449
        // fix1 118.75310621432493 32.07452505596113
        // fix2 118.74267778567503 32.078606898476124
        double lng = 118.78509;
        double lat = 32.04317;
        // double lngS = lngToPixel(lng, 18) + 972;
        // System.out.println("lngS:" + lngS);
        // double lngs = pixelToLng(lngS, 18);
        // System.out.println("lngs:" + lngs);
        // double lngX = latToPixel(lat, 18) + 449;
        // System.out.println("lngX:" + lngX);
        // double lngx = pixelToLat(lngX, 18);
        // System.out.println("lngx:" + lngx);
        // System.out.println(lngs + " " + lngx);

        double lngS = lngToPixel(lng, 18) - 971;
        System.out.println("lngS:" + lngS);
        double lngs = pixelToLng(lngS, 18);
        System.out.println("lngs:" + lngs);
        double lngX = latToPixel(lat, 18) - 449;
        System.out.println("lngX:" + lngX);
        double lngx = pixelToLat(lngX, 18);
        System.out.println("lngx:" + lngx);
        System.out.println(lngs + " " + lngx);
        // String path = "D:\\Dropbox\\doc\\mitian\\dev\\passbook\\google地图偏移精度5米.txt";
        // readToDB(path);
        // baidu x: 118.75460213199 y: 32.082138444471
        // google 118.747996 32.076459
        baiduToGoogle();
        trans("118.75460213199", "32.082138444471", "2", "4");
    }

    public PKLocation fixLocationOffset(PKLocation location, Integer offsetLng, Integer offsetLat) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Integer latInteger = ((Double) (lat * 100)).intValue();
        Integer lngInteger = ((Double) (lng * 100)).intValue();
        double pixelLng = lngToPixel(lng, 18) - offsetLng;
        double fixedLng = pixelToLng(pixelLng, 18);

        Double pixelLat = latToPixel(lat, 18) - offsetLat;
        Double fixedLat = pixelToLat(pixelLat, 18);
        location.setLongitude(NumberUtils.setDoubleScale(fixedLng, 6));
        location.setLatitude(NumberUtils.setDoubleScale(fixedLat, 6));

        return location;
    }

    // 经度到像素X值
    public static double lngToPixel(double lng, int zoom) {
        return (lng + 180) * (256 << zoom) / 360;
    }

    // 像素X到经度
    public static double pixelToLng(double pixelX, int zoom) {
        return pixelX * 360 / (256 << zoom) - 180;
    }

    // 纬度到像素Y
    public static double latToPixel(double lat, int zoom) {
        double siny = Math.sin(lat * Math.PI / 180);
        double y = Math.log((1 + siny) / (1 - siny));
        return (128 << zoom) * (1 - y / (2 * Math.PI));
    }

    // 像素Y到纬度
    public static double pixelToLat(double pixelY, int zoom) {
        double y = 2 * Math.PI * (1 - pixelY / (128 << zoom));
        double z = Math.pow(Math.E, y);
        double siny = (z - 1) / (z + 1);
        return Math.asin(siny) * 180 / Math.PI;
    }

    public static String sql_templete =
            "insert into `EMMS_MAP_OFFSET`(`LNG`,`LAT`,`OFF_LNG`,`OFF_LAT`) values ({0},{1},{2},{3});\r\n";

    public static void readToDB(String filePath) throws IOException {
        RandomAccessFile read = new RandomAccessFile(filePath, "r");
        RandomAccessFile writer = new RandomAccessFile("D://tmp//offset.sql", "rw");
        int count = 0;
        while (true) {
            count++;
            String s = read.readLine();
            if (s == null) {
                break;
            }
            if (count % 500 == 0) {
                System.out.println("count:" + count);
            }
            else {
                String trimValue = StringUtils.trimToEmpty(s);
                if (StringUtils.isNotBlank(trimValue)) {
                    String[] values = trimValue.split(":");
                    String sql = MessageFormat.format(sql_templete, values);
                    writer.write(sql.getBytes());
                }
            }
        }
        read.close();
        writer.close();
    }

    /**
     * double logdeviation = 1.0000568461567492425578691530827;//经度偏差<br>
     * double latdeviation = 1.0002012762190961772159526495686;//纬度偏差<br>
     * google地图坐标=百度坐标*经验值
     */
    public static void baiduToGoogle() {
        String log = "118.79065";
        String lat = "32.042509";
        double googleLog = Double.parseDouble(log) * 1.0000568461567492425578691530827;
        double googleLat = Double.parseDouble(lat) * 1.0002012762190961772159526495686;
        System.out.println("log:" + googleLog + " lat:" + googleLat);
    }
}
