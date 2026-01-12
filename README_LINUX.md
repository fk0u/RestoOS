# RestoOS - Running on Ubuntu Linux

## Setup Selesai! ✅

Aplikasi RestoOS sudah siap dijalankan di Ubuntu Linux.

### Yang Sudah Dikonfigurasi:

1. ✅ MySQL Server terinstall dan running
2. ✅ Database `restopos` sudah dibuat
3. ✅ User database: `restopos` / password: `restopos123`
4. ✅ Tables sudah terinisialisasi
5. ✅ Script launcher untuk Linux sudah dibuat

### Cara Menjalankan:

#### Opsi 1: Dari Terminal Biasa (Recommended)
Buka terminal baru (BUKAN dari VSCode) dan jalankan:
```bash
cd /home/kou/Public/Project/RestoOS
./start.sh
```

#### Opsi 2: Dari Script run.sh
```bash
./run.sh
```

### Catatan Penting:

**Jika ada error terkait `libpthread` atau snap:**
- Error ini terjadi karena konflik antara snap VSCode dengan Java GUI
- Solusi: Jalankan aplikasi dari terminal biasa/native (bukan terminal VSCode)
- Atau buka terminal baru: Ctrl+Alt+T, lalu cd ke folder project dan jalankan `./start.sh`

### Login Credentials:

Default users yang tersedia:
- **Admin**: username: `admin`, password: `admin`
- **Kasir**: username: `kasir`, password: `kasir`  
- **Client**: username: `client`, password: `client`

### Database Info:

- Host: localhost
- Port: 3306
- Database: restopos
- User: restopos
- Password: restopos123

### Troubleshooting:

#### MySQL tidak jalan:
```bash
sudo systemctl start mysql
sudo systemctl status mysql
```

#### Cek database:
```bash
mysql -urestopos -prestopos123 -h127.0.0.1 -e "USE restopos; SHOW TABLES;"
```

#### Reset password database (jika perlu):
```bash
sudo mysql -e "ALTER USER 'restopos'@'localhost' IDENTIFIED BY 'restopos123'; FLUSH PRIVILEGES;"
```

### File Penting:

- `run.sh` - Script untuk build dan run
- `start.sh` - Alternative launcher (clean environment)
- `src/main/java/com/kiloux/restopos/config/DatabaseConfig.java` - Konfigurasi database

Selamat menggunakan RestoOS! 🍽️
