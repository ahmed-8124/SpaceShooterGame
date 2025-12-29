# Galaxy Defender - Space Shooter with DSA Implementation

## 🎮 Project Overview
A Java-based space shooter game that implements various Data Structures and Algorithms (DSA) as core gameplay mechanics. This educational game demonstrates practical applications of computer science concepts.

## 🚀 Features
- **10+ DSA Implementations**: Linked Lists, Stacks, Queues, BST, Graph, A* Algorithm
- **Progressive Levels**: 5 difficulty levels with increasing challenge
- **Multiple Modes**: Single Player, Multiplayer, AI Battle
- **Educational Focus**: Each gameplay mechanic corresponds to a DSA concept

## 📁 Project Structure
SpaceShooterGame/
├── resources/           # Images, sounds, etc.
├── src/
│   ├── game/
│   │   ├── AIController.java
│   │   ├── AIShip.java
│   │   ├── EmotionSystem.java
│   │   ├── EnemyWave.java
│   │   ├── GameEngine.java
│   │   ├── GameLauncher.java
│   │   ├── GamePanel.java
│   │   ├── GameWindow.java
│   │   ├── Level.java
│   │   ├── LevelLinkedList.java
│   │   ├── MultiplayerWindow.java
│   │   └── PlayerShip.java
│   ├── model/
│   │   ├── Bullet.java
│   │   ├── Enemy.java
│   │   ├── Player.java
│   │   └── PowerUp.java
│   └── structures/
│       ├── BST.java
│       ├── CircularLinkedList.java
│       ├── DoublyLinkedList.java
│       ├── Graph.java
│       ├── QueueDS.java
│       ├── SinglyLinkedList.java
│       └── StackDS.java
├── Main.java
├── README.md           # You'll create this
└── .gitignore          # Important - create this!

## 🛠️ Technologies Used
- **Java 11+**
- **Swing/AWT** for GUI
- **Git/GitHub** for version control
- **Maven** for build management

## 🎯 DSA Implementations
| Data Structure | Game Application |
|----------------|------------------|
| Singly Linked List | Bullet management system |
| Doubly Linked List | Objective tracking |
| Stack | Move history & combo system |
| Queue | Enemy wave spawning |
| Binary Search Tree | High score ranking |
| Graph | Level/sector mapping |
| A* Algorithm | Enemy AI pathfinding |

## 📦 Installation & Running

### Prerequisites
- Java JDK 11 or higher
- Git (for cloning)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/SpaceShooterGame.git
   cd SpaceShooterGame