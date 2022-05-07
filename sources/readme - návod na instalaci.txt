Návod na instalaci aplikace

Nutný software: Android Studio, nástroj na připojení do databáze (například MySQL Workbench), nástroj na připojení aplikace na vzdálený server (například FileZilla)
Nutný hardware: Vzdálený server s nainstalovaným MySQL databázovým serverem, SSH serverem, Webovým serverm a PHP

Vytvoření databáze na serveru
1) Připojit se na databázový server pomocí MySQL Workbench nebo jiného nástroje
2) V aplikaci otevřit soubor "AndroidDB.sql"
3) Spustit otevřený script

Nahrání PHP skriptu na server
1) Připojit se na SSH server pomocí programu FileZilla nebo jiného nástroje
2) Nahrát soubor "request.php" do složky /var/www/html

Instalace Mobilní Aplikace
1) Otevřít projekt "ApplicationMarsh" v prostředí Android Studio: File->Open
2) V případě jiného serveru je třeba upravit soubor java/DBC/DatabaseRequester.java -> v každé metodě upravit parametr URL metody "client.post" na "IP_Adresa_Serveru/request.php"
3) Připojit mobilního zařízení k počítači, kde je otevřeno Android Studio s projektem
4) Build projektu: Build->Make Project
5) Nahrát aplikaci na mobilní zařízení: Run->Run 'app'