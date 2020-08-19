/* İŞ AKIŞ WEB UYGULAMASI
*  Giriş ekranı;
*       yerel componentler g_ ile başlamaktadır.
*       Sistemde tanımlı kullanıcı adı ve şifre ile sisteme giriş yapılır.
*       Olası senaryolar için hata mesajları eklendi.
*  Evrak Onay Ekranı;
*       yerel componentler eo_ ile başlamaktadır.
*       Onay sırasının kimde olduğu bildirimi yer alır.
*       onay ve ret butonları tanımlanmıştır.
*       onay butonu ile onay sırası değişir.
*  Evrak Kabul Ekranı;
*       yerel componentler ek_ ile başlar.
*       evrak türü seçimi yapılır.
*       ardından evrak onay sırasına girer.
*  Yönetim Ekranı;
*       yerel componentler y_ ile başlar.
*       evrak türleri ile onay yetkileri ayarlanır.
*  Sistem Ekranı;
*       yerel componentler s_ ile başlar
*       Giriş ekranı ayarlamaları yapılır.
*       erişim yetkilerinin ataması yapılır.
*
*  Sistemden çıkış butonu giriş ekranına yönlendirir.
*  Kullanıcıların yetkisi olmayan sekmelere erişmesi engellenmiştir.
*  Onay Yetkisi olarak Yazılım Birim şefi ve insan kaynaklarının kesiştiği durumlarda Yazılım Birim şefi önceliklidir.
*
*  NURSULTAN OKLU
*  MAVİNCİ YAZ STAJI
*  Ağustos 2020 ANKARA,
*/
package com;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayOutputStream;

public class MyVaadinApplication extends UI{
    @Override
    public void init (VaadinRequest request){
        //kullanılan değişkenler
        final String[] kullanicilar_ad = {"kullanici_1", "kullanici_2", "kullanici_3", "kullanici_4"};
        final String[] kullanicilar_sifre = {"kullanici_1", "kullanici_2", "kullanici_3", "kullanici_4"};
        final String[] kullanici_yetki= {"Yazılım Birim Şefi", "İnsan Kaynakları", "Kullanıcı", "Sistem Uzmanı"};
        final int[] gecerli_kullanici_no = {0};

        /*Kullanıcı 1 -> Yazılım Birim Şefi: Kullanıcı no: 1
        * Kullanıcı 2 -> İnsan kaynakları: Kullanıcı no: 2
        * Kullanıcı 3 -> Sınırlı erişimli Kullanıcı: Kullanıcı no: 3
        * Kullanıcı 4 -> Sistem Uzmanı: Kullanıcı no: 4*/

        final boolean[] yazilim_sef_onay_flag = {true, true};
        final boolean[] insan_kaynaklari_onay_flag = {true, true};

        String eo_str_evrak_adi = " ";

        final String[] y_str_yazilim_sefi = {"YOK", "YOK"};
        final String[] y_str_insan_kaynaklari = {"YOK", "YOK"};
        final String[] y_str_evraklar = {"Talep Formu", "Yazılım Test Raporu"};

        final String[] s_str_sekmeler = {"Evrak Kabul", "Evrak Onay", "Yönetim", "Sistem"};
        final String[] s_str_yazilim_sefi_flag = {"YOK", "YOK", "YOK", "YOK"};
        final String[] s_str_insan_kaynaklari_flag = {"YOK", "YOK", "YOK", "YOK"};
        final String[] s_str_kullanici_flag = {"YOK", "YOK", "YOK", "YOK"};

        /*1. eleman talep formu izni
        * 2. eleman Yazılım Test Raporu izni*/
        final boolean[] y_bl_yazilim_sefi_flag = {false, false};
        final boolean[] y_bl_insan_kaynaklari_flag = {false, false};

        /*1. eleman Evrak kabul sekmesi izni
        * 2. eleman Evrak Onay sekmesi izni
        * 3. eleman Yonetim sekmesi izni*/
        final boolean[] s_bl_yazilim_sefi_flag = {false, false, false};
        final boolean[] s_bl_insan_kaynaklari_flag = {false, false, false};
        final boolean[] s_bl_kullanici_flag = {false, false, false};

        final boolean[] onay_flag = {false};//belge yüklendiğinde aktif olur.
        final boolean[] giris_onay_flag = {false};//sisteme başarılı giriş yapıldığında aktif olur
        final boolean[] btn_onay_flag = {false};//evrak bir kez onaylandığında aktif olur

        //kullanılan Katmanlar
        VerticalLayout layout_giris = new VerticalLayout();//giris ekranı componentleri g_ ile başlar
        VerticalLayout layout_anasayfa = new VerticalLayout();//anasayfa ekranı componentleri a_ ile başlar


        //{----------------------------------GİRİŞ EKRANI--------------------------------------------
        Label g_lbl_info = new Label("    İş Akış Programına Hoşgeldiniz\n" +
        "Kullanıcı Adı ve Şifre ile Giriş yapınız", ContentMode.PREFORMATTED);

        TextField g_txtbx_kullanici_adi = new TextField("Kullanıcı Adı");
        PasswordField g_txtbx_sifre = new PasswordField("Şifre");




        //{----------------------------------ANA SAYFA-----------------------------------------------
        TabSheet tabSheet = new TabSheet();
        Label a_lbl_info = new Label("\tHOŞGELDİNİZ\nİşlem Yapmak İstediğiniz Sekmeyi Seçiniz");




                //----------------------------YÖNETİM SEKMESİ-----------------------------------------
        TextArea y_txt_onay = new TextArea("        ONAY");
        y_txt_onay.setHeight("150px");
        y_txt_onay.setWidth("550px");
        y_txt_onay.setReadOnly(true);
        y_txt_onay.setRows(2);

        Label y_lbl_info = new Label("Yonetim Ekranı: Evrak türleri için onay yetkilendirmelerinin verildiği ekran");
        Label y_lbl_evrak_turu = new Label();
        Label y_lbl_evrak_turu_giris = new Label();

        RadioButtonGroup y_rd_btn_grp = new RadioButtonGroup("Evrak Türleri");
        y_rd_btn_grp.setItems("Talep Formu", "Yazılım Test Raporu");
        y_rd_btn_grp.setSelectedItem("Talep Formu");
        y_lbl_evrak_turu.setValue(y_rd_btn_grp.getValue().toString());
        y_lbl_evrak_turu_giris.setValue("İşlem Yapılan Evrak Türü: " + y_lbl_evrak_turu.getValue().toString());
        y_rd_btn_grp.addValueChangeListener(valueChangeEvent -> {
            y_lbl_evrak_turu.setValue(y_rd_btn_grp.getValue().toString());
            y_lbl_evrak_turu_giris.setValue("İşlem Yapılan Evrak Türü: " + y_lbl_evrak_turu.getValue().toString());
        });

        Label y_lbl_onay_yetki = new Label("ONAY YETKİLERİNİ SEÇİNİZ");
        CheckBox y_chck_bx_yazilim_sefi = new CheckBox("Yazılım Birim Şefi");
        CheckBox y_chck_bx_insan_kaynaklari = new CheckBox("İnsan Kaynakları");

        y_txt_onay.setValue( "Evraklar   \t\t\t|\tİnsan Kaynakları  \t\t|\tYazılım Birim Şefi\n\n" +
                "Yazılım Test Raporu\t|\t\t  " + y_str_insan_kaynaklari[1] + "  \t\t\t|\t\t" + y_str_yazilim_sefi[1] + "\n\n" +
                "Talep Formu\t\t\t|\t\t  " + y_str_insan_kaynaklari[0] + "  \t\t\t|\t\t" + y_str_yazilim_sefi[0]);

        Button y_btn_onay = new Button("ONAY", (Button.ClickListener) clickEvent -> {
            if (y_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0) {//Talep Formu
                if (y_chck_bx_yazilim_sefi.getValue()) {
                    y_bl_yazilim_sefi_flag[0] = true;
                    y_str_yazilim_sefi[0] = "VAR";
                    yazilim_sef_onay_flag[0] = false;
                } else {
                    y_bl_yazilim_sefi_flag[0] = false;
                    y_str_yazilim_sefi[0] = "YOK";
                    yazilim_sef_onay_flag[0] = true;
                }

                if (y_chck_bx_insan_kaynaklari.getValue()) {
                    y_bl_insan_kaynaklari_flag[0] = true;
                    y_str_insan_kaynaklari[0] = "VAR";
                    insan_kaynaklari_onay_flag[0] = false;
                } else {
                    y_bl_insan_kaynaklari_flag[0] = false;
                    y_str_insan_kaynaklari[0] = "YOK";
                    insan_kaynaklari_onay_flag[0] = true;
                }
            } else if (y_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0) {//Yazılım Test Raporu
                if (y_chck_bx_yazilim_sefi.getValue()) {
                    y_bl_yazilim_sefi_flag[1] = true;
                    y_str_yazilim_sefi[1] = "VAR";
                    yazilim_sef_onay_flag[1] = false;
                } else {
                    y_bl_yazilim_sefi_flag[1] = false;
                    y_str_yazilim_sefi[1] = "YOK";
                    yazilim_sef_onay_flag[1] = true;
                }
                if (y_chck_bx_insan_kaynaklari.getValue()) {
                    y_bl_insan_kaynaklari_flag[1] = true;
                    y_str_insan_kaynaklari[1] = "VAR";
                    insan_kaynaklari_onay_flag[1] = false;
                } else {
                    y_bl_insan_kaynaklari_flag[1] = false;
                    y_str_insan_kaynaklari[1] = "YOK";
                    insan_kaynaklari_onay_flag[1] = true;
                }
            }

            y_txt_onay.setValue( "Evraklar   \t\t\t|\tİnsan Kaynakları  \t\t|\tYazılım Birim Şefi\n\n" +
                    "Yazılım Test Raporu\t|\t\t  " + y_str_insan_kaynaklari[1] + "  \t\t\t|\t\t" + y_str_yazilim_sefi[1] + "\n\n" +
                    "Talep Formu\t\t\t|\t\t  " + y_str_insan_kaynaklari[0] + "  \t\t\t|\t\t" + y_str_yazilim_sefi[0]);
            Notification.show("ONAYLANDI");
        });

        Button y_btn_clear = new Button("TÜM ONAY İPTALİ", (Button.ClickListener) clickEvent -> {
            for(int i = 0; i < 2; i++){
                y_bl_insan_kaynaklari_flag[i] = false;
                y_str_insan_kaynaklari[i] = "YOK";
                y_bl_yazilim_sefi_flag[i] = false;
                y_str_yazilim_sefi[i] = "YOK";
            }

            y_txt_onay.setValue( "Evraklar   \t\t\t|\tİnsan Kaynakları  \t\t|\tYazılım Birim Şefi\n\n" +
                    "Yazılım Test Raporu\t|\t\t  " + y_str_insan_kaynaklari[1] + "  \t\t\t|\t\t" + y_str_yazilim_sefi[1] + "\n\n" +
                    "Talep Formu\t\t\t|\t\t  " + y_str_insan_kaynaklari[0] + "  \t\t\t|\t\t" + y_str_yazilim_sefi[0]);

            Notification.show("ONAYLAR İPTAL EDİLDİ");
        });

        VerticalLayout tab3 = new VerticalLayout();
        tab3.setCaption("YÖNETİM PANELİ");
        tab3.addComponent(y_lbl_info);
        tab3.addComponent(y_rd_btn_grp);
        tab3.addComponent(y_lbl_evrak_turu_giris);
        tab3.addComponent(y_lbl_onay_yetki);
        tab3.addComponent(y_chck_bx_insan_kaynaklari);
        tab3.addComponent(y_chck_bx_yazilim_sefi);
        tab3.addComponent(y_btn_onay);
        tab3.addComponent(y_btn_clear);
        tab3.addComponent(y_txt_onay);
        tabSheet.addTab(tab3);


        //----------------------------EVRAK KABUL SEKMESİ-----------------------------------------
        Label ek_lbl_info = new Label("Evrak Kabul Ekranı: Evrakları sisteme yükleyerek evrak türü seçimi yapınız.");
        Label ek_lbl_evrak_turu = new Label();
        RadioButtonGroup ek_rd_btn_grp = new RadioButtonGroup("Evrak Türleri");
        ek_rd_btn_grp.setItems("Talep Formu", "Yazılım Test Raporu");
        ek_rd_btn_grp.setSelectedItem("Talep Formu");
        Label eo_lbl_sira = new Label();
        ek_lbl_evrak_turu.setValue(ek_rd_btn_grp.getValue().toString());
        ek_rd_btn_grp.addValueChangeListener(valueChangeEvent -> {
            ek_lbl_evrak_turu.setValue(ek_rd_btn_grp.getValue().toString());
        });

        Upload upload = new Upload();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setCaption("Yüklenme Durumu");
        progressBar.setHeight("10px");
        progressBar.setWidth("50px");
        upload.setReceiver((String filename, String mimeType) -> new ByteArrayOutputStream());
        upload.addProgressListener(new Upload.ProgressListener() {//upload component ilerlemesi progress bar ile görüntüleniyor
            @Override
            public void updateProgress(long l, long l1) {
                progressBar.setCaption("Yükleniyor...");
                progressBar.setValue(upload.getUploadSize());
            }
        });

        upload.addSucceededListener(succeededEvent -> {//upload component başarı ile tamamlanırsa çalışan blok
            progressBar.setCaption("Yüklendi");
            onay_flag[0] = true;
            Notification.show("Evrak Yüklendi");
            if(!yazilim_sef_onay_flag[0]){
                eo_lbl_sira.setValue("Onay sırası: Yazılım Birim Şefi");
                eo_lbl_sira.setVisible(true);
            }
            if(yazilim_sef_onay_flag[0] && !insan_kaynaklari_onay_flag[0]){
                eo_lbl_sira.setValue("Onay sırası: İnsan Kaynakları");
                eo_lbl_sira.setVisible(true);
            }
        });

        VerticalLayout tab2 = new VerticalLayout();
        tab2.setCaption("EVRAK KABUL");
        tab2.addComponent(ek_lbl_info);
        tab2.addComponent(ek_lbl_evrak_turu);
        tab2.addComponent(ek_rd_btn_grp);
        tab2.addComponent(upload);
        tab2.addComponent(progressBar);
        tabSheet.addTab(tab2);


        //----------------------------EVRAK ONAY SEKMESİ-----------------------------------------
        Label eo_lbl_info = new Label("Evrak Onay Ekranı: Onay için Gelen Evrakları inceleyerek; uygunluğuna göre onaylayınız");
        Label eo_lbl_onay_sira = new Label();
        Label eo_lbl_bilgi = new Label();

        Button eo_btn_onay = new Button("ONAY", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(!btn_onay_flag[0]){
                    if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0 && !yazilim_sef_onay_flag[0]){
                        yazilim_sef_onay_flag[0] = true;
                        if(!insan_kaynaklari_onay_flag[0]){
                            eo_lbl_sira.setVisible(true);
                            eo_lbl_sira.setValue("Onay Sırası: İnsan Kaynakları");
                        }
                    }
                    else if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0 && !insan_kaynaklari_onay_flag[0] && yazilim_sef_onay_flag[0]){
                        insan_kaynaklari_onay_flag[0] = true;
                    }

                    if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0 && !yazilim_sef_onay_flag[1]){
                        yazilim_sef_onay_flag[1] = true;
                        if(!insan_kaynaklari_onay_flag[1]){
                            eo_lbl_sira.setVisible(true);
                            eo_lbl_sira.setValue("Onay Sırası: İnsan Kaynakları");
                        }
                    }
                    else if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0 && !insan_kaynaklari_onay_flag[1] && yazilim_sef_onay_flag[1]){
                        insan_kaynaklari_onay_flag[1] = true;
                    }
                    Notification.show("ONAYLANDI");
                    if(insan_kaynaklari_onay_flag[0] && yazilim_sef_onay_flag[0] && insan_kaynaklari_onay_flag[1] && yazilim_sef_onay_flag[1]){
                        onay_flag[0] = false;
                        eo_lbl_bilgi.setValue("Onay bekleyen evrak yok");
                        eo_lbl_sira.setVisible(false);
                        eo_lbl_onay_sira.setVisible(false);
                    }
                    btn_onay_flag[0]=true;
                }
                else {
                    Notification.show("ONAY ZATEN VERİLDİ!");
                }
            }
        });

        VerticalLayout tab1 = new VerticalLayout();
        tab1.setCaption("EVRAK ONAY");
        tab1.addComponent(eo_lbl_info);
        tab1.addComponent(eo_lbl_sira);
        tab1.addComponent(eo_lbl_bilgi);
        tab1.addComponent(eo_btn_onay);
        tabSheet.addTab(tab1);

        //----------------------------SİSTEM SEKMESİ-----------------------------------------
        TextArea s_txt_onay = new TextArea("YETKİLER");
        s_txt_onay.setWidth("850px");
        s_txt_onay.setHeight("200px");
        s_txt_onay.setReadOnly(true);

        Label s_lbl_info = new Label("Sistem Paneli: Sekme türleri için onay yetkilendirmelerinin verildiği ekran");
        Label s_lbl_sekme_turu = new Label();
        Label s_lbl_sekme_turu_giris = new Label();

        RadioButtonGroup s_rd_btn_grp = new RadioButtonGroup("SEKMELER");
        s_rd_btn_grp.setItems("Evrak Onay", "Evrak Kabul", "Yönetim");
        s_rd_btn_grp.setSelectedItem("Evrak Onay");
        s_lbl_sekme_turu.setValue(s_rd_btn_grp.getValue().toString());
        s_lbl_sekme_turu_giris.setValue("İşlem Yapılan Sekme Türü: " + s_lbl_sekme_turu.getValue().toString());
        s_rd_btn_grp.addValueChangeListener(valueChangeEvent -> {
            s_lbl_sekme_turu.setValue(s_rd_btn_grp.getValue().toString());
            s_lbl_sekme_turu_giris.setValue("İşlem Yapılan Sekme Türü: " + s_lbl_sekme_turu.getValue().toString());
        });

        Label s_lbl_onay_yetki = new Label("SEKME YETKİLERİNİ SEÇİNİZ");
        CheckBox s_chck_bx_yazilim_sefi = new CheckBox("Yazılım Birim Şefi");
        CheckBox s_chck_bx_insan_kaynaklari = new CheckBox("İnsan Kaynakları");
        CheckBox s_chck_bx_kullanici = new CheckBox("Kullanıcı");

        s_txt_onay.setValue( "Sekmeler   \t|\tYazılım Birim Şefi\t|\tİnsan Kaynakları\t|\tKullanıcı\t\n\n" +
                "Evrak Kabul\t|\t\t   " + s_str_yazilim_sefi_flag[0] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[0] + "   \t\t|\t   " + s_str_kullanici_flag[0] + "\n\n" +
                "Evrak Onay \t|\t\t   " + s_str_yazilim_sefi_flag[1] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[1] + "   \t\t|\t   " + s_str_kullanici_flag[1] + "\n\n" +
                "Yönetim\t\t|\t\t   "   + s_str_yazilim_sefi_flag[2] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[2] + "   \t\t|\t   " + s_str_kullanici_flag[2]);

        Button s_btn_onay = new Button("ONAY", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (s_lbl_sekme_turu.getValue().compareTo(s_str_sekmeler[0]) == 0) {
                    if (s_chck_bx_yazilim_sefi.getValue()) {
                        s_bl_yazilim_sefi_flag[0] = true;
                        s_str_yazilim_sefi_flag[0] = "VAR";
                    }
                    else {
                        s_bl_yazilim_sefi_flag[0] = false;
                        s_str_yazilim_sefi_flag[0] = "YOK";
                    }

                    if (s_chck_bx_insan_kaynaklari.getValue()) {
                        s_bl_insan_kaynaklari_flag[0] = true;
                        s_str_insan_kaynaklari_flag[0] = "VAR";
                    } else {
                        s_bl_insan_kaynaklari_flag[0] = false;
                        s_str_insan_kaynaklari_flag[0] = "YOK";
                    }

                    if (s_chck_bx_kullanici.getValue()) {
                        s_bl_kullanici_flag[0] = true;
                        s_str_kullanici_flag[0] = "VAR";
                    } else {
                        s_bl_kullanici_flag[0] = false;
                        s_str_kullanici_flag[0] = "YOK";
                    }

                } else if (s_lbl_sekme_turu.getValue().compareTo(s_str_sekmeler[1]) == 0) {
                    if (s_chck_bx_yazilim_sefi.getValue()) {
                        s_bl_yazilim_sefi_flag[1] = true;
                        s_str_yazilim_sefi_flag[1] = "VAR";
                    }
                    else {
                        s_bl_yazilim_sefi_flag[1] = false;
                        s_str_yazilim_sefi_flag[1] = "YOK";
                    }

                    if (s_chck_bx_insan_kaynaklari.getValue()) {
                        s_bl_insan_kaynaklari_flag[1] = true;
                        s_str_insan_kaynaklari_flag[1] = "VAR";
                    } else {
                        s_bl_insan_kaynaklari_flag[1] = false;
                        s_str_insan_kaynaklari_flag[1] = "YOK";
                    }

                    if (s_chck_bx_kullanici.getValue()) {
                        s_bl_kullanici_flag[1] = true;
                        s_str_kullanici_flag[1] = "VAR";
                    } else {
                        s_bl_kullanici_flag[1] = false;
                        s_str_kullanici_flag[1] = "YOK";
                    }

                }else if (s_lbl_sekme_turu.getValue().compareTo(s_str_sekmeler[2]) == 0) {
                    if (s_chck_bx_yazilim_sefi.getValue()) {
                        s_bl_yazilim_sefi_flag[2] = true;
                        s_str_yazilim_sefi_flag[2] = "VAR";
                    }
                    else {
                        s_bl_yazilim_sefi_flag[2] = false;
                        s_str_yazilim_sefi_flag[2] = "YOK";
                    }

                    if (s_chck_bx_insan_kaynaklari.getValue()) {
                        s_bl_insan_kaynaklari_flag[2] = true;
                        s_str_insan_kaynaklari_flag[2] = "VAR";
                    }
                    else {
                        s_bl_insan_kaynaklari_flag[2] = false;
                        s_str_insan_kaynaklari_flag[2] = "YOK";
                    }

                    if(s_chck_bx_kullanici.getValue()){
                        s_bl_kullanici_flag[2] = true;
                        s_str_kullanici_flag[2] = "VAR";
                    }else {
                        s_bl_kullanici_flag[2] = false;
                        s_str_kullanici_flag[2] = "YOK";
                    }
                }
                s_txt_onay.setValue( "Sekmeler   \t|\tYazılım Birim Şefi\t|\tİnsan Kaynakları\t|\tKullanıcı\t\n\n" +
                        "Evrak Kabul\t|\t\t   " + s_str_yazilim_sefi_flag[0] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[0] + "   \t\t|\t   " + s_str_kullanici_flag[0] + "\n\n" +
                        "Evrak Onay \t|\t\t   " + s_str_yazilim_sefi_flag[1] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[1] + "   \t\t|\t   " + s_str_kullanici_flag[1] + "\n\n" +
                        "Yönetim\t\t|\t\t   "   + s_str_yazilim_sefi_flag[2] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[2] + "   \t\t|\t   " + s_str_kullanici_flag[2]);
                Notification.show("ONAYLANDI");
            }
        });


        Button s_btn_clear = new Button("TÜM ONAY İPTALİ", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                for (int i = 0; i < 3; i++){
                    s_bl_insan_kaynaklari_flag[i] = false;
                    s_bl_yazilim_sefi_flag[i] = false;
                    s_bl_kullanici_flag[i] = false;
                    s_str_insan_kaynaklari_flag[i] = "YOK";
                    s_str_yazilim_sefi_flag[i] = "YOK";
                    s_str_kullanici_flag[i] = "YOK";
                }
                s_txt_onay.setValue( "Sekmeler   \t|\tYazılım Birim Şefi\t|\tİnsan Kaynakları\t|\tKullanıcı\t\n\n" +
                        "Evrak Kabul\t|\t\t   " + s_str_yazilim_sefi_flag[0] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[0] + "   \t\t|\t   " + s_str_kullanici_flag[0] + "\n\n" +
                        "Evrak Onay \t|\t\t   " + s_str_yazilim_sefi_flag[1] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[1] + "   \t\t|\t   " + s_str_kullanici_flag[1] + "\n\n" +
                        "Yönetim\t\t|\t\t   "   + s_str_yazilim_sefi_flag[2] + "\t\t|\t\t  " + s_str_insan_kaynaklari_flag[2] + "   \t\t|\t   " + s_str_kullanici_flag[2]);
                Notification.show("ONAYLAR İPTAL EDİLDİ");
            }
        });

        VerticalLayout tab4 = new VerticalLayout();
        tab4.setCaption("Sistem Yönetimi");
        tab4.addComponent(s_lbl_info);
        tab4.addComponent(s_rd_btn_grp);
        tab4.addComponent(s_lbl_sekme_turu_giris);
        tab4.addComponent(s_lbl_onay_yetki);
        tab4.addComponent(s_chck_bx_insan_kaynaklari);
        tab4.addComponent(s_chck_bx_yazilim_sefi);
        tab4.addComponent(s_chck_bx_kullanici);
        tab4.addComponent(s_btn_onay);
        tab4.addComponent(s_btn_clear);
        tab4.addComponent(s_txt_onay);

        tabSheet.setSelectedTab(tab1);

        Button a_btn_cikis = new Button("SİSTEMDEN ÇIKIŞ", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setContent(layout_giris);
            }
        });

        layout_anasayfa.addComponent(a_lbl_info);
        layout_anasayfa.addComponent(tabSheet);
        layout_anasayfa.addComponent(a_btn_cikis);
        //}----------------------------------ANA SAYFA-----------------------------------------------
        Label lbl_welcome = new Label();

        Button g_btn_giris = new Button("GİRİŞ", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                btn_onay_flag[0] = false;
                progressBar.reset();
                progressBar.setCaption("Yükleme Durumu");
                tabSheet.removeAllComponents();
                giris_onay_flag[0] = false;
                if(g_txtbx_kullanici_adi.isEmpty() || g_txtbx_sifre.isEmpty()){
                    Notification.show("Kullanıcı adı ya da şifre boş bırakılamaz!\n\tGiriş Başarısız");
                }
                else {
                    if (g_txtbx_kullanici_adi.getValue().compareTo(kullanicilar_ad[0]) == 0 && g_txtbx_sifre.getValue().compareTo(kullanicilar_sifre[0]) == 0) {
                        gecerli_kullanici_no[0] = 1;
                        if(s_bl_yazilim_sefi_flag[0]){
                            tabSheet.addTab(tab2);
                        }
                        if(s_bl_yazilim_sefi_flag[1]){
                            tabSheet.addTab(tab1);
                        }
                        if(s_bl_yazilim_sefi_flag[2]){
                            tabSheet.addTab(tab3);
                        }
                        setContent(layout_anasayfa);
                        giris_onay_flag[0] = true;
                    }
                    else if (g_txtbx_kullanici_adi.getValue().compareTo(kullanicilar_ad[1]) == 0 && g_txtbx_sifre.getValue().compareTo(kullanicilar_sifre[1]) == 0) {
                        gecerli_kullanici_no[0] = 2;
                        if(s_bl_insan_kaynaklari_flag[0]){
                            tabSheet.addTab(tab2);
                        }
                        if(s_bl_insan_kaynaklari_flag[1]){
                            tabSheet.addTab(tab1);
                        }
                        if(s_bl_insan_kaynaklari_flag[2]){
                            tabSheet.addTab(tab3);
                        }
                        setContent(layout_anasayfa);
                        giris_onay_flag[0] = true;
                    }
                    else if (g_txtbx_kullanici_adi.getValue().compareTo(kullanicilar_ad[2]) == 0 && g_txtbx_sifre.getValue().compareTo(kullanicilar_sifre[2]) == 0) {
                        gecerli_kullanici_no[0] = 3;
                        if(s_bl_kullanici_flag[0]){
                            tabSheet.addTab(tab2);
                        }
                        if(s_bl_kullanici_flag[1]){
                            tabSheet.addTab(tab1);
                        }
                        if(s_bl_kullanici_flag[2]){
                            tabSheet.addTab(tab3);
                        }
                        setContent(layout_anasayfa);
                        giris_onay_flag[0] = true;
                    }
                    else if (g_txtbx_kullanici_adi.getValue().compareTo(kullanicilar_ad[3]) == 0 && g_txtbx_sifre.getValue().compareTo(kullanicilar_sifre[3]) == 0) {
                        gecerli_kullanici_no[0] = 4;
                        tabSheet.addTab(tab4);
                        setContent(layout_anasayfa);
                        giris_onay_flag[0] = true;
                    }
                    else {
                        Notification.show("GİRİŞ BAŞARISIZ KULLANICI ADI VEYA ŞİFRE HATALI");
                    }
                    if(giris_onay_flag[0]){
                        eo_btn_onay.setVisible(false);
                        lbl_welcome.setValue("Ünvan: " + kullanici_yetki[(gecerli_kullanici_no[0] - 1)].toString());
                        if (!onay_flag[0]){
                            eo_lbl_bilgi.setValue("Onay bekleyen evrak yok");
                            eo_lbl_onay_sira.setVisible(false);
                            eo_btn_onay.setVisible(false);
                        }
                        else{
                            eo_lbl_bilgi.setValue("Evrak: " + eo_str_evrak_adi);
                            if(gecerli_kullanici_no[0] == 1){//yazılım birim şefi
                                if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0 && y_bl_yazilim_sefi_flag[0]){//Talep Formu gelirse ve Yetkisi varsa
                                    eo_btn_onay.setVisible(true);
                                }
                                if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0 && y_bl_yazilim_sefi_flag[1]){//Yazılım Test Raporu gelirse ve Yetkisi varsa
                                    eo_btn_onay.setVisible(true);
                                }
                            }
                            if(gecerli_kullanici_no[0] == 2){//insan kaynakları
                                if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0 ){
                                    if(y_bl_yazilim_sefi_flag[0] && yazilim_sef_onay_flag[0]){
                                        eo_btn_onay.setVisible(true);
                                    }
                                    else if(!y_bl_yazilim_sefi_flag[0]){
                                        eo_btn_onay.setVisible(true);
                                    }
                                }
                                if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0 ){
                                    if(y_bl_yazilim_sefi_flag[1] && yazilim_sef_onay_flag[1]){
                                        eo_btn_onay.setVisible(true);
                                    }
                                    else if(!y_bl_yazilim_sefi_flag[1]){
                                        eo_btn_onay.setVisible(true);
                                    }
                                }
                            }
                        }
                    }
                }

                if (!onay_flag[0]){
                    eo_lbl_bilgi.setValue("Onay bekleyen evrak yok");
                    eo_lbl_sira.setVisible(false);
                    eo_lbl_onay_sira.setVisible(false);
                    eo_btn_onay.setVisible(false);
                }
                else{
                    eo_lbl_bilgi.setValue("Onay Bekleyen Evrak: " + eo_str_evrak_adi);
                    eo_lbl_sira.setVisible(true);
                    if(gecerli_kullanici_no[0] == 1){//yazılım birim şefi
                        if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0 && y_bl_yazilim_sefi_flag[0]){//Talep Formu gelirse ve Yetkisi varsa
                            eo_btn_onay.setVisible(true);
                        }
                        if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0 && y_bl_yazilim_sefi_flag[1]){//Yazılım Test Raporu gelirse ve Yetkisi varsa
                            eo_btn_onay.setVisible(true);
                        }
                    }
                    if(gecerli_kullanici_no[0] == 2){//insan kaynakları
                        if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[0]) == 0 ){
                            if(y_bl_yazilim_sefi_flag[0] && yazilim_sef_onay_flag[0]){
                                eo_btn_onay.setVisible(true);
                            }
                            else if(!y_bl_yazilim_sefi_flag[0]){
                                eo_btn_onay.setVisible(true);
                            }
                        }
                        if(ek_lbl_evrak_turu.getValue().compareTo(y_str_evraklar[1]) == 0 ){
                            if(y_bl_yazilim_sefi_flag[1] && yazilim_sef_onay_flag[1]){
                                eo_btn_onay.setVisible(true);
                            }
                            else if(!y_bl_yazilim_sefi_flag[1]){
                                eo_btn_onay.setVisible(true);
                            }
                        }
                    }
                }
                }
        });
        layout_anasayfa.addComponent(lbl_welcome);

        //Giris Ekranı componentleri
        layout_giris.addComponent(g_lbl_info);
        layout_giris.addComponent(g_txtbx_kullanici_adi);
        layout_giris.addComponent(g_txtbx_sifre);
        layout_giris.addComponent(g_btn_giris);
        setContent(layout_giris);
    }
}
