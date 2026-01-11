# Java Restaurant POS (Retro Edition)

A Restaurant Point of Sale (POS) system built with Java Swing, featuring a unique **Retro PDA / Windows 98** aesthetic and a robust **MySQL** backend.

## 🌟 Features

### 🎨 Retro UI Design
- **Windows 98 Style**: Custom components (Buttons, Panels, Title Bars) mimicking the classic OS.
- **PDA Mode**: Optimized layout and colors (LCD Sage Green, CRT Black) for a nostalgic feel.
- **Dynamic Cart**: Real-time shopping cart updates with tax calculation.

### 🛠️ Core Functionality
- **User Authentication**: Secure Login/Register for staff (Admin/Cashier roles).
- **Table Management**: Select and manage occupied tables.
- **Menu System**: Grid-based menu with categories (Food, Drink, Dessert).
- **Order Processing**: 
  - Add items with notes.
  - Review cart totals (Subtotal + PPN 11%).
  - Save orders to Database.

### 🗄️ Database
- **Engine**: MySQL 8.x
- **Schema**: Auto-configured on first run.
- **Support**: Handles `users`, `menu_items`, `orders`, and `tables`.

## 🚀 Getting Started

### Prerequisites
- Java JDK 17 or higher.
- MySQL Server installed and running on `localhost:3306`.
  - User: `root`
  - Password: (Empty) or default. *Configure in `DatabaseConfig.java` if different.*

### Installation
1. Clone the repository.
2. Ensure MySQL service is starting.
3. Run the setup script to initialize dependencies (if needed).

### Data Initialization
The application will **automatically create** the `restopos` database and seed initial data (Admin user, Default Menu) upon the first successful connection.

### How to Run
Double-click `run.bat` or execute in terminal:
```powershell
.\run.bat
```

## 🔑 Default Credentials
- **Username**: `admin`
- **Password**: `admin`

## 🛠️ Project Structure
- `src/main/java/com/kiloux/restopos`: Source code.
  - `gui`: UI Panels (Login, Menu, Cart, etc).
  - `model`: Data entities.
  - `dao`: Database Access Objects.
  - `service`: Business logic (CartService, UserService).
  - `config`: App configuration (DB, UI Constants).

## 📝 Configuration
- **Database**: Edit `src/main/java/com/kiloux/restopos/config/DatabaseConfig.java`.
- **UI Theme**: Edit `src/main/java/com/kiloux/restopos/config/UIConfig.java`.

---
*Created by KOU | KILOUX*
