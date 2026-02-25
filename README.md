# RestoOS v17 (Aero-Gnome Edition)

A revolutionary Restaurant Operating System simulation built with **Java Swing**, featuring a premium **Aero Glass & Gnome** hybrid UI, simulated desktop environment, and advanced system tools.

![RestoOS Banner](https://img.shields.io/badge/RestoOS-v17.0-blue?style=for-the-badge) ![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge) ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge)

## 🌟 New Features in v17

### 🎨 Stunning Aero-Gnome UI
- **Adwaita Dark Glass**: A dark, translucent theme inspired by Gnome and Windows Vista.
- **Deep Space Aurora**: Dynamic, programmatic desktop wallpaper with vignette and energy curves.
- **Plex Design System**: Custom glass headers, buttons, and panels ( `PlexUtils`).
- **Unified Contrast**: Optimization for "Dark Mode" visibility (White Text / Dark Glass).

### 🖥️ Desktop Experience
- **Activities Menu**: A classic popup start menu launching system apps.
- **Floating Dock**: Access common apps (Files, Settings) from the desktop.
- **Window Management**: `JInternalFrame` based windowing for multitasking.

### 🛠️ Advanced System Apps
1.  **Terminal (`bash` simulation)**
    -   Commands: `ls`, `cd`, `whoami`, `date`, `neofetch`.
    -   Real file system navigation.
2.  **Control Panel**
    -   Central hub for System, User, and Network settings.
    -   Classic categorized view.
3.  **Files (Explorer)**
    -   Navigate your actual hard drive.
    -   Open text files in Notepad.
4.  **Task Manager**
    -   Monitor simulated CPU/Memory usage.
    -   View running processes.
5.  **Swing Designer (Drag & Drop)**
    -   Drag komponen (`Button`, `Label`, `TextField`, `CheckBox`) dari palette ke canvas.
    -   Geser ulang posisi komponen secara langsung di canvas.
    -   Generate kode Swing otomatis dari layout hasil desain.

### 🍽️ Core POS Functionality
-   **Cashier Point of Sale**: Full cart, checkout, and receipt system.
-   **Admin Dashboard**: Real-time Financial Reports, User Management, Table Map.
-   **Database**: Auto-syncs with local MySQL `restopos` database.

## 🚀 Getting Started

### Prerequisites
-   **Java JDK 17+**
-   **Apache Ant 1.10+**
-   **MySQL Server** (running on port 3306)

### Installation & Run
1.  Jalankan build dan run dengan Ant:
    ```bash
    ant clean copy-resources
    ant run
    ```
    atau langsung double-click `run.bat` (Windows).
2.  **Login**:
    -   User: `admin`
    -   Pass: `admin`
3.  From the Desktop, click **"Activities"** (Top Left) to launch apps.

### Terminal Commands
Try these in the new Terminal App:
```bash
guest@restoos:~$ help
guest@restoos:~$ neofetch  # Show system info
guest@restoos:~$ ls        # List files
```

## 📂 Project Structure
-   `com.kiloux.restopos.apps`: System Applications (Terminal, ControlPanel, Settings).
-   `com.kiloux.restopos.gui`: Core POS Panels (MainFrame, AdminPanel, Checkout).
-   `com.kiloux.restopos.gui.forms`: Swing Form Layer (`JPanel Form` dan `JFrame Form`).
-   `com.kiloux.restopos.ui`: Custom UI Libraries (PlexUtils, RetroButton).
-   `com.kiloux.restopos.utils`: Managers (Database, Sound).

## 🧩 Swing Form Architecture
-   Semua screen utama mengikuti pola Swing GUI berbasis `JPanel` (form konten) dan `JFrame` (window host).
-   Contoh implementasi: `SwingDesignerPanelForm` (JPanel Form) dan `SwingDesignerFrameForm` (JFrame Form).
-   Kompatibel NetBeans GUI Builder: tersedia `NbDesignerPanelForm` + `NbDesignerPanelForm.form` dan `NbDesignerFrameForm` + `NbDesignerFrameForm.form`.
-   Panel inti juga sudah disiapkan untuk Design tab NetBeans: `OnboardingPanel.form`, `LoginPanel.form`, dan `RegisterPanel.form`.

---
*Powered by Java Swing & Antipravity Engine*
