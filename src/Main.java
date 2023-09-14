import MLSClient.*;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Client.start(3000,"0.0.0.0");
        Client.setLicence("Radek","9f3a08745c23449a53fc05d68eda1e1b");
        try {
            Client.getLicenseToken();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        if (Client.licenceToken!=null)
        {
            System.out.println(Client.licenceToken);
        }

        Thread.sleep(30*1000);
        Client.stop();
    }
}