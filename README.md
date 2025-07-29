# Ortim Download Manager - Free Edition

## 📖 Açıklama
Ortim Download Manager, YouTube ve diğer video platformlarından video/ses dosyalarını indirmek için tasarlanmış JavaFX tabanlı masaüstü uygulamasıdır. **Açık kaynak** sürüm olarak herkesin kullanımına sunulmuştur.

## ✨ Özellikler
- 🎥 **Video İndirme**: YouTube ve diğer platformlardan video indirme
- 🎵 **Ses İndirme**: MP3, AAC, FLAC, WAV gibi ses formatlarında indirme
- 🎨 **Çoklu Format Desteği**: MP4, AVI, MKV, WEBM, MOV, FLV
- 📁 **Organize İndirme**: Özel klasör yapısında düzenli indirme
- 🔧 **Otomatik Kurulum**: yt-dlp ve FFmpeg otomatik kurulumu
- 📊 **Gerçek Zamanlı Takip**: İndirme durumu ve log takibi
- 🖥️ **Modern Arayüz**: JavaFX ile modern kullanıcı arayüzü

## 🚀 Kurulum

### Gereksinimler
- Java 17 veya üzeri
- Maven 3.6+

### Adımlar
1. Projeyi klonlayın:
```bash
git clone https://github.com/your-username/OrtimDM.git
cd OrtimDM
```

2. Projeyi derleyin:
```bash
mvn clean compile
```

3. Uygulamayı çalıştırın:
```bash
mvn javafx:run
```

## 📋 Kullanım
1. Uygulamayı başlatın
2. İndirmek istediğiniz video URL'sini girin
3. "Analyze" butonuna tıklayın
4. İndirme seçeneklerini belirleyin (format, klasör)
5. "Download" butonuna tıklayın

## 🛠️ Teknik Detaylar
- **Java 17** uyumluluğu
- **JavaFX 17.0.6** kullanımı
- **yt-dlp** entegrasyonu
- **FFmpeg** desteği
- **Maven** build sistemi

## 📁 Proje Yapısı
```
OrtimDM/
├── src/main/java/com/ortimdm/ortimdm/
│   ├── controller/          # UI kontrolcüleri
│   ├── service/            # İş mantığı servisleri
│   ├── model/              # Veri modelleri
│   ├── component/          # UI bileşenleri
│   └── task/               # Arka plan görevleri
├── src/main/resources/     # FXML dosyaları ve kaynaklar
└── target/                 # Derleme çıktıları
```

## 🤝 Katkıda Bulunma
1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some AmazingFeature'`)
4. Branch'inizi push edin (`git push origin feature/AmazingFeature`)
5. Pull Request oluşturun

## 📄 Lisans
Bu proje açık kaynak olarak MIT lisansı altında yayınlanmıştır.

## 👨‍💻 Geliştirici
**orhanurullah** - [GitHub](https://github.com/orhanurullah)

## 🔄 Sürüm Geçmişi
- **v2.0** - Lisans sistemi kaldırıldı, açık kaynak sürüm
- **v1.0** - İlk sürüm (lisanslı)

## ⚠️ Uyarı
Bu uygulama sadece kişisel kullanım ve eğitim amaçlıdır. Telif hakkı olan içerikleri indirirken yasal düzenlemelere uygun hareket ediniz. 
