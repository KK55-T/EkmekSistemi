public class TeslimatBL {

    public String kontrol(String adet) {
        try {
            int a = Integer.parseInt(adet);
            if (a <= 0) return "Adet sıfırdan büyük olmalıdır.";
        } catch (NumberFormatException e) {
            return "Adet geçerli bir tam sayı olmalıdır.";
        }
        return "OK";
    }
}