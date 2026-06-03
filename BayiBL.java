public class BayiBL {

    public String kontrol(String ad, String yetkili, String tel, String adres) {
        if (ad.trim().isEmpty()) return "Bayi adı boş olamaz.";
        if (yetkili.trim().isEmpty()) return "Yetkili kişi boş olamaz.";
        if (tel.trim().isEmpty()) return "Telefon boş olamaz.";
        if (adres.trim().isEmpty()) return "Adres boş olamaz.";
        return "OK";
    }
}