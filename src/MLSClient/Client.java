package MLSClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

class Client {
    public static int port= 3000;
    public static String ipAdres="0.0.0.0";
    public static String userName="none";
    public static String licenseKey= "none";
    public static LicenceToken licenceToken=null;
    public static LicenceTokenThread licenceTokenThread;



    public static void main(String[] args) throws Exception {
//        start(3000,"0.0.0.0");
//        setLicence("Radek","9f3a08745c23449a53fc05d68eda1e1b");
//
//        try {
//            getLicenseToken();
//        }catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//        Thread.sleep(15*1000);
//        stop();
    }

    private static LicenceToken getLicenseToken() throws Exception {

        if (licenceToken==null)
        {
            LicenceToken mlsResponse=getLicenceTokenFromMls();
            if (mlsResponse==null)
            {
                throw new Exception("cannot connect to MLS server");
            }else
            {
//                Thread.sleep(getTimeLeft((String)licenceToken.getExpired()) * 1000L);//odpalic watek co odlicza
                licenceTokenThread= new LicenceTokenThread();
                licenceTokenThread.start();
                return mlsResponse;
            }
        }else
        {
            return licenceToken;
        }
    }
 private static LicenceToken getLicenceTokenFromMls() throws IOException {
     try  {
         Socket socket = new Socket(ipAdres, port);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         String clientQuery = null;


         clientQuery = "{ \"LicenceUserName\": \"" + userName + "\"," +
                 " \"LicenceKey\": \"" + licenseKey + "\" }";
         out.println(clientQuery);
         out.flush();
         String response = in.readLine();
         if (response == null) {
             System.out.println("rozlaczono z serwerem");
             return null;
         }
//         System.out.println(response);
         JSONParser parser = new JSONParser();
         JSONObject jsonObject = (JSONObject) parser.parse(response);
//         System.out.println((String) jsonObject.get("Licence"));

         socket.close();
         out.close();
         in.close();

         if (((String) jsonObject.get("Licence")).equals("true")) {
             licenceToken = new LicenceToken((String) jsonObject.get("LicenceUserName"), true, (String) jsonObject.get("Expired"));
             return licenceToken;
         } else {
             System.out.println((String) jsonObject.get("Description"));
             return null;
         }
//                System.out.println(getTimeLeft((String)jsonObject.get("Expired")));

     } catch (Exception e) {
//         System.out.println(Arrays.toString(e.getStackTrace()));
     }
     return null;

}

    private static void start(int port,String ipAdres) {
        Client.port = port;
        Client.ipAdres=ipAdres;

    }
    private static void stop() {
        if (licenceTokenThread!=null)
        {
            licenceTokenThread.stopThread();
        }
        Client.port = 0;
        Client.ipAdres="";
        Client.userName="none";
        Client.licenseKey="none";
        Client.licenceToken=null;

    }

    private static void setLicence(String userName,String licenseKey) {
        Client.userName = userName;
        Client.licenseKey=licenseKey;

    }



    private static Long getTimeLeft(String expirationTimeISO) {
        if (expirationTimeISO==null)
        {
            return 0L;
        }

        Instant instant = Instant.parse(expirationTimeISO);
        Instant currentInstant = Instant.now();
        long secondsDifference = ChronoUnit.SECONDS.between(currentInstant,instant);
//        System.out.println(secondsDifference+"here");
        if (secondsDifference>=0)
        {
            return secondsDifference;
        }else
        {
            return 0L;
        }

    }

    static class LicenceTokenThread extends Thread {
        private static final long INTERVAL_MS = getTimeLeft((String)licenceToken.getExpired()) * 1000L; // 5 seconds interval
        private volatile boolean stopFlag = false;

        @Override
        public void run() {
            while (!stopFlag) {
                try {

//                    System.out.println("czekam: "+INTERVAL_MS);
                    Thread.sleep(INTERVAL_MS);
                    Thread.sleep(INTERVAL_MS);
                    if (getLicenceTokenFromMls()==null)
                    {
                        throw new Exception("cannot connect to MLS server");
                    }
                } catch (Exception e) {
                    System.out.println("server disconnected");
                    licenceToken=null;
                    stopThread();
                }
            }
        }
        public void stopThread() {
            stopFlag = true;
        }

    }

}
