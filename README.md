# 🌱 Botanic Garden Management System

This repository contains an application designed for managing a **Botanic Garden**, implemented using **four different architectural patterns**:  
- **MVP (Model-View-Presenter)**
- **MVVM (Model-View-ViewModel)**
- **MVC (Model-View-Controller)**
- **Client-Server Architecture**

## 📌 Project Overview

The system allows different types of users to interact with the botanic garden database:  
- **Visitors:** View and filter plants by type, species, carnivorous plants, and garden zones.  
- **Employees:** Perform CRUD operations on plant data after authentication.  
- **Administrators:** Manage users and employees, view authentication logs, and handle advanced functionalities.  

## 🏗️ Architectural Implementations

### 1️⃣ MVP (Model-View-Presenter)
- **Separation of concerns:** Business logic is moved into **Presenters**, which mediate between the UI and the data model.  
- **Components:**  
  - `MainPresenter` mediates interactions in the general user interface.  
  - `EmployeePresenter` handles CRUD operations for employees.  
  - `AdministratorPresenter` manages admin-related functionalities.  

### 2️⃣ MVVM (Model-View-ViewModel)
- **Data binding:** Uses ViewModels as an abstraction between the UI and the data model.  
- **Components:**  
  - `PlantViewModel` handles plant-related operations.  
  - `UserViewModel` manages users and authentication.  
  - Commands (`PlantCommands`, `UserCommands`) encapsulate actions like adding, deleting, filtering.  

### 3️⃣ MVC (Model-View-Controller)
- **Traditional three-tier separation:** Controller handles user actions, Model manages data, and View renders UI.  
- **Components:**  
  - `MainController` for navigation and authentication.  
  - `EmployeeController` for CRUD operations and reporting.  
  - `AdministratorController` for user and data management.  

### 4️⃣ Client-Server Architecture
- **Decouples frontend (Client) from backend (Server).**  
- **Server Components:**  
  - `Repository` layer handles database interactions.  
  - `Service` layer processes business logic.  
  - `Notification` module sends updates (Email, SMS, etc.).  
- **Client Components:**  
  - `Controller` layer communicates with the server via API.  
  - `View` layer renders data and manages UI interactions.  

## 🛠️ Technologies Used

| Component              | Technology |
|------------------------|------------|
| Programming Language   | Java       |
| UI Framework          | Swing (Java SE) |
| IDE                   | IntelliJ IDEA |
| Diagram Tool          | StarUML |
| Database Access       | Repository Pattern |
| Documentation         | Microsoft Word |

## ▶️ Running the Project

1. **Clone the repository:**
   ```sh
   git clone https://github.com/raresm2003/Botanic-Garden.git
   cd botanic-garden
   ```

2. **Run the desired version:**  
   - **MVP:** `java -jar BotanicGarden-MVP.jar`  
   - **MVVM:** `java -jar BotanicGarden-MVVM.jar`  
   - **MVC:** `java -jar BotanicGarden-MVC.jar`  
   - **Client-Server:** Start **server first**, then run the client:  
     ```sh
     java -jar BotanicGarden-Server.jar
     java -jar BotanicGarden-Client.jar
     ```

## 🚀 Future Improvements

- Implement real-time notifications in all architectures.  
- Enhance data visualization with interactive charts.  
- Add multilingual support for international users.  
- Implement REST API for better **Client-Server** communication.  

## 👨‍💻 Contributors

**[Miclea Rareș](https://github.com/your-username)**   

---


### ⭐ If you like this project, give it a star! ⭐  

