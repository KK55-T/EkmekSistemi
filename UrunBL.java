public class UrunBL {

    public String kontrol(String ad, String fiyat, String stok) {
        if (ad.trim().isEmpty()) return "Ürün adı boş olamaz.";
        try {
            float f = Float.parseFloat(fiyat);
            if (f <= 0) return "Fiyat sıfırdan büyük olmalıdır.";
        } catch (NumberFormatException e) {
            return "Fiyat geçerli bir sayı olmalıdır.";
        }
        try {
            int s = Integer.parseInt(stok);
            if (s < 0) return "Stok negatif olamaz.";
        } catch (NumberFormatException e) {
            return "Stok geçerli bir tam sayı olmalıdır.";
        }
        return "OK";
    }
}