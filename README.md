# 💣 CREEPS Destroyer
```
ASCII ART
```

---

## 📖 Project Overview

**CREEPS Destroyer** is a project developed as part of the APP ING1 course at EPITA, and to showcase the full potential of the Creeps game. The objective is to push the game's mechanics to their limits, automating and optimizing every aspect to create the most efficient and complete strategy possible.

While the game technically has no definitive end, this project aims to develop a system capable of progressing as far as possible, optimizing resource management, base expansion, and survival against threats.

--- 

## ⚠️ Disclaimer  

This project was **not created to assist future students** in completing their assignments. It serves as a demonstration of what can be achieved with advanced optimization techniques.

I am **not responsible** for any misuse of this project, including potential academic dishonesty. Use this repository responsibly.

---

## 🛠️ Installation

### Prerequisites

Ensure you have the following installed before running the project:

- **Java 21+**
- **Apache Maven**
- **Creeps Server** (provided)

### Clone the Repository

```bash
git clone https://github.com/AstraaDev/CREEPS-Destroyer.git
```

## ▶️ Running the Project

### Start a Local Server  

If you want to test the project locally, you need to start a Creeps server:  

```sh
java -jar creeps-server.jar
```

This will start a local server on the default port `1664`.  

### Run CREEPS Destroyer  

To launch the project and connect to a server (local or online), use the following command:  

```sh
java -jar target/creeps-1.0.0-SNAPSHOT.jar <SERVER_HOST> <PORT> <USERNAME>
```

Replace:  
- `<SERVER_HOST>` with the server address (e.g., `localhost` for local testing).  
- `<PORT>` with the server port (default: `1664`).  
- `<USERNAME>` with your in-game login.

---

## 🚀 Features  

- [x] **Multi-Threaded System** – Efficient task scheduling for parallel execution of commands.
- [x] **Anti-Hector System** – Smart management of players and buildings to prevent the passage of Hector.
- [x] **Automated Resource Management** – Optimal gathering, refining, and utilization of resources.
- [x] **Base Building & Expansion** – Construction of necessary structures to support a growing colony.
- [x] **Unit Control & Strategy** – Intelligent unit movements and decision-making for survival and growth.
- [x] **Combat & Defense** – Protection against external threats and strategic offensive actions.
- [x] **Achievement Optimization** – Designed to unlock as many in-game achievements as possible.

---

## 📜 License  

This project is for **personal and demonstration purposes only**.  
It follows EPITA's **internal usage policies** and is **not meant for public distribution**.
